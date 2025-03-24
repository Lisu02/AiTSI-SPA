package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ASTParents extends ASTLinks {
    public TNode getParent(TNode c)
    {
        ASTNode astNode = (ASTNode)c;
        ASTNode parent = astNode.getParent();
        if(parent != null)
        {
            if(checkIfEntityIsWhileOrIf(parent))
            {
                return parent;
            }
        }
       return null;
    }
    public List<TNode> getParentedBy(TNode p)
    {
        ASTNode astNode = (ASTNode) p;
        if(checkIfEntityIsWhileOrIf(astNode))
        {
            return astNode.getChildren().stream().map(child -> (TNode)child).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    //Do dogadania bo chyba bedzie musialo zwrocic liste, ustalilem z Lukaszem ze pogadamy na zajeciach z Kasprem
    public TNode getParentAstra(TNode c)
    {
        ASTNode astNode1 = (ASTNode) c;
        List<TNode> parents = new ArrayList<>();
        ASTNode parent1 = astNode1.getParent();
        if(parent1 != null)
        {
            ASTNode parent2 = parent1.getParent();
            if(checkIfEntityIsWhileOrIf(parent1))
            {
                //to jest pod liste
//                while (parent2 != null) {
//                    if (checkIfEntityIsWhileOrIf(parent2)) {
//                      parents.add(parent2);
//                    }
//                      else
//                      {
//                           break;
//                      }
//                    parent2 = parent2.getParent();
//                }
                if(parent2!=null)
                {
                    if (checkIfEntityIsWhileOrIf(parent2)) {
                       return parent2;
                    }
                }
            }
        }
    return null;
    }

    public List<TNode> getParentedAstraBy(TNode p)
    {
        List<TNode> nodesParentedBy = new ArrayList<>();
        ASTNode astNode1 = (ASTNode) p;
        ASTNode parent1 = astNode1.getParent();
        if(parent1 != null)
        {
            if(checkIfEntityIsWhileOrIf(parent1))
            {
                ASTNode parent2 = parent1.getParent();
                if(parent2!=null)
                {
                    if(checkIfEntityIsWhileOrIf(parent2))
                    {
                        addChildrenOfParent(parent2, nodesParentedBy);
                    }
                }
            }
        }
        return nodesParentedBy;
    }
    private void addChildrenOfParent(ASTNode parent, List<TNode> nodesParentedBy) {
        for (ASTNode child : parent.getChildren()) {
            nodesParentedBy.add(child);

            if (checkIfEntityIsWhileOrIf(child)) {
                addChildrenOfParent(child, nodesParentedBy);
            }
        }
    }
    protected boolean checkIfEntityIsWhileOrIf(ASTNode n1)
    {
        if(n1.getEntityType()== EntityType.WHILE || n1.getEntityType()==EntityType.IF)
        {
            return true;
        }
        return false;
    }
}
