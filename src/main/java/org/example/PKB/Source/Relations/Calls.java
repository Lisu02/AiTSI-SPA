package org.example.PKB.Source.Relations;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.ICalls;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.NodeImplementations.CallNode;
import org.example.PKB.Source.NodeImplementations.ProcedureNode;

import java.util.*;

public class Calls implements ICalls {

    private Map<TNode, Set<TNode>> calls;
    private Map<TNode, Set<TNode>> calledBy;

    private Map<TNode, Set<TNode>> callsAstra;
    private Map<TNode, Set<TNode>> calledByAstra;

    public Calls() {
        calls = new HashMap<>();
        calledBy = new HashMap<>();
        calledByAstra = new HashMap<>();
        callsAstra = new HashMap<>();
    }


    @Override
    public void setCalls(TNode node, TNode calls) throws RelationException {
        if(!(node instanceof CallNode) || !(calls instanceof ProcedureNode))
            throw new RelationException("Incorrect nodes for Calls relation");
        Set<TNode> nodes;
        if(!this.calls.containsKey(node))
        {
            nodes = this.calls.put(node, new HashSet<>());
        }
        else
        {
            nodes = this.calls.get(node);
        }
        if(nodes.contains(calls))throw new RelationException("Relation already exists");
        nodes.add(calls);

        if(!this.calledBy.containsKey(calls))
        {
            nodes = this.calledBy.put(calls, new HashSet<>());
        }
        else
        {
            nodes = this.calledBy.get(calls);
        }
        nodes.add(node);


    }

    @Override
    public void setCallsAstra(TNode node, TNode calls) throws RelationException {
        if(!(node instanceof CallNode) || !(calls instanceof ProcedureNode))
            throw new RelationException("Incorrect nodes for Calls relation");
        Set<TNode> nodes;
        if(!this.callsAstra.containsKey(node))
        {
            nodes = this.callsAstra.put(node, new HashSet<>());
        }
        else
        {
            nodes = this.callsAstra.get(node);
        }
        if(nodes.contains(calls))throw new RelationException("Relation already exists");
        nodes.add(calls);

        if(!this.calledByAstra.containsKey(calls))
        {
            nodes = this.calledByAstra.put(calls, new HashSet<>());
        }
        else
        {
            nodes = this.calledByAstra.get(calls);
        }
        nodes.add(node);
    }

    @Override
    public List<TNode> getCalls(TNode node) {
        return calls.get(node).stream().toList();
    }

    @Override
    public List<TNode> getCalledBy(TNode node) {
        return calledBy.get(node).stream().toList();
    }

    @Override
    public List<TNode> getCallsAstra(TNode node) {
        return callsAstra.get(node).stream().toList();
    }

    @Override
    public List<TNode> getCalledByAstra(TNode node) {
        return calledByAstra.get(node).stream().toList();
    }

    @Override
    public boolean isCalls(TNode node, TNode calls) {
        Set<TNode> nodes = this.calls.get(node);
        return nodes != null && nodes.contains(calls);
    }

    @Override
    public boolean isCallsAstra(TNode node, TNode calls) {
        Set<TNode> nodes = this.callsAstra.get(node);
        return nodes != null && nodes.contains(calls);
    }
}
