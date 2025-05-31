package org.example.PKB.Source.Relations;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.INext;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.NodeImplementations.StmtNode;

import java.util.*;

public class Next implements INext {

    private Map<TNode, Set<TNode>> next;
    private Map<TNode, Set<TNode>> prev;

    private Map<TNode, Set<TNode>> nextAstra;
    private Map<TNode, Set<TNode>> prevAstra;

    public Next() {
        next = new HashMap<TNode, Set<TNode>>();
        prev = new HashMap<TNode, Set<TNode>>();
        nextAstra = new HashMap<TNode, Set<TNode>>();
        prevAstra = new HashMap<TNode, Set<TNode>>();
    }

    @Override
    public void setNext(TNode node, TNode next) throws RelationException {
        if(!(node instanceof StmtNode) || !(next instanceof StmtNode))
            throw new RelationException("Incorrect nodes for Calls relation");
        Set<TNode> nodes;
        if(!this.next.containsKey(node))
        {
            this.next.put(node, new HashSet<>());
        }
        nodes = this.next.get(node);
        if(nodes.contains(next))return;
        nodes.add(next);

        if(!this.prev.containsKey(next))
        {
            this.prev.put(next, new HashSet<>());
        }
        nodes = this.prev.get(next);
        nodes.add(node);
    }

    @Override
    public void setNextAstra(TNode node, TNode next) throws RelationException {
        if(!(node instanceof StmtNode) || !(next instanceof StmtNode))
            throw new RelationException("Incorrect nodes for Calls relation");
        Set<TNode> nodes;
        if(!this.nextAstra.containsKey(node))
        {
            this.nextAstra.put(node, new HashSet<>());
        }
        nodes = this.nextAstra.get(node);
        if(nodes.contains(next))return;
        nodes.add(next);

        if(!this.prevAstra.containsKey(next))
        {
            this.prevAstra.put(next, new HashSet<>());
        }
        nodes = this.prevAstra.get(next);
        nodes.add(node);
    }

    @Override
    public List<TNode> getNext(TNode node) {
        return new ArrayList<>(next.get(node));
    }

    @Override
    public List<TNode> getPrevious(TNode node) {
        return new ArrayList<>(prev.get(node));
    }

    @Override
    public List<TNode> getNextAstra(TNode node) {
        return new ArrayList<>(nextAstra.get(node));
    }

    @Override
    public List<TNode> getPreviousAstra(TNode node) {
        return new ArrayList<>(prevAstra.get(node));
    }

    @Override
    public boolean isNext(TNode node, TNode next) {
        Set<TNode> nodes = this.next.get(node);
        return nodes != null && nodes.contains(next);
    }

    @Override
    public boolean isNextAstra(TNode node, TNode next) {
        Set<TNode> nodes = this.nextAstra.get(node);
        return nodes != null && nodes.contains(next);
    }
}
