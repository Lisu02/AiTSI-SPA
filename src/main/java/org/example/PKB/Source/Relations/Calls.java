package org.example.PKB.Source.Relations;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.ICalls;
import org.example.PKB.API.TNode;

import java.util.List;
import java.util.Map;

public class Calls implements ICalls {

    private Map<TNode, TNode> calls;
    private Map<TNode, List<TNode>> calledBy;


    @Override
    public void setCalls(TNode node, TNode calls) throws RelationException {

    }

    @Override
    public TNode getCalls(TNode node) throws RelationException {
        return null;
    }

    @Override
    public List<TNode> getCalledBy(TNode node) throws RelationException {
        return List.of();
    }

    @Override
    public boolean isCalls(TNode node, TNode calls) {
        return false;
    }
}
