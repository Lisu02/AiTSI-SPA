package org.example.QueryProc;

import org.example.Exceptions.NotImplementedRuntimeException;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.model.*;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.*;
import java.util.stream.Collectors;

public class Evaluator {
    private static final IAST AST = PKB.getAST();

    public Set<TNode> evaluateQuery(QueryTree queryTree) {
//        Map<Argument, Set<TNode>> resultMap = new HashMap<>();
//        queryTree.synonyms().forEach((name, type) -> resultMap.put(new Argument(name,type), new HashSet<>(AST.getNodesOfEntityTypes(type))));
        Set<Map<Argument,TNode>> resultMap = new HashSet<>();

        for( Relation relation : queryTree.relations()) {
            if(!evaluateRelation(relation,queryTree.synonyms(),resultMap)) {
                return Set.of();
            }
        }

        for( WithStatement statement : queryTree.withStatements()) {
           evaluateWith(statement, resultMap);
        }

        //return resultMap.get(queryTree.returnValues().get(0));
        return resultMap.stream().map(r->r.get(queryTree.returnValues().get(0))).collect(Collectors.toSet());
    }
    private void evaluateWith(WithStatement withStatement,  Set<Map<Argument,TNode>> resultMap) {
        Set<TNode> arguments1 = resultMap.stream().map(row->row.get(withStatement.arg1())).collect(Collectors.toSet());
        if (arguments1.size() == 0 || arguments1.contains(null)) {
            arguments1 = AST.getNodesOfEntityTypes(withStatement.arg1().type());
        }

        TNode tNode;
        if(withStatement.attribute().equals("stmt#") || withStatement.attribute().equals("value")) {
           tNode = findNodeByProgLine(Integer.parseInt(withStatement.constVal()));
        }
        else if(withStatement.attribute().equals("procName") || withStatement.attribute().equals("varName")) {
            tNode = findNodeByName(withStatement.constVal());
        }
        else {
            throw new NotImplementedRuntimeException("query evaluator","with statement with two synonyms hasn't been implemented yet");
        }
        if(tNode != null) {
            //resultMap.get(withStatement.arg1()).retainAll(Set.of(tNode));
        }
        if(resultMap.isEmpty() && tNode != null) {
            resultMap.add(new HashMap<>(Map.of(withStatement.arg1(),tNode)));
        }
        List<Map<Argument, TNode>> toDelete = resultMap.stream()
                .filter(row -> row.get(withStatement.arg1()) != tNode)
                .toList();

        toDelete.forEach(resultMap::remove);
    }
    private boolean evaluateRelation(Relation relation,Set<Argument> synonyms, Set<Map<Argument,TNode>> resultMap) {
        RelationFunctions functions = GrammarRules.RELATION_FUNCTIONS.get(relation.name());

        Set<Map<Argument,TNode>> result = new HashSet<>();

//        if(relation.arg1().name().equals("_") && relation.arg2().name().equals("_") && resultMap.isEmpty()) {
//            for(TNode node1 : findTNode(relation.arg1())) {
//                for(TNode node2 : findTNode(relation.arg2())) {
//                    System.out.println(AST.getType(node1) + " " + AST.getAttr(node1).getLine() + " == " + AST.getType(node2) + " " + AST.getAttr(node2).getLine());
//                    if(functions.isFunction().test(node1,node2)) {
//                        System.out.println("!");
//                        Argument key = synonyms.iterator().next();
//                        TNode node = AST.getType(node1) == key.type() ? node1 : node2;
//                        resultMap.add(new HashMap<>(Map.of(key, node)));
//                    }
//                }
//            }
//            return true;
//        }
        if(!synonyms.contains(relation.arg1()) && !synonyms.contains(relation.arg2())) {
            for(TNode node1 : findTNode(relation.arg1())) {
                for(TNode node2 : findTNode(relation.arg2())) {
                    if(functions.isFunction().test(node1,node2)) {
                        if(relation.arg1().name().equals("_") && relation.arg2().name().equals("_") && resultMap.isEmpty()) {
                            Argument key = synonyms.iterator().next();
                            resultMap.addAll(AST.getNodesOfEntityTypes(key.type()).stream().map(n->new HashMap<>(Map.of(key,n))).collect(Collectors.toSet()));
                        }
                        return true;
                    }
                }
            }
            return false;
        }
        else if(!synonyms.contains(relation.arg1())) {
            for(TNode node : findTNode(relation.arg1())) {
//                arguments2.addAll(functions.byFunction().apply(node));
//                result.add(new HashMap<>(Map.of(relation.arg2(), node)));
                result.addAll(functions.byFunction().apply(node).stream()
                        .filter(n->relation.arg2().type().allows(AST.getType(n)))
                        .map(n->new HashMap<>(Map.of(relation.arg2(),n))).toList());
            }
//            addRows(arguments2,relation.arg2(),resultMap);
        }
        else if(!synonyms.contains(relation.arg2())) {
            for(TNode node : findTNode(relation.arg2())) {
//                arguments1.addAll(functions.function().apply(node));
//                result.add(new HashMap<>(Map.of(relation.arg1(), node)));
                result.addAll(functions.function().apply(node).stream()
                        .filter(n->relation.arg1().type().allows(AST.getType(n)))
                        .map(n->new HashMap<>(Map.of(relation.arg1(),n))).toList());
                System.out.println(result);
            }
//            addRows(arguments1,relation.arg1(),resultMap);
        }
        else {
            Set<TNode> arguments1 = resultMap.stream().map(row->row.get(relation.arg1())).collect(Collectors.toSet());
            if (arguments1.size() == 0 || arguments1.contains(null)) {
                arguments1 = AST.getNodesOfEntityTypes(relation.arg1().type());
            }
            System.out.println(arguments1 + " " + relation.arg1());
            for(TNode valueArg1 : arguments1) {
                var tmp = functions.byFunction().apply(valueArg1);
                for(TNode valueArg2 : tmp) {
                    if(relation.arg2().type().allows(AST.getType(valueArg2))) {
                        result.add(new HashMap<>(Map.of(relation.arg1(), valueArg1, relation.arg2(), valueArg2)));
                    }
                }
            }
        }
        addRows(result,resultMap);
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
        return true;
    }
    private void addRows(Set<Map<Argument, TNode>> newNodes, Set<Map<Argument,TNode>> result) {
        if (newNodes.isEmpty()) {
            return;
        }

        List<Argument> keys = newNodes.iterator().next().keySet().stream().toList();

        if(result.size() == 0) {
            result.addAll(newNodes);
        }
        else if(result.iterator().next().keySet().containsAll(newNodes.iterator().next().keySet())) {
            List<Map<Argument, TNode>> toDelete = new ArrayList<>();
            for(Map<Argument, TNode> row : result) {
                for(Argument key : newNodes.iterator().next().keySet()) {
                    if(newNodes.stream().map(r->r.get(key)).noneMatch(n-> n == row.get(key))) {
                        toDelete.add(row);
                    }
                }
            }
            toDelete.forEach(result::remove);
        }
        else if(result.iterator().next().containsKey(keys.get(0)) || result.iterator().next().containsKey(keys.get(1))) {
            List<Map<Argument, TNode>> toDelete = new ArrayList<>();

            for (Map<Argument, TNode> row : result) {
                Iterator<Map<Argument, TNode>> iterator = newNodes.iterator();
                if (!iterator.hasNext()) continue;

                while (iterator.hasNext()) {
                    Map<Argument, TNode> next = iterator.next();
                    if(next.get(keys.get(0)) == row.get(keys.get(0)) || next.get(keys.get(1)) == row.get(keys.get(1))) {
                        row.putAll(next);
                    }
                    else {
                        toDelete.add(row);
                    }
                }
            }
            toDelete.forEach(result::remove);
        }
        else {
            List<Map<Argument, TNode>> newRows = new ArrayList<>();

            for (Map<Argument, TNode> row : result) {
                Iterator<Map<Argument, TNode>> iterator = newNodes.iterator();
                if (!iterator.hasNext()) continue;

                // Dodaj pierwszy element do istniejącej mapy
                Map<Argument, TNode> first = iterator.next();

                 //Każdy kolejny powoduje utworzenie nowej mapy
                while (iterator.hasNext()) {
                    Map<Argument, TNode> next = iterator.next();
                    next.putAll(row);
                    newRows.add(next);
                }

                for(Argument key : first.keySet()) {
                    row.put(key, first.get(key));
                }
            }
            result.addAll(newRows);
        }
    }
//    private void addRows(Set<TNode> newNodes, Argument key, List<Map<Argument,TNode>> result) {
//        if(result.size() == 0) {
//            for (TNode node : newNodes) {
//                result.add(new HashMap<>(Map.of(key, node)));
//            }
//        }
//        else if(result.get(0).containsKey(key)) {
//            result = result.stream().filter(r->newNodes.contains(r.get(key))).toList();
//        }
//        else {
//            List<Map<Argument, TNode>> newRows = new ArrayList<>();
//
//            for (Map<Argument, TNode> row : result) {
//                Iterator<TNode> iterator = newNodes.iterator();
//                if (!iterator.hasNext()) continue;
//
//                // Dodaj pierwszy element do istniejącej mapy
//                TNode first = iterator.next();
//                row.put(key, first);
//
//                // Każdy kolejny powoduje utworzenie nowej mapy
//                while (iterator.hasNext()) {
//                    TNode next = iterator.next();
//                    Map<Argument, TNode> newRow = new HashMap<>(row); // kopia z aktualnego row
//                    newRow.put(key, next);
//                    newRows.add(newRow);
//                }
//            }
//            result.addAll(newRows);
//        }
//    }
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
}
