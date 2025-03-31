package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AssignNode  extends StmtNode{
    private VariableNode variableNode;
    private ExprNode exprNode;
    public AssignNode() {
        super(EntityType.ASSIGN);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.ASSIGN);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if(variableNode == null)
        {
            if(!(child instanceof VariableNode))
                throw new ASTBuildException("Variable child must be an instance of VariableNode");
            variableNode = (VariableNode) child;
            variableNode.setId(0);
            variableNode.setParent(this);
            return 0;
        }
        if(exprNode == null)
        {
            if(!(child instanceof ExprNode))
            {
                throw new ASTBuildException("ExprNode child must be an instance of ExprNode");
            }
            exprNode = (ExprNode) child;
            exprNode.setId(1);
            exprNode.setParent(this);
            return 1;
        }
        return -1;
    }

    @Override
    public List<ASTNode> getChildren() {
        if (variableNode == null) {
            return List.of();
        }
        List<ASTNode> children = new ArrayList<>();
        children.add(variableNode);
        children.add(exprNode);
        return children;
    }

    @Override
    public ASTNode getChild(int num) {
        if(num == 0)
            return variableNode;
        if(num == 1)
            return exprNode;
        return null;
    }

    @Override
    public int getChildCount() {
        if(variableNode != null)
        {
            if(exprNode != null)
                return 2;
            return 1;
        }
        return 0;
    }
}
