package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.List;
import java.util.Set;

public abstract class ExprNode extends ASTNode {
    public ExprNode(EntityType entityType) {
        super(entityType);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.EXPR);
        return set;
    }


}
