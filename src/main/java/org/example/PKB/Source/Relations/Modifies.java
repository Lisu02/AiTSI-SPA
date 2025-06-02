package org.example.PKB.Source.Relations;

import org.example.PKB.API.IModifies;
import org.example.PKB.API.TNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modifies implements IModifies {

    private Map<TNode, List<TNode>> modifiesMap;
    private Map<TNode, List<TNode>> isModifiedMap;

    public Modifies()
    {
        modifiesMap = new HashMap<>();
        isModifiedMap = new HashMap<>();
    }

    @Override
    public void addModifies(TNode procedureNode, TNode stmtNode, TNode variableNode) {
        
    }

    @Override
    public void addModifies(TNode procedureNode, List<TNode> tNodeList, TNode variableNode) {

    }

    @Override
    public List<TNode> getModifies(TNode node) {
        return Collections.emptyList();
    }

    @Override
    public List<TNode> getModifiesBy(TNode var) {
        return Collections.emptyList();
    }

    @Override
    public boolean isModifying(TNode node, TNode var) {
        return false;
    }
}
