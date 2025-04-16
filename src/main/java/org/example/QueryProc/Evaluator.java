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
    public Set<TNode> evaluateQuery(QueryTree queryTree) {
        Set<Map<Argument,TNode>> finalResult = new HashSet<>();

        for( Relation relation : queryTree.relations()) {
            if(!evaluateRelation(relation,queryTree.synonyms(),finalResult)) {
                return Set.of();
            }
        }

        for( WithStatement statement : queryTree.withStatements()) {
           evaluateWith(statement, finalResult);
        }

        return finalResult.stream()
                .map(r->r.get(queryTree.returnValues().get(0)))
                .collect(Collectors.toSet());
    }
    private boolean evaluateRelation(Relation relation,Set<Argument> synonyms, Set<Map<Argument,TNode>> finalResult) {
        RelationFunctions functions = GrammarRules.RELATION_FUNCTIONS.get(relation.name());

        Argument arg1 = relation.arg1();
        Argument arg2 = relation.arg2();
        boolean isArg1Synonym = synonyms.contains(arg1);
        boolean isArg2Synonym = synonyms.contains(arg2);

        Set<Map<Argument,TNode>> result = new HashSet<>();
        if(isArg1Synonym && isArg2Synonym) {
            Set<TNode> valuesArg1 = finalResult.stream()
                    .map(row -> row.get(arg1))
                    .filter(Objects::nonNull)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toSet(),
                            set -> set.isEmpty() ? AST.getNodesOfEntityTypes(arg1.type()) : set
                    ));

            for(TNode node : valuesArg1 ) {
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
        mergeResults(result,finalResult);
        //print(resultMap);
        return true;
    }
    private void mergeResults(Set<Map<Argument, TNode>> newNodes, Set<Map<Argument,TNode>> result) {
        if(result.size() == 0 || newNodes.isEmpty()) {
            result.addAll(newNodes);
            return;
        }

        Set<Argument> newKeys = newNodes.iterator().next().keySet();
        Set<Argument> resultKeys = result.iterator().next().keySet();

        if(resultKeys.containsAll(newKeys)) {
            result.removeIf(row -> newNodes.stream()
                    .noneMatch(newRow -> newKeys.stream()
                            .allMatch(key -> newRow.get(key) == row.get(key))
            ));
        }
        else if(!Collections.disjoint(resultKeys, newKeys)) {
            Set<Map<Argument, TNode>> toDelete = new HashSet<>();
            for (Map<Argument, TNode> row : result) {

                for (Map<Argument, TNode> next : newNodes) {
                    if (newKeys.stream().anyMatch(key -> next.get(key) == row.get(key))) {
                        row.putAll(next);
                    } else {
                        toDelete.add(row);
                    }
                }
            }
            toDelete.forEach(result::remove);
        }
        else {
            Set<Map<Argument, TNode>> newRows = new HashSet<>();
            for (Map<Argument, TNode> row : result) {
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
            result.addAll(newRows);
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
//        Set<TNode> arguments1 = resultMap.stream().map(row->row.get(withStatement.arg1())).collect(Collectors.toSet());
//        if (arguments1.size() == 0 || arguments1.contains(null)) {
//            arguments1 = AST.getNodesOfEntityTypes(withStatement.arg1().type());
//        }

//        TNode tNode;
//        if(withStatement.attribute().equals("stmt#") || withStatement.attribute().equals("value")) {
//            tNode = findNodeByProgLine(Integer.parseInt(withStatement.constVal()));
//        }
//        else if(withStatement.attribute().equals("procName") || withStatement.attribute().equals("varName")) {
//            tNode = findNodeByName(withStatement.constVal());
//        }
//        else {
//            throw new NotImplementedRuntimeException("query evaluator","with statement with two synonyms hasn't been implemented yet");
//        }
//        if(tNode != null) {
//            //resultMap.get(withStatement.arg1()).retainAll(Set.of(tNode));
//        }
//        if(resultMap.isEmpty() && tNode != null) {
//            resultMap.add(new HashMap<>(Map.of(withStatement.arg1(),tNode)));
//        }
//        List<Map<Argument, TNode>> toDelete = resultMap.stream()
//                .filter(row -> row.get(withStatement.arg1()) != tNode)
//                .toList();
//
//        toDelete.forEach(resultMap::remove);
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
        else if(arg.name().equals("_")) {
            return AST.getNodesOfEntityTypes(arg.type());
        }
        return Set.of();
    }
    private TNode findNodeByProgLine(int progLine) {
        Set<TNode> tNodes = AST.getNodesOfEntityTypes(EntityType.STMT);

        for(TNode tNode : tNodes) {
            if(AST.getAttr(tNode).getLine() == progLine) {
                return tNode;
            }
        }
        return null;
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
