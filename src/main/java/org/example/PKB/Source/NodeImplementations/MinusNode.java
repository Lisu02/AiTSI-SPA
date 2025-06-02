package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MinusNode extends ExprNode {
    private ExprNode leftOperand;
    private ExprNode rightOperand;

    public MinusNode() {
        super(EntityType.MINUS);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.MINUS);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child to MinusNode");
        }
        if (!(child instanceof ExprNode)) {
            throw new ASTBuildException("MinusNode can only have ExprNodes as children");
        }

        if (leftOperand == null) {
            leftOperand = (ExprNode) child;
            child.setParent(this);
            child.setId(0);
            return 0;
        } else if (rightOperand == null) {
            rightOperand = (ExprNode) child;
            child.setParent(this);
            child.setId(1);
            return 1;
        } else {
            throw new ASTBuildException("MinusNode can only have two operands");
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
        return  Arrays.asList(leftOperand, rightOperand);
    }

    @Override
    public ASTNode getChild(int num) {
        switch (num) {
            case 0: return leftOperand;
            case 1: return rightOperand;
            default: return null;
        }
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
        return "MinusNode{" +
                "left=" + leftOperand +
                ", right=" + rightOperand +
                '}';
    }
}