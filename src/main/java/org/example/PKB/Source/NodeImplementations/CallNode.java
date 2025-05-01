package org.example.PKB.Source.NodeImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.List;

public class CallNode extends ASTNode {

    ProcedureNode procedure;

    public CallNode() {
        super(EntityType.CALL);
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
        return List.of(procedure);
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
