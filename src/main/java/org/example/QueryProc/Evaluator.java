package org.example.QueryProc;

import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.DataStructs.Argument;
import org.example.QueryProc.DataStructs.QueryTree;
import org.example.QueryProc.DataStructs.Relation;
import org.example.QueryProc.DataStructs.RelationFunctions;

import java.util.*;
import java.util.function.BiPredicate;

public class Evaluator {
    private static final IAST IAST = PKB.getAST();
    private final static Map<String, RelationFunctions> RELATION_FUNCTIONS_MAP = Map.of(
            "Follows", new RelationFunctions(IAST::getFollowedBy,IAST::getFollows,IAST::isFollowed),
            "Follows*", new RelationFunctions(IAST::getFollowedAstraBy,IAST::getFollowsAstra,IAST::isFollowedAstra),
            "Parent", new RelationFunctions(IAST::getParentedBy,IAST::getParent,IAST::isParent),
            "Parent*", new RelationFunctions(IAST::getParentedAstraBy,IAST::getParentAstra,IAST::isParentAstra),
            "Modifies", new RelationFunctions(),
            "Modifies*", new RelationFunctions(),
            "Uses", new RelationFunctions(),
            "Uses*", new RelationFunctions();


    public Set<TNode> evaluateQuery(QueryTree queryTree) {
        List<Relation> suchThatStatements = queryTree.getRelations();

        Set<TNode> result = new HashSet<>();
        for( Relation relation : suchThatStatements) {
            result.addAll(evaluateRelation(relation,queryTree.getReturnValues()));
        }
        return result;
    }
    public Set<TNode> evaluateRelation(Relation relation, List<Argument> returnValues) {
        RelationFunctions functions = RELATION_FUNCTIONS_MAP.get(relation.getName());

        Set<TNode> result = new HashSet<>();

        if(returnValues.contains(relation.getArg1())) {
            findTNodeUsedAsParameterInASTMethod(relation.getArg2())
                    .stream()
                    .map(functions.getByFunction())
                    .forEach(result::addAll);
        }
        else if(returnValues.contains(relation.getArg2())) {
            findTNodeUsedAsParameterInASTMethod(relation.getArg1())
                    .stream()
                    .map(functions.getFunction())
                    .forEach(result::addAll);
        }
        else if(doesSolutionExist(relation.getArg1(), relation.getArg2(), functions.getIsFunction())) {
            result.addAll(IAST.getTNodes(returnValues.get(0).getType()));
        }

        return result;
    }
    private List<TNode> findTNodeUsedAsParameterInASTMethod(Argument arg) {
        List<TNode> tNodes = new ArrayList<>();
        if(arg.getType().equals("integer")) {
            System.out.println("arg int");
            tNodes.add(IAST.getTNode(Integer.parseInt(arg.getName())));
        }
        else if(arg.getType().equals("String")) {
            System.out.println("arg string");
            tNodes.add(IAST.getTNode(arg.getName()));
        }
        else {
            System.out.println("arg type");
            tNodes.addAll(IAST.getTNodes(arg.getType()));
        }
        return tNodes;
    }
    private boolean doesSolutionExist(Argument arg1, Argument arg2, BiPredicate<TNode, TNode> methode) {
        List<TNode> tNodes1 = findTNodeUsedAsParameterInASTMethod(arg1);
        List<TNode> tNodes2 = findTNodeUsedAsParameterInASTMethod(arg2);
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
