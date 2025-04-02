package org.example.QueryProc;

import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.Set;

public class ResultProjector {
    public void print(Set<TNode> tNodes) {
        for(TNode tNode : tNodes) {
            ASTNode node = (ASTNode) tNode;
            System.out.println(node.getEntityType() + " ");
        }
    }
}
