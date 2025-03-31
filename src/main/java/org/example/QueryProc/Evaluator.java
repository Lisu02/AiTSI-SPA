package org.example.QueryProc;

import org.example.Exceptions.NotImplementedRuntimeException;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.model.*;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.*;
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
           tNode = IAST.getTNode(Integer.parseInt(withStatement.constVal()));
        }
        else if(withStatement.type().equals("string")) {
            tNode = IAST.getTNode(withStatement.constVal());
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
            result.addAll(IAST.getTNodes(returnValues.get(0).type()));
        }

        return result;
    }
    private List<TNode> findTNodes(Argument arg) {
        List<TNode> tNodes = new ArrayList<>();
        if(arg.type().equals("integer")) {
            System.out.println("arg int");
            tNodes.add(IAST.getTNode(Integer.parseInt(arg.name())));
        }
        else if(arg.type().equals("String")) {
            System.out.println("arg string");
            tNodes.add(IAST.getTNode(arg.name()));
        }
        else {
            System.out.println("arg type");
            tNodes.addAll(IAST.getTNodes(arg.type()));
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
}
