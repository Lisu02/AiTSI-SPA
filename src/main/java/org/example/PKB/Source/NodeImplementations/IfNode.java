package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class IfNode extends StmtNode{
    private VariableNode variableChild;
    private StmtListNode stmtChildren;
    private StmtListNode stmtElseChildren;

    public IfNode() {
        super(EntityType.IF);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.IF);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child node");
        }
        if(variableChild == null)
        {
            if(!(child instanceof VariableNode))
                throw new ASTBuildException("First child of IFNode must be a VariableNode!");
            variableChild = (VariableNode)child;
            variableChild.setId(0);
            variableChild.setParent(this);
            return 0;
        }
        if(stmtChildren == null) {
            if (!(child instanceof StmtListNode))
                throw new ASTBuildException("Next child of IFNode must be a StmtListNode!");
            stmtChildren = (StmtListNode) child;
            stmtChildren.setId(1);
            stmtChildren.setParent(this);
            return 1;
        }
        if(stmtElseChildren == null) {
            if (!(child instanceof StmtListNode))
                throw new ASTBuildException("Next child of IFNode must be a StmtListNode!");
            stmtElseChildren = (StmtListNode) child;
            stmtElseChildren.setId(2);
            stmtElseChildren.setParent(this);
            return 2;
        }
        throw new ASTBuildException("No more children can be assigned to a IFNode!");
    }

    @Override
    public List<ASTNode> getChildren() {
        if (variableChild == null) {
            return Collections.emptyList();
        }

        List<ASTNode> children = new ArrayList<>();
        children.add(variableChild);
        children.add(stmtChildren);
        children.add(stmtElseChildren);
        return children;
    }


    @Override
    public ASTNode getChild(int num) {
        if(num == 0)return variableChild;
        if(num == 1)return stmtChildren;
        if(num == 2)return stmtElseChildren;
        return null;
    }

    @Override
    public int getChildCount() {
        if(variableChild == null)return 0;
        if(stmtChildren == null)return 1;
        if(stmtElseChildren == null)return 2;
        return 3;
    }
}
