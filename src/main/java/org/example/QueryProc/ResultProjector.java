package org.example.QueryProc;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.model.Argument;

import java.util.*;

public class ResultProjector {
    private final IAST AST = PKB.getAST();
    public String toPipeTesterFormat(Set<Map<Argument,TNode>> tNodes, List<Argument> returnValues) {
//        System.out.println(tNodes);

        if(tNodes.isEmpty()) {
            return "none";
        }
        for(Argument key : returnValues) {
            if(tNodes.iterator().next().get(key) == null) {
                return "none";
            }
        }
        Set<String> resultSet = new HashSet<>();
        String result = "";

        Set<EntityType> stmtTypes = EnumSet.of(
                EntityType.STMT, EntityType.ASSIGN,
                EntityType.IF, EntityType.WHILE, EntityType.CALL
        );

        for (Map<Argument, TNode> row : tNodes) {
            String line = "";

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

                line += line + attribute + " ";
            }

            resultSet.add(line);
        }

        for (String line : resultSet) {
            if (line.length() > 0) {
                line = line.substring(0, line.length() - 1); // usuń ostatni znak
                result += line + ", ";
            }
        }

        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }

        return result;
    }
    public List<String> convertToString(Set<Map<Argument,TNode>> tNodes, List<Argument> returnValues) {
        List<String> result = new ArrayList<>();
        for(Map<Argument, TNode> row : tNodes) {
            String line = "";
            for(Argument key : returnValues) {
                String attribute = "";
                TNode tNode = row.get(key);
                Set<EntityType> stmtTypes = new LinkedHashSet<>(Arrays.asList(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL));
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
