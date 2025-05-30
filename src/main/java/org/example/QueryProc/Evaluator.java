package org.example.QueryProc;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.model.*;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Evaluator {
    private static final IAST AST = PKB.getAST();
    private final Set<Map<Argument, TNode>> finalResult = new LinkedHashSet<>();
    public Set<Map<Argument,TNode>> evaluateQuery(QueryTree queryTree) {
        finalResult.clear();
//        System.out.println(AST.getNodesOfEntityTypes(queryTree.getReturnValues().get(0).getType()));
        for(Argument arg : queryTree.getReturnValues()) {
//            System.out.println(findTNode(arg));
            mergeResults(findTNode(arg).stream()
                    .map(node -> {
                        Map<Argument, TNode> map = new LinkedHashMap<>();
                        map.put(arg, node);
                        return map;})
                    .collect(Collectors.toSet()));
        }
        //return finalResult;

        for(Relation relation : queryTree.getRelations()) {
            if(!evaluateRelation(relation,queryTree.getSynonyms(), queryTree.getReturnValues())) {
                return new LinkedHashSet<>();
            }
        }

        for( WithStatement statement : queryTree.getWithStatements()) {
            if(!evaluateWith(statement)) {
                return new LinkedHashSet<>();
            }
        }

        return finalResult;
    }
    public Boolean evaluateBooleanQuery(QueryTree queryTree) {
        finalResult.clear();

        for(Relation relation : queryTree.getRelations()) {
            if(!evaluateRelation(relation,queryTree.getSynonyms(),queryTree.getReturnValues())) {
                return false;
            }
        }

        for( WithStatement statement : queryTree.getWithStatements()) {
            if(!evaluateWith(statement)) {
                return false;
            }
        }
        return true;
    }
    private boolean evaluateRelation(Relation relation, Set<Argument> synonyms, List<Argument> returnValues) {
        RelationFunctions functions = GrammarRules.RELATION_FUNCTIONS.get(relation.getName());

        Argument arg1 = relation.getArg1();
        Argument arg2 = relation.getArg2();
        boolean isArg1Synonym = synonyms.contains(arg1);
        boolean isArg2Synonym = synonyms.contains(arg2);

        Set<Map<Argument,TNode>> result = new LinkedHashSet<>();
        if(isArg1Synonym && isArg2Synonym) {
            for(TNode node : findTNode(arg1) ) {
                functions.getByFunction().apply(node).stream()
                        .filter(n -> arg2.getType().allows(AST.getType(n)))
                        .map(n -> {
                            Map<Argument, TNode> map = new LinkedHashMap<>();
                            map.put(arg1, node);
                            map.put(arg2, n);
                            return map;
                        })
                        .forEach(result::add);
            }
            if(result.isEmpty()) return false;
        }
        else if(isArg1Synonym) {
            for(TNode node : findTNode(arg2)) {
                functions.getFunction().apply(node).stream() //todo : tutaj -Adrian 26.05.2025 modifies debug
                        .filter(n->arg1.getType().allows(AST.getType(n)))
                        .map(n -> {
                            Map<Argument, TNode> map = new LinkedHashMap<>();
                            map.put(arg1, n);
                            return map;
                        })
                        .forEach(result::add);
            }
            if(result.isEmpty()) return false;
        }
        else if(isArg2Synonym) {
            for(TNode node : findTNode(arg1)) {
                functions.getByFunction().apply(node).stream()
                        .filter(n->arg2.getType().allows(AST.getType(n)))
                        .map(n -> {
                            Map<Argument, TNode> map = new LinkedHashMap<>();
                            map.put(arg2, n);
                            return map;
                        })
                        .forEach(result::add);
            }
            if(result.isEmpty()) return false;
        }
        else {
            if(!doesSolutionExist(arg1,arg2,functions.getIsFunction())) {
                return false;
            }
//            for(Argument arg : returnValues) {
//                mergeResults(findTNode(arg).stream()
//                        .map(node -> {
//                            Map<Argument, TNode> map = new LinkedHashMap<>();
//                            map.put(arg, node);
//                            return map;})
//                        .collect(Collectors.toSet()));
//            }
//            if(arg1.getName().equals("_") && arg2.getName().equals("_") && finalResult.isEmpty()) {
//                Argument key = synonyms.iterator().next();
//                AST.getNodesOfEntityTypes(key.getType()).stream()
//                        .map(node -> {
//                            Map<Argument, TNode> map = new HashMap<>();
//                            map.put(key, node);
//                            return map;
//                        })
//                        .forEach(result::add);
//            }
        }
        mergeResults(result);
        //print(resultMap);
        return true;
    }
    private void mergeResults(Set<Map<Argument, TNode>> newNodes) {

        if(finalResult.size() == 0 || newNodes.isEmpty()) {
            finalResult.addAll(newNodes);
            return;
        }

        Set<Argument> newKeys = newNodes.iterator().next().keySet();
        Set<Argument> resultKeys = finalResult.iterator().next().keySet();

        if(resultKeys.containsAll(newKeys)) {
            finalResult.removeIf(row -> newNodes.stream()
                    .noneMatch(newRow -> newKeys.stream()
                            .allMatch(key -> newRow.get(key) == row.get(key))
            ));
        }
        else if(!Collections.disjoint(resultKeys, newKeys)) {
            Set<Map<Argument, TNode>> toDelete = new LinkedHashSet<>();
            for (Map<Argument, TNode> row : finalResult) {

                for (Map<Argument, TNode> next : newNodes) {
                    if (newKeys.stream().anyMatch(key -> next.get(key) == row.get(key))) {
                        row.putAll(next);
                    } else {
                        toDelete.add(row);
                    }
                }
            }
            toDelete.forEach(finalResult::remove);
        }
        else {
            Set<Map<Argument, TNode>> newRows = new LinkedHashSet<>();
            for (Map<Argument, TNode> row : finalResult) {
                Iterator<Map<Argument, TNode>> iterator = newNodes.iterator();
                if (!iterator.hasNext()) continue;

                Map<Argument, TNode> first = new LinkedHashMap<>(iterator.next());
                row.putAll(first);

                while (iterator.hasNext()) {
                    Map<Argument, TNode> next = new LinkedHashMap<>(row);
                    next.putAll(iterator.next());
                    newRows.add(next);
                }
            }
            finalResult.addAll(newRows);
        }
    }

    private boolean doesSolutionExist(Argument arg1, Argument arg2, BiPredicate<TNode, TNode> predicate) {
        for(TNode node1 : findTNode(arg1)) {
            for(TNode node2 : findTNode(arg2)) {
                if(predicate.test(node1,node2)) {
                    return true;
                }
            }
        }
        return false;
    }
    private Boolean evaluateWith(WithStatement withStatement) {
        Argument arg1 = withStatement.getArg1();
        Argument arg2 = withStatement.getArg2();
        Set<TNode> arguments2 = findTNode(arg2);

        if (finalResult.isEmpty() ||
                !finalResult.iterator().next().containsKey(arg1) ||
                (!finalResult.iterator().next().containsKey(arg2) &&
                        arg2.getType() != EntityType.INTEGER &&
                        arg2.getType() != EntityType.STRING)) {

            Set<Map<Argument,TNode>> nodes = arguments2.stream()
                    .map(node -> {
                        Map<Argument, TNode> map = new HashMap<>();
                        map.put(arg1, node);
                        map.put(arg2, node);
                        return map;
                    }).collect(Collectors.toSet());

            if(nodes.isEmpty()) return false;

            mergeResults(nodes);
        } else {
            Iterator<Map<Argument, TNode>> iterator = finalResult.iterator();
            while (iterator.hasNext()) {
                Map<Argument, TNode> row = iterator.next();
                if (!arguments2.contains(row.get(arg1))) {
                    iterator.remove();
                }
            }
        }
        return !finalResult.isEmpty();
    }
    private Set<TNode> findTNode(Argument arg) {
        if(arg.getType().name().equals("INTEGER")) {
            TNode node = findNodeByProgLine(Integer.parseInt(arg.getName()));
            return node == null ? Collections.emptySet() : Collections.singleton(node);
        }
        else if(arg.getType().name().equals("STRING")) {
            TNode node = findNodeByName(arg.getName());
            return node == null ? Collections.emptySet() : Collections.singleton(node);
        }
        else {
            return finalResult.stream()
                    .map(row -> row.get(arg))
                    .filter(Objects::nonNull)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toSet(),
                            set -> set.isEmpty() ? AST.getNodesOfEntityTypes(arg.getType()) : set
                    ));
        }
    }
    private TNode findNodeByProgLine(int progLine) {
        return AST.getNodesOfEntityTypes(EntityType.STMT).stream()
                .filter(node -> AST.getAttr(node).getLine() == progLine)
                .findFirst()
                .orElse(null);
    }
    private TNode findNodeByName(String name) {
        name = name.replace("\"", "");

        Set<TNode> procedures = AST.getNodesOfEntityTypes(EntityType.PROCEDURE);

        for(TNode tNode : procedures) {
            if(AST.getAttr(tNode).getProcName().equals(name)) {
                return tNode;
            }
        }

        Set<TNode> variables = AST.getNodesOfEntityTypes(EntityType.VARIABLE);

        for(TNode tNode : variables) {
            if(AST.getAttr(tNode).getVarName().equals(name)) {
                return tNode;
            }
        }
        return null;
    }
}
