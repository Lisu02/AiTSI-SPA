package org.example.PKB.Source;

import org.example.PKB.API.Attr;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;

import java.util.List;

public abstract class ASTNode implements TNode {
    private final EntityType entityType;
    private final ASTNode parent;
    private final int id;

    public ASTNode(EntityType entityType, ASTNode parent, int id)
    {
        this.entityType = entityType;
        this.parent = parent;
        this.id = id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public ASTNode getParent() {
        return parent;
    }

    public int getId() {
        return id;
    }

    public abstract Attr getAttr();
    public abstract int setNextChild(ASTNode child);
    public abstract List<ASTNode> getChildren();
    public abstract ASTNode getChild(int num);

}
