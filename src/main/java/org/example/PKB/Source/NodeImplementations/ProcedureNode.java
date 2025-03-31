package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.Source.ASTNode;
import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import java.util.ArrayList;
import java.util.List;

public class ProcedureNode extends ASTNode {
    private final List<ASTNode> children;

    public ProcedureNode() {
        super(EntityType.PROCEDURE);
        this.children = new ArrayList<ASTNode>();
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child node");
        }
        children.add(child);
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
}