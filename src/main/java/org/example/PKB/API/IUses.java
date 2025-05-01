package org.example.PKB.API;

import java.util.List;

public interface IUses{
    void setUses(TNode procedureTNode,List<TNode> tNodeList,String variableName);
    void setUses(TNode procedureTNode,List<TNode> tNodeList,TNode variableTNode);
    public void setUses(TNode node, String value);
    List<TNode> getUses(TNode node);
    List<TNode> getUsedBy(TNode var);
    boolean isUsing(TNode node, TNode var);
}
