package org.example.PKB.Source.ASTImplementations;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.Attr;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

public abstract class ASTSetters extends ASTGetters {
    public void setRoot(TNode node)
    {
        root = (ASTNode) node;
    }
    public void setAttr(TNode n, Attr attr)
    {
        ((ASTNode) n).setAttr(attr);

    }

    public void setParentChildLink(TNode p, TNode c) throws ASTBuildException
    {
        ((ASTNode) p).setNextChild((ASTNode) c);
    }
}
