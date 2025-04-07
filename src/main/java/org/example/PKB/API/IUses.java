package org.example.PKB.API;

import java.util.List;

public interface IUses{
    public void setUses(TNode node, String value);
    List<TNode> getUses(TNode node);
    List<TNode> getUsedBy(TNode var);
    boolean isUsing(TNode node, TNode var);
}
