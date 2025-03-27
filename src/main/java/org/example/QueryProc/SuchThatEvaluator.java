package org.example.QueryProc;

import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SuchThatEvaluator {
    private final IAST IAST = PKB.getAST();
    private List<Argument> returnValues;
    private final Map<String, BiFunction<Argument, Argument, Set<TNode>>> relationHandlers = Map.of(
            "Follows", this::follows,
            "Follows*", this::followsAstra,
            "Parent", this::parent,
            "Parent*", this::parentAstra,
            "Modifies", this::modifies,
            "Modifies*", this::modifiesAstra,
            "Uses", this::uses,
            "Uses*", this::usesAstra);

    public Set<TNode> evaluate(Relation relation) {
        return relationHandlers
                .getOrDefault(relation.getName(), (a, b) -> Collections.emptySet())
                .apply(relation.getArg1(), relation.getArg2());
    }
    public void setReturnValues(List<Argument> returnValues) {
        this.returnValues = returnValues;
    }
    private Set<TNode> follows(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();

//        if(this.queryTree.isThisArgumentReturnValue(arg1)) {
//            if(arg2.getType().equals("integer")) {
//                System.out.println("arg1 int");
//                TNode node = IAST.getTNode(Integer.parseInt(arg2.getName()));
//                result.add(IAST.getFollowedBy(node));
//            }
//            else if(arg2.getType().equals("prog_line")) {
//                throw new NotImplementedRuntimeException("Query evaluator","prog_line not implemented");
//            }
//            else {
//                System.out.println("arg1 type");
//                List<TNode> nodes = IAST.getTNodes(arg2.getType());
//                for(TNode node : nodes) {
//                    result.add(IAST.getFollowedBy(node));
//                }
//            }
//        }
//        else if(this.queryTree.isThisArgumentReturnValue(arg2)) {
//            if(arg1.getType().equals("integer")) {
//                System.out.println("arg2 int");
//                TNode node = IAST.getTNode(Integer.parseInt(arg1.getName()));
//                result.add(IAST.getFollows(node));
//            }
//            else if(arg1.getType().equals("prog_line")) {
//                throw new NotImplementedRuntimeException("Query evaluator","prog_line not implemented");
//            }
//            else {
//                System.out.println("arg2 type");
//                List<TNode> nodes = IAST.getTNodes(arg1.getType());
//                for(TNode node : nodes) {
//                    result.add(IAST.getFollows(node));
//                }
//            }
//        }
//        else {
//            boolean solutionFound = false;
//            if(arg1.getType().equals("integer")) {
//                System.out.println("arg1 int w");
//                TNode node = IAST.getTNode(Integer.parseInt(arg1.getName()));
//                solutionFound = IAST.getFollows(node) != null;
//            }
//            else if(arg2.getType().equals("integer")) {
//                System.out.println("arg2 int w");
//                TNode node = IAST.getTNode(Integer.parseInt(arg2.getName()));
//                solutionFound = IAST.getFollowedBy(node) != null;
//            }
//            else {
//                System.out.println("type w");
//                List<TNode> nodes = IAST.getTNodes(arg2.getType());
//                for(TNode node : nodes) {
//                    solutionFound = IAST.getFollowedBy(node) != null;
//                    if(solutionFound) {
//                        break;
//                    }
//                }
//            }
//            if(solutionFound) {
//                System.out.println("w true");
//                result = new HashSet<>(IAST.getTNodes(queryTree.getSynonyms().get(queryTree.getReturnValues().get(1))));
//            }
//        }

        return result;
    }
    private Set<TNode> followsAstra(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();

        return result;
    }
    private Set<TNode> parent(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> parentAstra(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> modifies(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> modifiesAstra(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> uses(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> usesAstra(Argument arg1, Argument arg2) {
        Set<TNode> result = new HashSet<>();
        return result;
    }
    private Set<TNode> executor(Argument arg1, Argument arg2, Function<TNode,TNode> forward, Function<TNode,TNode> backward) {
        List<TNode> tNodes = findTNodeUsedAsParameterInASTMethod(decideWhatToReturn(arg1,arg2));
        //Potrzebuje zwracać jkąś zmienną po której zdecyduje czy użyć forward, backward czy funkcji typu boolean(jak tak teraz myśle to jak dodamy jeszcze że
        // zamiast selecta może być Boolean to tych funkcji zrobi się za duzo na przekazywanie ich jako argumętu, trzeba wymyślić coś innego
        for(TNode tNode : tNodes) {
            // code responsible for calling AST method
        }
        return new HashSet<>();
    }
    private Argument decideWhatToReturn(Argument arg1, Argument arg2) {
        if(returnValues.contains(arg1)) {
            return arg1;
        }
        else if(returnValues.contains(arg2)) {
            return arg2;
        }
        else {
            return returnValues.get(0);
        }
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
}
