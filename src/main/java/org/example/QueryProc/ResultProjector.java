package org.example.QueryProc;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;

import java.util.ArrayList;
import java.util.List;
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
}
