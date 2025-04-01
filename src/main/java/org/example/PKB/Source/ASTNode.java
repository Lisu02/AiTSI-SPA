package org.example.PKB.Source;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.Attr;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ASTNode implements TNode {
    private final EntityType entityType;
    private ASTNode parent;
    private int id;
    private Attr attr;

    public ASTNode(EntityType entityType)
    {
        this.entityType = entityType;
        this.id = -1;
    }

    public EntityType getEntityType() {
        return entityType;
    }
    public Set<EntityType> getAllEntityTypes()
    {
        return new HashSet<>();
    }

    public ASTNode getParent() {
        return parent;
    }

    public void setParent(ASTNode parent) {
        if (this.parent != null) return;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != -1) return;
        this.id = id;
    }

    public Attr getAttr()
    {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

    public abstract int setNextChild(ASTNode child) throws ASTBuildException;
    public abstract List<ASTNode> getChildren();
    public abstract ASTNode getChild(int num);
    public abstract int getChildCount();
}
