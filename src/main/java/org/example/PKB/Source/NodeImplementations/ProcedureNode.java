package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.Source.ASTNode;
import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcedureNode extends ASTNode {
    private final List<StmtNode> children;

    public ProcedureNode() {
        super(EntityType.PROCEDURE);
        this.children = new ArrayList<StmtNode>();
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child node");
        }
        if (!(child instanceof StmtNode))
            throw new ASTBuildException("Next child of ProgramNode must be a StmtNode!");
        children.add((StmtNode) child);
        child.setParent(this);
        return children.size() - 1;
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(children);  //
    }

    @Override
    public ASTNode getChild(int num) {
        if (num < 0 || num >= children.size()) {
            return null;
        }
        return children.get(num);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.PROCEDURE);
        return set;
    }
}