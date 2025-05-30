package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.Attr;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.HashSet;
import java.util.Set;

public abstract class ASTGetters extends ASTFactory {
    public TNode getRoot()
    {
        return root;
    }

    public EntityType getType(TNode node)
    {
        if(node == null) {
            System.out.println(node);
        }
//
        ASTNode astNode = (ASTNode) node;
        return astNode.getEntityType();
    }

    public Attr getAttr(TNode node)
    {
        ASTNode astNode = (ASTNode) node;
        return astNode.getAttr();
    }

    public Set<TNode> getNodesOfEntityTypes(EntityType et)
    {
        Set<TNode> entitySet = entities.get(et);
        return entitySet == null ? new HashSet<>() : entitySet;
//        return entities.get(et);
    }
}
