package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.TNode;

import java.util.List;

public abstract class ASTParents extends ASTLinks {
    public TNode getParent(TNode c)
    {
        return null; // To be implemented...
    }
    public List<TNode> getParentedBy(TNode p)
    {
        return null; // To be implemented...
    }

    public TNode getParentAstra(TNode c)
    {
        return null; // To be implemented...
    }
    public List<TNode> getParentedAstraBy(TNode p)
    {
        return null; // To be implemented...
    }
}
