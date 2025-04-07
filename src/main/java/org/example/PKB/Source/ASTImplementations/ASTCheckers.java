package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

public abstract class ASTCheckers extends ASTFollows {

    // Check if n2 follows n1
    public boolean isFollowed(TNode n1, TNode n2)
    {
        ASTNode astNode1 = (ASTNode) n1;
        ASTNode astNode2 = (ASTNode) n2;
        if(astNode1.getParent()!=null && astNode2.getParent()!=null)
        {
            if(astNode1.getParent()==astNode2.getParent())
            {

                if(astNode1.getId()==astNode2.getId()-1)

                {
                    return true;
                }

            }
        }
        return false;
    }
    // Check if n2 follows* n1
    public boolean isFollowedAstra(TNode n1, TNode n2)
    {
        ASTNode astNode1 = (ASTNode) n1;
        ASTNode astNode2 = (ASTNode) n2;
        if(astNode1.getParent()!=null && astNode2.getParent()!=null)
        {
            if(astNode1.getParent()==astNode2.getParent())
            {
                if(astNode1.getId()>astNode2.getId())
                {
                    return true;
                }
            }
        }
        return false;
    }
    // Check if n1 is parent n2
    public boolean isParent(TNode n1, TNode n2)
    {
        ASTNode astNode1 = (ASTNode) n1;
        ASTNode astNode2 = (ASTNode) n2;
        ASTNode parent = astNode2.getParent();

        if(parent!=null)
        {
            if(checkIfEntityIsWhileOrIf(astNode1))
            {
                if(parent.getEntityType() == EntityType.STMTLIST)
                {
                    parent = parent.getParent();
                    if(parent == null)
                        return false;
                }

                if(astNode1==parent)
                {
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //check if n1 is parent* n2
    public boolean isParentAstra(TNode n1, TNode n2)
    {
        ASTNode astNode1 = (ASTNode) n1;
        ASTNode astNode2 = (ASTNode) n2;

        ASTNode parent = astNode2.getParent();
        if(checkIfEntityIsWhileOrIf(astNode1))
        {
            while (parent != null) {
                if (checkIfEntityIsWhileOrIf(parent)) {
                    if (parent == astNode1) {
                        return true;
                    }
                }
                parent = parent.getParent();
            }
        }


        return false;
    }


}
