package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.LinkType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;
// chyba trzeba zmienic hierarchie
public abstract class ASTLinks extends ASTSetters {
    public TNode getFirstChild(TNode p)
    {
        ASTNode astNode = (ASTNode) p;
        if(astNode.getChildCount()>0)
        {
            return astNode.getChild(0);
        }
        return null;
    }
    public TNode getLinkedNode(LinkType link, TNode node1)
    {
        ASTNode astNode1 = (ASTNode) node1;
        switch (link) {
            case FirstChild:
                return getFirstChild(astNode1);
            case Follows:
                return getFollows(astNode1);

            case RightSibling:
                return getFollows(astNode1);
            case Parent:
                return getParent(astNode1);
            default:
                return null;
        }
    }
    public boolean isLinked(LinkType link, TNode node1, TNode node2)
    {
        ASTNode astNode1 = (ASTNode) node1;
        ASTNode astNode2 = (ASTNode) node2;
        switch (link) {
            case FirstChild:
                return true;
            case Follows:
                return  true;

            case RightSibling:
                return true;
            case Parent:
                return true;
            default:
                return false;
        }
    }
}
