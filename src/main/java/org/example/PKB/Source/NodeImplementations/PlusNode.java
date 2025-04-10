package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class PlusNode extends ExprNode {
    private ExprNode leftOperand;
    private ExprNode rightOperand;

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
        if (child == null) {
            throw new ASTBuildException("Can't add null child to PlusNode");
        }
        if (!(child instanceof ExprNode)) {
            throw new ASTBuildException("PlusNode can only have ExprNodes as children");
        }

        if (leftOperand == null) {
            leftOperand = (ExprNode) child;
            child.setParent(this);
            return 0;
        } else if (rightOperand == null) {
            rightOperand = (ExprNode) child;
            child.setParent(this);
            return 1;
        } else {
            throw new ASTBuildException("PlusNode can only have two operands");
        }
    }

    @Override
    public List<ASTNode> getChildren() {
        if (leftOperand == null) {
            return Collections.emptyList();
        }
        if (rightOperand == null) {
            return Collections.singletonList(leftOperand);
        }
        return List.of(leftOperand, rightOperand);
    }

    @Override
    public ASTNode getChild(int num) {
        return switch (num) {
            case 0 -> leftOperand;
            case 1 -> rightOperand;
            default -> null;
        };
    }

    @Override
    public int getChildCount() {
        return (leftOperand != null ? 1 : 0) + (rightOperand != null ? 1 : 0);
    }

    private ExprNode getLeftOperand() {
        return leftOperand;
    }

    private ExprNode getRightOperand() {
        return rightOperand;
    }


    @Override
    public String toString() {
        return "PlusNode{" +
                "left=" + leftOperand +
                ", right=" + rightOperand +
                '}';
    }
}