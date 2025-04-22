package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.List;
import java.util.Set;

public class ConstantNode extends RefNode{
    public ConstantNode() {
        super(EntityType.CONSTANT);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.CONSTANT);
        return set;
    }
}
