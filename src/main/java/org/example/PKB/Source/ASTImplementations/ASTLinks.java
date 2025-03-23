package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.LinkType;
import org.example.PKB.API.TNode;

public abstract class ASTLinks extends ASTSetters {
    public TNode getFirstChild(TNode p)
    {
        return null; // To be implemented...
    }
    public TNode getLinkedNode(LinkType link, TNode node1)
    {
        return null; // To be implemented...
    }
    public boolean isLinked(LinkType link, TNode node1, TNode node2)
    {
        return false; // To be implemented...
    }
}
