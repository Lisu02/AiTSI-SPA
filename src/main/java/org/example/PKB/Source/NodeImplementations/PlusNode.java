package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class PlusNode extends ExprNode {
    private final List<ExprNode> operands;

    public PlusNode() {
        super(EntityType.PLUS);
        this.operands = new ArrayList<ExprNode>(2);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.PLUS);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child to PlusNode");
        }
        if (!(child instanceof ExprNode)) {
            throw new ASTBuildException("PlusNode can only have ExprNodes as children");
        }
        operands.add((ExprNode) child);
        child.setParent(this);
        return operands.size() - 1;
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(operands);
    }

    @Override
    public ASTNode getChild(int num) {
        if (num < 0 || num >= operands.size()) {
            return null;
        }
        return operands.get(num);
    }

    @Override
    public int getChildCount() {
        return operands.size();
    }

    public ASTNode getLeftOperand() {
        return operands.size() > 0 ? operands.get(0) : null;
    }

    public ASTNode getRightOperand() {
        return operands.size() > 1 ? operands.get(1) : null;
    }

    public boolean isBinaryOperation() {
        return operands.size() == 2;
    }

    @Override
    public String toString() {
        return "PlusNode{" + operands + "}";
    }
}