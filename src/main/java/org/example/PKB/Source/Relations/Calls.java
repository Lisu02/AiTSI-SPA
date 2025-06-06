package org.example.PKB.Source.Relations;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.ICalls;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.NodeImplementations.ProcedureNode;

import java.util.*;

public class Calls implements ICalls {

    private static Calls instance = new Calls();

    private Map<TNode, Set<TNode>> calls;
    private Map<TNode, Set<TNode>> calledBy;
    private Map<TNode, Set<TNode>> callsAstra;
    private Map<TNode, Set<TNode>> calledByAstra;

    private Calls() {
        calls = new LinkedHashMap<>();
        calledBy = new LinkedHashMap<>();
        calledByAstra = new LinkedHashMap<>();
        callsAstra = new LinkedHashMap<>();
    }

    public static Calls getInstance(){
        return instance;
    }


    @Override
    public void setCalls(TNode node, TNode calls) throws RelationException {
        if(!(node instanceof ProcedureNode) || !(calls instanceof ProcedureNode))
            throw new RelationException("Incorrect nodes for Calls rel  ation");
        Set<TNode> nodes; //todo: null ptr exception kiedy to jest inicjalizowane? -Adrian 27.05.2025
        if(!this.calls.containsKey(node))
        {
            this.calls.put(node, new HashSet<>());
        }
        nodes = this.calls.get(node);
        if(nodes.contains(calls))return;
        nodes.add(calls);

        if(!this.calledBy.containsKey(calls))
        {
            this.calledBy.put(calls, new HashSet<>());
        }
        nodes = this.calledBy.get(calls);
        nodes.add(node);


    }

    @Override
    public void setCallsAstra(TNode node, TNode calls) throws RelationException {
        if(!(node instanceof ProcedureNode) || !(calls instanceof ProcedureNode))
            throw new RelationException("Incorrect nodes for Calls relation");
        Set<TNode> nodes;
        if(!this.callsAstra.containsKey(node))
        {
            this.callsAstra.put(node, new HashSet<>());
        }
        nodes = this.callsAstra.get(node);
        if(nodes.contains(calls))return;
        nodes.add(calls);

        if(!this.calledByAstra.containsKey(calls))
        {
            this.calledByAstra.put(calls, new HashSet<>());
        }
        nodes = this.calledByAstra.get(calls);
        nodes.add(node);
    }

    @Override
    public List<TNode> getCalls(TNode node) {
        return calls.get(node) == null ? new ArrayList<>() : new ArrayList<>(calls.get(node));
    }

    @Override
    public List<TNode> getCalledBy(TNode node) {
        return calledBy.get(node) == null ? new ArrayList<>() : new ArrayList<>(calledBy.get(node));
    }

    @Override
    public List<TNode> getCallsAstra(TNode node) {
        return callsAstra.get(node) == null ? new ArrayList<>() : new ArrayList<>(callsAstra.get(node));
    }

    @Override
    public List<TNode> getCalledByAstra(TNode node) {
        return calledByAstra.get(node) == null ? new ArrayList<>() : new ArrayList<>(calledByAstra.get(node));
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
