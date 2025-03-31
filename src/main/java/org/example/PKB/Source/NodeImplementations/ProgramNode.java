package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.Source.ASTNode;
import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.EntityType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgramNode extends ASTNode {
    private final List<ProcedureNode> children;

    public ProgramNode() {
        super(EntityType.PROGRAM);
        this.children = new ArrayList<ProcedureNode>();
    }

    @Override
    public int setNextChild(ASTNode child) throws ASTBuildException {
        if (child == null) {
            throw new ASTBuildException("Can't add null child node");
        }
        if (!(child instanceof ProcedureNode))
            throw new ASTBuildException("Next child of ProgramNode must be a ProcedureNode!");
        children.add((ProcedureNode) child);
        child.setParent(this);
        return children.size() - 1;
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(children);
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


    public void addProcedure(ASTNode procedure) throws ASTBuildException {
        if (procedure == null || procedure.getEntityType() != EntityType.PROCEDURE) {
            throw new ASTBuildException("Only procedure nodes can be added as children");
        }
        setNextChild(procedure);
    }

    public List<ASTNode> getProcedures() {
        List<ASTNode> procedures = new ArrayList<>();
        for (ASTNode child : children) {
            if (child.getEntityType() == EntityType.PROCEDURE) {
                procedures.add(child);
            }
        }
        return procedures;
    }

   @Override
   public Set<EntityType> getAllEntityTypes() {
       Set<EntityType> set = super.getAllEntityTypes();
       set.add(EntityType.PROGRAM);
       return set;
   }

}