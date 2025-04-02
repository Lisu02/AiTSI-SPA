package org.example.QueryProc;

import org.example.Exceptions.NotImplementedRuntimeException;
import org.example.QueryProc.model.*;
import org.example.PKB.API.*;
import org.example.PKB.Source.ASTNode;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

public class Evaluator {
    private static final IAST IAST = PKB.getAST();

    public Set<TNode> evaluateQuery(QueryTree queryTree) {
        Set<TNode> result = new HashSet<>();

        //We must add system where while searching for TNodes meeting our conditions we only check TNodes return by previous relation or with statement
        for( Relation relation : queryTree.relations()) {
            Set<TNode> tNodes = evaluateRelation(relation,queryTree.returnValues());
            if(result.isEmpty()) {
                result.addAll(tNodes);
            }
            else {
                result.retainAll(tNodes);
            }
        }

        for( WithStatement statement : queryTree.withStatements()) {
            Set<TNode> tNodes = evaluateWith(statement);
            if(result.isEmpty()) {
                result.addAll(tNodes);
            }
            else {
                result.retainAll(tNodes);
            }
        }

        return result;
    }
    public Set<TNode> evaluateWith(WithStatement withStatement) {
        Set<TNode> result = new HashSet<>();

        TNode tNode;
        if(withStatement.type().equals("integer")) {
           tNode = findNodeByProgLine(Integer.parseInt(withStatement.constVal()));
        }
        else if(withStatement.type().equals("string")) {
            tNode = findNodeByName(withStatement.constVal());
        }
        else {
            throw new NotImplementedRuntimeException("query evaluator","with statement with two synonyms hasn't been implemented yet");
        }
        result.add(tNode);

        return result;
    }
    public Set<TNode> evaluateRelation(Relation relation, List<Argument> returnValues) {
        RelationFunctions functions = GrammarRules.RELATION_FUNCTIONS.get(relation.name());

        Set<TNode> result = new HashSet<>();

        if(returnValues.contains(relation.arg1())) {
//            List<TNode> list = findTNodes(relation.arg2());
//
////            list.stream().map(n->(ASTNode) n).forEach(n->System.out.println(n.getAttr()));
//            List<TNode> set = functions.byFunction().apply(list.get(0));
//            set.stream().map(n->(ASTNode) n).forEach(n->System.out.println(n.getAttr()));
            findTNodes(relation.arg2())
                    .stream()
                    .map(functions.byFunction())
                    .forEach(result::addAll);
        }
        else if(returnValues.contains(relation.arg2())) {
            findTNodes(relation.arg1())
                    .stream()
                    .map(functions.function())
                    .forEach(result::addAll);
        }
        else if(solutionExists(relation.arg1(), relation.arg2(), functions.isFunction())) {
            result.addAll(IAST.getNodesOfEntityTypes(returnValues.get(0).type()));
        }

        return result;
    }
    private List<TNode> findTNodes(Argument arg) {
        List<TNode> tNodes = new ArrayList<>();
        if(arg.type().name().equals("INTEGER")) {
            System.out.println("arg int");
            TNode tNode = findNodeByProgLine(Integer.parseInt(arg.name()));
            if(tNode != null) {
                tNodes.add(tNode);
            }

        }
        else if(arg.type().name().equals("STRING")) {
            System.out.println("arg string");
            tNodes.add(findNodeByName(arg.name()));
        }
        else {
            System.out.println("arg type");
            tNodes.addAll(IAST.getNodesOfEntityTypes(arg.type()));
        }
        return tNodes;
    }
    private boolean solutionExists(Argument arg1, Argument arg2, BiPredicate<TNode, TNode> methode) {
        List<TNode> tNodes1 = findTNodes(arg1);
        List<TNode> tNodes2 = findTNodes(arg2);
        for(TNode tNode1 : tNodes1) {
            for(TNode tNode2 : tNodes2) {
                if(methode.test(tNode1, tNode2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private TNode findNodeByProgLine(int progLine) {
        List<TNode> tNodes = IAST.getNodesOfEntityTypes(EntityType.STMT);

        for(TNode tNode : tNodes) {
            if(tNode instanceof ASTNode) {
                if(((ASTNode) tNode).getAttr().getLine() == progLine) {
                    return tNode;
                }
            }
        }
        return null;
    }
    private TNode findNodeByName(String name) {
//        IVarTable varTable = PKB.getVarTable();
//        int index = varTable.getVarIndex(name);
//        if(index > 0) {
//            return varTable.getVarName(index);
//        }

        List<TNode> procedures = IAST.getNodesOfEntityTypes(EntityType.PROCEDURE);

        for(TNode tNode : procedures) {
            if(tNode instanceof ASTNode) {
                if(((ASTNode) tNode).getAttr().getProcName().equals(name)) {
                    return tNode;
                }
            }
        }

        List<TNode> variables = IAST.getNodesOfEntityTypes(EntityType.VARIABLE);

        for(TNode tNode : variables) {
            if(tNode instanceof ASTNode) {
                if(((ASTNode) tNode).getAttr().getVarName().equals(name)) {
                    return tNode;
                }
            }
        }
        return null;
    }
}
