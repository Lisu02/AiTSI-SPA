package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.Exceptions.SolutionDoesNotExist;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.model.Argument;
import org.example.QueryProc.model.QueryTree;

import java.util.*;

public class ResultProjector {
    private final IAST AST = PKB.getAST();
    public void exePqlQueryFromPipeTester(String pqlQuery1, String pqlQuery2) {
        String pqlQuery = pqlQuery1+pqlQuery2;
        QueryTree queryTree = null;
        PreProc preProc = new PreProc();
        Evaluator evaluator = new Evaluator();
        try {
            queryTree = preProc.parseQuery(pqlQuery);
            evaluator.evaluateQueryPipeTester(queryTree);
        } catch (InvalidQueryException e) {
            System.err.println("#" + e.getMessage());
        }catch (SolutionDoesNotExist ex)
        {
            if(queryTree.isBoolean())
                System.out.println("false");
            else
             System.err.println("#" + ex.getMessage());
        }
    }
//    public List<String> convertToString(Set<TNode> tNodes) {
//        List<String> result = new ArrayList<>();
//        for(TNode tNode : tNodes) {
//
//            String attribute = "";
//            Set<EntityType> stmtTypes = Set.of(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL);
//            if(stmtTypes.contains(AST.getType(tNode))) {
//                attribute = AST.getAttr(tNode).getLine() + "";
//            }
//            else if(AST.getType(tNode) == EntityType.PROCEDURE) {
//                attribute = AST.getAttr(tNode).getProcName();
//            }
//            else if(AST.getType(tNode) == EntityType.VARIABLE) {
//                attribute = AST.getAttr(tNode).getVarName();
//            }
//
//           result.add(AST.getType(tNode) + " " + attribute);
//        }
//        return result;
//    }
    public String toPipeTesterFormat(Set<Map<Argument,TNode>> tNodes, List<Argument> returnValues) {
        if(tNodes.isEmpty()) {
            return "none";
        }
        StringBuilder result = new StringBuilder();

        Set<EntityType> stmtTypes = EnumSet.of(
                EntityType.STMT, EntityType.ASSIGN,
                EntityType.IF, EntityType.WHILE, EntityType.CALL
        );

        for (Map<Argument, TNode> row : tNodes) {
            StringBuilder line = new StringBuilder();

            for (Argument key : returnValues) {
                TNode tNode = row.get(key);
                EntityType type = AST.getType(tNode);
                String attribute;

                if (stmtTypes.contains(type)) {
                    attribute = String.valueOf(AST.getAttr(tNode).getLine());
                } else if (type == EntityType.PROCEDURE) {
                    attribute = AST.getAttr(tNode).getProcName();
                } else if (type == EntityType.VARIABLE) {
                    attribute = AST.getAttr(tNode).getVarName();
                } else {
                    attribute = "";
                }

                line.append(attribute).append(" ");
            }

            if (line.length() > 0) {
                line.setLength(line.length() - 1);
                result.append(line).append(", ");
            }
        }

        if (result.length() >= 2) {
            result.setLength(result.length() - 2);
        }

        return result.toString();
    }
    public List<String> convertToString(Set<Map<Argument,TNode>> tNodes, List<Argument> returnValues) {
        List<String> result = new ArrayList<>();
        for(Map<Argument, TNode> row : tNodes) {
            String line = "";
            for(Argument key : returnValues) {
                String attribute = "";
                TNode tNode = row.get(key);
                Set<EntityType> stmtTypes = new LinkedHashSet<>(Arrays.asList(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL));
                System.out.println(tNode);
                if(stmtTypes.contains(AST.getType(tNode))) {
                    attribute = AST.getAttr(tNode).getLine() + "";
                }
                else if(AST.getType(tNode) == EntityType.PROCEDURE) {
                    attribute = AST.getAttr(tNode).getProcName();
                }
                else if(AST.getType(tNode) == EntityType.VARIABLE) {
                    attribute = AST.getAttr(tNode).getVarName();
                }
                line += AST.getType(tNode) + " " + attribute + " ";

            }
            result.add(line.trim());
        }
        return result;
    }
}
