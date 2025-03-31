package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.List;
import java.util.Set;

public class PlusNode extends ExprNode {
    public PlusNode() {
        super(EntityType.PLUS);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.PLUS);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        return 0;
    }

    @Override
    public List<ASTNode> getChildren() {
        return List.of();
    }

    @Override
    public ASTNode getChild(int num) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }
}
