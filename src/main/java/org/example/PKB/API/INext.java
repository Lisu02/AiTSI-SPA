package org.example.PKB.API;

import org.example.Exceptions.RelationException;

import java.util.List;

public interface INext {
    void setNext(TNode node, TNode next) throws RelationException;
    void setNextAstra(TNode node, TNode next) throws RelationException;
    List<TNode> getNext(TNode node);
    List<TNode> getPrevious(TNode node);
    List<TNode> getNextAstra(TNode node);
    List<TNode> getPreviousAstra(TNode node);
    boolean isNext(TNode node, TNode next);
    boolean isNextAstra(TNode node, TNode next);
}
