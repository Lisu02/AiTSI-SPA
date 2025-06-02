package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ProcedureNode extends ASTNode {
    private StmtListNode children;

    public ProcedureNode() {
        super(EntityType.PROCEDURE);
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child node");
        }
        if(children != null)
            throw new ASTBuildException("Cannot add more children in ProcedureNode!");
        if (!(child instanceof StmtListNode))
            throw new ASTBuildException("A child of a ProcedureNode must be a StmtListNode!");
        children = (StmtListNode) child;
        children.setId(0);
        children.setParent(this);
        return 0;
    }

    @Override
    public List<ASTNode> getChildren() {
        return Collections.singletonList(children);
    }

    @Override
    public ASTNode getChild(int num) {
        return num == 0 ? children : null;
    }

    @Override
    public int getChildCount() {
        return children == null ? 0 : 1;
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.PROCEDURE);
        return set;
    }
}