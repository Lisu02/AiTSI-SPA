package org.example.QueryProc;

import org.example.Exceptions.NotImplementedRuntimeException;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluator {
    //I set return type as String because I didn't have better idea. Feel free to change it as you wish.
    private IAST IAST = PKB.getAST();
    private QueryTree queryTree;
    public Set<TNode> evaluateQuery(QueryTree queryTree) {
        this.queryTree = queryTree;
        List<Relation> suchThatStatements = this.queryTree.getRelations();

        Set<TNode> result = new HashSet<>();
        for( Relation relation : suchThatStatements) {
            switch (relation.getName()) {
                case "Follows":
                    result.addAll(follows(relation.getArg1(), relation.getArg2()));
                case "Parent":
                    result.addAll(parent(relation.getArg1(), relation.getArg2()));
                case "Modifies":
                    result.addAll(modifies(relation.getArg1(), relation.getArg2()));
                case "Uses":
                    result.addAll(uses(relation.getArg1(), relation.getArg2()));
                default:
                    System.out.println("default result");
            }
        }
        return result;
    }
    private Set<TNode> follows(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();

        if(this.queryTree.isThisArgumentReturnValue(arg1)) {
            if(arg2.getType().equals("integer")) {
                System.out.println("arg1 int");
                TNode node = IAST.getNode(Integer.parseInt(arg2.getName()));
                result.add(IAST.getFollowedBy(node));
            }
            else if(arg2.getType().equals("prog_line")) {
                throw new NotImplementedRuntimeException("Query evaluator","prog_line not implemented");
            }
            else {
                System.out.println("arg1 type");
                List<TNode> nodes = IAST.getTNodes(arg2.getType());
                for(TNode node : nodes) {
                    result.add(IAST.getFollowedBy(node));
                }
            }
        }
        else if(this.queryTree.isThisArgumentReturnValue(arg2)) {
            if(arg1.getType().equals("integer")) {
                System.out.println("arg2 int");
                TNode node = IAST.getNode(Integer.parseInt(arg1.getName()));
                result.add(IAST.getFollows(node));
            }
            else if(arg1.getType().equals("prog_line")) {
                throw new NotImplementedRuntimeException("Query evaluator","prog_line not implemented");
            }
            else {
                System.out.println("arg2 type");
                List<TNode> nodes = IAST.getTNodes(arg1.getType());
                for(TNode node : nodes) {
                    result.add(IAST.getFollows(node));
                }
            }
        }
        else {
            boolean solutionFound = false;
            if(arg1.getType().equals("integer")) {
                System.out.println("arg1 int w");
                TNode node = IAST.getNode(Integer.parseInt(arg1.getName()));
                solutionFound = IAST.getFollows(node) != null;
            }
            else if(arg2.getType().equals("integer")) {
                System.out.println("arg2 int w");
                TNode node = IAST.getNode(Integer.parseInt(arg2.getName()));
                solutionFound = IAST.getFollowedBy(node) != null;
            }
            else {
                System.out.println("type w");
                List<TNode> nodes = IAST.getTNodes(arg2.getType());
                for(TNode node : nodes) {
                    solutionFound = IAST.getFollowedBy(node) != null;
                    if(solutionFound) {
                        break;
                    }
                }
            }
            if(solutionFound) {
                System.out.println("w true");
                result = new HashSet<>(IAST.getTNodes(queryTree.getSynonyms().get(queryTree.getReturnValues().get(1))));
            }
        }

        return result;
    }
    private Set<TNode> parent(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> modifies(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> uses(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }

}
