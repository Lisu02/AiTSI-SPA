package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.List;
import java.util.Set;

public class VariableNode extends RefNode{
    public VariableNode() {
        super(EntityType.VARIABLE);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.VARIABLE);
        return set;
    }
}
