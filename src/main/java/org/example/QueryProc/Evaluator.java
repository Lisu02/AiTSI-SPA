package org.example.QueryProc;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTImplementations.ASTGetters;
import org.example.PKB.Source.ASTImplementations.ASTModifies;
import org.example.QueryProc.model.*;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Evaluator {
    private static final IAST AST = PKB.getAST();
    private final Set<Map<Argument,TNode>> finalResult = new HashSet<>();
    public Set<TNode> evaluateQuery(QueryTree queryTree) {
        //Set<Map<Argument,TNode>> finalResult = new HashSet<>();
        finalResult.clear();

        for( Relation relation : queryTree.relations()) {
            if(!evaluateRelation(relation,queryTree.synonyms())) {
                return Set.of();
            }
        }

        for( WithStatement statement : queryTree.withStatements()) {
            evaluateWith(statement, finalResult);
        }

        return finalResult.stream()
                .map(r->r.get(queryTree.returnValues().get(0)))
                .collect(Collectors.toSet());
        //return finalResult;
    }
    public void evaluateQueryPipeTester(QueryTree queryTree) {
        //Set<Map<Argument,TNode>> finalResult = new HashSet<>();
        finalResult.clear();

        for( Relation relation : queryTree.relations()) {
            if(!evaluateRelation(relation,queryTree.synonyms())) {
                //Ja bym dal jakis wyjatek?
               // return Set.of();
            }
        }

        for( WithStatement statement : queryTree.withStatements()) {
           evaluateWith(statement, finalResult);
        }
        List<Argument> returnValues = queryTree.returnValues();
        Set<String> results = new HashSet<>();

        for (Map<Argument, TNode> mapping : finalResult) {
            boolean allPresent = returnValues.stream().allMatch(mapping::containsKey);
            if (!allPresent) {
                continue;
            }

            StringBuilder resultLine = new StringBuilder();
            for (int i = 0; i < returnValues.size(); i++) {
                Argument arg = returnValues.get(i);
                TNode node = mapping.get(arg);

                resultLine.append(AST.getAttr(node).getLine());

                if (i < returnValues.size() - 1) {
                    resultLine.append(" ");
                }
            }
            results.add(resultLine.toString());
        }
        System.out.println(String.join(",", results));

    }
    private boolean evaluateRelation(Relation relation,Set<Argument> synonyms) {
        RelationFunctions functions = GrammarRules.RELATION_FUNCTIONS.get(relation.name());

        Argument arg1 = relation.arg1();
        Argument arg2 = relation.arg2();
        boolean isArg1Synonym = synonyms.contains(arg1);
        boolean isArg2Synonym = synonyms.contains(arg2);

        Set<Map<Argument,TNode>> result = new HashSet<>();
        if(isArg1Synonym && isArg2Synonym) {
            for(TNode node : findTNode(arg1) ) {
                functions.byFunction().apply(node).stream()
                        .filter(n -> arg2.type().allows(AST.getType(n)))
                        .map(n -> new HashMap<>(Map.of(arg1, node, arg2, n)))
                        .forEach(result::add);
            }
        }
        else if(isArg1Synonym) {
            for(TNode node : findTNode(arg2)) {
                functions.function().apply(node).stream()
                        .filter(n->arg1.type().allows(AST.getType(n)))
                        .map(n->new HashMap<>(Map.of(arg1,n)))
                        .forEach(result::add);
            }
        }
        else if(isArg2Synonym) {
            for(TNode node : findTNode(arg1)) {
                functions.byFunction().apply(node).stream()
                        .filter(n->arg2.type().allows(AST.getType(n)))
                        .map(n->new HashMap<>(Map.of(arg2,n)))
                        .forEach(result::add);
            }
        }
        else {
            if(!doesSolutionExist(arg1,arg2,functions.isFunction())) {
                return false;
            }
            if(arg1.name().equals("_") && arg2.name().equals("_") && finalResult.isEmpty()) {
                Argument key = synonyms.iterator().next();
                AST.getNodesOfEntityTypes(key.type()).stream()
                        .map(n->new HashMap<>(Map.of(key,n)))
                        .forEach(result::add);
            }
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
            Set<Map<Argument, TNode>> toDelete = new HashSet<>();
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
            Set<Map<Argument, TNode>> newRows = new HashSet<>();
            for (Map<Argument, TNode> row : finalResult) {
                Iterator<Map<Argument, TNode>> iterator = newNodes.iterator();
                if (!iterator.hasNext()) continue;

                Map<Argument, TNode> first = new HashMap<>(iterator.next());
                row.putAll(first);

                while (iterator.hasNext()) {
                    Map<Argument, TNode> next = new HashMap<>(row);
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
    private void evaluateWith(WithStatement withStatement,  Set<Map<Argument,TNode>> resultMap) {
        Argument arg1 = withStatement.arg1();
        Argument arg2 = withStatement.arg2();
        Set<TNode> arguments2 = findTNode(arg2);

        if(resultMap.isEmpty()) {
            arguments2.stream()
                    .map(node->new HashMap<>(Map.of(arg1, node, arg2, node)))
                    .forEach(finalResult::add);
        }
        else {
            List<Map<Argument, TNode>> toDelete = resultMap.stream()
                    .filter(row -> !arguments2.contains(row.get(arg1)))
                    .toList();

            toDelete.forEach(resultMap::remove);
        }
    }
    private Set<TNode> findTNode(Argument arg) {
        if(arg.type().name().equals("INTEGER")) {
            TNode node = findNodeByProgLine(Integer.parseInt(arg.name()));
            return node == null ? Set.of() : Set.of(node);
        }
        else if(arg.type().name().equals("STRING")) {
            TNode node = findNodeByName(arg.name());
            return node == null ? Set.of() : Set.of(node);
        }
        else {
            return finalResult.stream()
                    .map(row -> row.get(arg))
                    .filter(Objects::nonNull)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toSet(),
                            set -> set.isEmpty() ? AST.getNodesOfEntityTypes(arg.type()) : set
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
    private void print(Set<Map<Argument, TNode>> resultMap) {
        for(Map<Argument, TNode> row : resultMap) {
            for(Argument key : row.keySet()) {
                TNode node = row.get(key);
                String attribute = "";
                Set<EntityType> stmtTypes = Set.of(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL);
                if(stmtTypes.contains(AST.getType(node))) {
                    attribute = AST.getAttr(node).getLine() + "";
                }
                else if(AST.getType(node) == EntityType.PROCEDURE) {
                    attribute = AST.getAttr(node).getProcName();
                }
                else if(AST.getType(node) == EntityType.VARIABLE) {
                    attribute = AST.getAttr(node).getVarName();
                }

                System.out.print("(" + key + " " + AST.getType(node) + " " + attribute + ") ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
