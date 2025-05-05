package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CallNode extends StmtNode {

    ProcedureNode procedure;

    public CallNode() {
        super(EntityType.CALL);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.CALL);
        return set;
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (procedure != null) throw new ASTBuildException("Child already set");
        if(!(child instanceof ProcedureNode))throw new ASTBuildException("Call child must be a Procedure");
        procedure = (ProcedureNode) child;
        procedure.setId(0);
        procedure.setParent(this);
        return 0;
    }

    @Override
    public List<ASTNode> getChildren() {
        return Collections.singletonList(procedure);
    }

    @Override
    public ASTNode getChild(int num) {
        return num == 0 ? procedure : null;
    }

    @Override
    public int getChildCount() {
        return procedure == null ? 0 : 1;
    }
}
