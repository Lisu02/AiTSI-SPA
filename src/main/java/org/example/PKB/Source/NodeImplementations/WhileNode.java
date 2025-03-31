package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WhileNode extends StmtNode{
    private VariableNode variableChild;
    private List<StmtNode> stmtChildren;

    public WhileNode() {
        super(EntityType.WHILE);
        stmtChildren = new ArrayList<StmtNode>();
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.WHILE);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if(variableChild == null)
        {
            if(!(child instanceof VariableNode))
                throw new ASTBuildException("First child of WhileNode must be a VariableNode!");
            variableChild = (VariableNode)child;
            variableChild.setId(0);
            variableChild.setParent(this);
            return 0;
        }

        if (!(child instanceof StmtNode))
            throw new ASTBuildException("Next child of WhileNode must be a StmtNode!");
        StmtNode stmtChild = (StmtNode)child;
        stmtChildren.add(stmtChild);
        stmtChild.setId(stmtChildren.size());
        stmtChild.setParent(this);
        return stmtChild.getId();

    }

    @Override
    public List<ASTNode> getChildren() {
        if (variableChild == null) {
            return List.of();
        }

        List<ASTNode> children = new ArrayList<>();
        children.add(variableChild);
        children.addAll(stmtChildren);
        return children;
    }


    @Override
    public ASTNode getChild(int num) {
        if(num == 0)return variableChild;
        if (num <= stmtChildren.size())return stmtChildren.get(num-1);
        return null;
    }

    @Override
    public int getChildCount() {
        return variableChild == null ? 0 : stmtChildren.size() + 1;
    }
}
