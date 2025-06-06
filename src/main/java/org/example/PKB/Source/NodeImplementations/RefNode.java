package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class RefNode extends ExprNode {
    public RefNode(EntityType entityType) {
        super(entityType);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.REF);
        return set;
    }

    public int setNextChild(ASTNode child) throws ASTBuildException
    {
        throw new ASTBuildException("RefNode cannot have any children!");
    }
    public List<ASTNode> getChildren()
    {
        return new ArrayList<ASTNode>();
    }
    public ASTNode getChild(int num)
    {
        return null;
    }
    public int getChildCount()
    {
        return 0;
    }
}
