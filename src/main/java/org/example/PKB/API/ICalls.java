package org.example.PKB.API;

import org.example.Exceptions.RelationException;

import java.util.List;

public interface ICalls {
    void setCalls(TNode node, TNode calls) throws RelationException;
    void setCallsAstra(TNode node, TNode calls) throws RelationException;
    List<TNode> getCalls(TNode node);
    List<TNode> getCalledBy(TNode node);
    List<TNode> getCallsAstra(TNode node);
    List<TNode> getCalledByAstra(TNode node);
    boolean isCalls(TNode node, TNode calls);
    boolean isCallsAstra(TNode node, TNode calls);
}
