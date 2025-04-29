package org.example.PKB.API;

import org.example.Exceptions.RelationException;

import java.util.List;

public interface ICalls {
    void setCalls(TNode node, TNode calls) throws RelationException;
    TNode getCalls(TNode node)  throws RelationException;
    List<TNode> getCalledBy(TNode node) throws RelationException;
    boolean isCalls(TNode node, TNode calls);
}
