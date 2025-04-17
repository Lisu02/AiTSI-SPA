package org.example.QueryProc;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.QueryProc.model.Argument;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultProjector {
    private final IAST AST = PKB.getAST();
    public List<String> convertToString(Set<TNode> tNodes) {
        List<String> result = new ArrayList<>();
        for(TNode tNode : tNodes) {

            String attribute = "";
            Set<EntityType> stmtTypes = Set.of(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL);
            if(stmtTypes.contains(AST.getType(tNode))) {
                attribute = AST.getAttr(tNode).getLine() + "";
            }
            else if(AST.getType(tNode) == EntityType.PROCEDURE) {
                attribute = AST.getAttr(tNode).getProcName();
            }
            else if(AST.getType(tNode) == EntityType.VARIABLE) {
                attribute = AST.getAttr(tNode).getVarName();
            }

           result.add(AST.getType(tNode) + " " + attribute);
        }
        return result;
    }
    public List<String> convertToString2(Set<Map<Argument,TNode>> tNodes) {
        List<String> result = new ArrayList<>();
        for(Map<Argument, TNode> row : tNodes) {
            String line = "";
            for(Argument key : row.keySet()) {
                String attribute = "";
                TNode tNode = row.get(key);
                Set<EntityType> stmtTypes = Set.of(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL);
                if(stmtTypes.contains(AST.getType(tNode))) {
                    attribute = AST.getAttr(tNode).getLine() + "";
                }
                else if(AST.getType(tNode) == EntityType.PROCEDURE) {
                    attribute = AST.getAttr(tNode).getProcName();
                }
                else if(AST.getType(tNode) == EntityType.VARIABLE) {
                    attribute = AST.getAttr(tNode).getVarName();
                }
                line += AST.getType(tNode) + " " + attribute;

            }
            result.add(line);
        }
        return result;
    }
}
