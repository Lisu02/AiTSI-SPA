package org.example.QueryProc;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ResultProjector {
    public List<String> convertToString(Set<TNode> tNodes) {
        List<String> result = new ArrayList<>();
        for(TNode tNode : tNodes) {
            ASTNode node = (ASTNode) tNode;

            String attribute = "";
            Set<EntityType> stmtTypes = Set.of(EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.CALL);
            if(stmtTypes.contains(node.getEntityType())) {
                attribute = node.getAttr().getLine() + "";
            }
            else if(node.getEntityType() == EntityType.PROCEDURE) {
                attribute = node.getAttr().getProcName();
            }
            else if(node.getEntityType() == EntityType.VARIABLE) {
                attribute = node.getAttr().getVarName();
            }

           result.add(node.getEntityType() + " " + attribute);
        }
        return result;
    }
}
