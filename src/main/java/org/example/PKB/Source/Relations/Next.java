package org.example.PKB.Source.Relations;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.INext;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.NodeImplementations.StmtNode;

import java.util.*;

public class Next implements INext {

    private Map<TNode, Set<TNode>> next;
    private Map<TNode, Set<TNode>> prev;

    private Map<TNode, Set<TNode>> nextAstra;
    private Map<TNode, Set<TNode>> prevAstra;

    private Map<TNode, TNode> unitedNodes;
    private Map<TNode, Set<TNode>> cycles;

    public Next() {
        next = new HashMap<TNode, Set<TNode>>();
        prev = new HashMap<TNode, Set<TNode>>();
        nextAstra = new HashMap<TNode, Set<TNode>>();
        prevAstra = new HashMap<TNode, Set<TNode>>();
    }

    @Override
    public void setUnitedNodes(Map<TNode, TNode> unitedNodes) {
        this.unitedNodes = unitedNodes;
    }

    @Override
    public void setCycles(Map<TNode, Set<TNode>> cycles) {
        this.cycles = cycles;
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
        if (node == null || !next.containsKey(node) || next.get(node) == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(next.get(node));
    }

    @Override
    public List<TNode> getPrevious(TNode node) {
        if (node == null || !prev.containsKey(node) || prev.get(node) == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(prev.get(node));
    }

    @Override
    public List<TNode> getNextAstra(TNode node) {
        List<TNode> resuts = new ArrayList<>();
        if (node == null || !nextAstra.containsKey(unitedNodes.get(node)) || nextAstra.get(unitedNodes.get(node)) == null) {
            return resuts;
        }

        for (TNode n : nextAstra.get(unitedNodes.get(node))) {
            resuts.addAll(cycles.get(n));
        }
        return resuts;
    }

    @Override
    public List<TNode> getPreviousAstra(TNode node) {
        List<TNode> resuts = new ArrayList<>();
        if (node == null || !prevAstra.containsKey(unitedNodes.get(node)) || prevAstra.get(unitedNodes.get(node)) == null) {
            return resuts;
        }
        TNode prev = unitedNodes.get(node);
        for (TNode n : prevAstra.get(unitedNodes.get(node))) {
            TNode LOW = unitedNodes.get(n);
            Set<TNode> cy = cycles.get(n);
            resuts.addAll(cycles.get(n));
        }

        return resuts;
    }

    @Override
    public boolean isNext(TNode node, TNode next) {
        Set<TNode> nodes = this.next.get(node);
        return nodes != null && nodes.contains(next);
    }

    @Override
    public boolean isNextAstra(TNode node, TNode next) {
        Set<TNode> nodes = this.nextAstra.get(unitedNodes.get(node));
//        return nodes != null && nodes.contains(unitedNodes.get(next));
        if(nodes == null) return false;
        for (TNode n : nodes) {
            if(cycles.get(n).contains(next))return true;
        }
        return false;
    }
}
