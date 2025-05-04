package org.example.PKB.Source.Relations;

import org.example.PKB.API.IUses;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.NodeImplementations.ProcedureNode;
import org.example.PKB.Source.NodeImplementations.StmtNode;
import org.example.PKB.Source.NodeImplementations.VariableNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Uses implements IUses {

    private Map<TNode, Set<TNode>> uses;
    private Map<TNode, Set<TNode>> usedBy;

    @Override
    public void setUses(TNode procedureTNode, List<TNode> tNodeList, String variableName) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setUses(TNode procedureTNode, List<TNode> tNodeList, TNode variableTNode) {
        if(!(procedureTNode instanceof ProcedureNode)) throw new RuntimeException("Invalid procedure node");
        if(!(variableTNode instanceof VariableNode)) throw new RuntimeException("Invalid variable node");
        for(TNode tNode : tNodeList) {
            if(!(tNode instanceof StmtNode)) throw new RuntimeException("Invalid stmt node");
        }
        Set<TNode> used = uses.get(procedureTNode);
        if(used == null) {
            used = uses.put(procedureTNode, new HashSet<>());
        }
        used.add(variableTNode);

        Set<TNode> usedVar = usedBy.get(variableTNode);
        if(usedVar == null) {
            usedVar = usedBy.put(variableTNode, new HashSet<>());
        }
        usedVar.add(procedureTNode);

        for(TNode tNode : tNodeList) {
            used = uses.get(tNode);
            if(used == null) {
                used = uses.put(tNode, new HashSet<>());
            }
            used.add(variableTNode);
            usedVar.add(tNode);
        }

    }

    @Override
    public void setUses(TNode node, String value) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public List<TNode> getUses(TNode node) {
        return uses.get(node).stream().toList();
    }

    @Override
    public List<TNode> getUsedBy(TNode var) {
        return usedBy.get(var).stream().toList();
    }

    @Override
    public boolean isUsing(TNode node, TNode var) {
        return uses.containsKey(node) && uses.get(node).contains(var);
    }
}
