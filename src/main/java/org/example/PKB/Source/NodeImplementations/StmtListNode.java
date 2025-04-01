package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StmtListNode extends ASTNode {
    private List<StmtNode> stmtChildren;

    public StmtListNode() {
        super(EntityType.STMTLIST);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.STMTLIST);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child node");
        }
        if (!(child instanceof StmtNode))
            throw new ASTBuildException("Next child of WhileNode must be a StmtNode!");
        StmtNode stmtChild = (StmtNode)child;
        stmtChildren.add(stmtChild);
        stmtChild.setId(stmtChildren.size() - 1);
        stmtChild.setParent(this);
        return stmtChild.getId();
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<ASTNode>(stmtChildren);
    }

    @Override
    public ASTNode getChild(int num) {
        if(num >= 0 && num < stmtChildren.size())
            return stmtChildren.get(num);
        return null;
    }

    @Override
    public int getChildCount() {
        return stmtChildren.size();
    }
}
