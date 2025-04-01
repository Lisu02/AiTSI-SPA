package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.Attr;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.List;

public abstract class ASTGetters extends ASTFactory {
    public TNode getRoot()
    {
        return root;
    }

    public EntityType getType(TNode node)
    {
        ASTNode astNode = (ASTNode) node;
        return astNode.getEntityType();
    }

    public Attr getAttr(TNode node)
    {
        ASTNode astNode = (ASTNode) node;
        return astNode.getAttr();
    }

    public List<TNode> getNodesOfEntityTypes(EntityType et)
    {
        return entities.get(et);
    }

}
