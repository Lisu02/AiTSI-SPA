package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ASTParents extends ASTSetters {
    public TNode getParent(TNode c)
    {
        ASTNode astNode = (ASTNode)c;
        ASTNode parent = astNode.getParent();
        if(parent != null)
        {
            if(parent.getEntityType() == EntityType.STMTLIST)parent = parent.getParent();
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

//        switch(astNode.getEntityType())
//        {
//            case WHILE -> {
//                List<TNode> children = new ArrayList<>();
//                for(ASTNode child : astNode.getChildren())
//                {
//                    switch (child.getEntityType())
//                    {
//                        case STMTLIST -> {
//                            children.addAll(child.getChildren());
//                        }
//                        default -> {
//                            children.add(child);
//                        }
//                    }
//                }
//                return children;
//            }
//            default -> {
//                return Collections.emptyList();
//            }
//        }

        if(astNode.getEntityType() == EntityType.WHILE || astNode.getEntityType() == EntityType.IF) {
            List<TNode> children = new ArrayList<>();
            for(ASTNode child : astNode.getChildren())
            {
                if(child.getEntityType() == EntityType.STMTLIST) {
                    children.addAll(child.getChildren());
                }
                else {
                    children.add(child);
                }
            }
            return children;
        }
        else {
            return Collections.emptyList();
        }

//        // Stare, jak dziala nodw to do usuniecia
//        if(checkIfEntityIsWhileOrIf(astNode))
//        {
//            return astNode.getChildren().stream().map(child -> (TNode)child).collect(Collectors.toList());
//        }
//        return Collections.emptyList();
    }

    public List<TNode> getParentAstra(TNode c)
    {
        ASTNode astNode1 = (ASTNode) c;
        List<TNode> parents = new ArrayList<>();

        ASTNode currentNode = astNode1;

        while(currentNode != null)
        {
            ASTNode parent = currentNode.getParent();
            if(parent == null)break;
//            switch (parent.getEntityType())
//            {
//                case STMTLIST -> {
//                    currentNode = parent;
//                }
//                case WHILE -> {
//                    parents.add(parent);
//                    currentNode = parent;
//                }
//                default -> {
//                    currentNode = null;
//                }
//            }
            EntityType entityType = parent.getEntityType();
            if(entityType == EntityType.STMTLIST) {
                currentNode = parent;
            }
            else if(entityType == EntityType.WHILE) {
                parents.add(parent);
                currentNode = parent;
            }
            else if(entityType == EntityType.IF) {
                parents.add(parent);
                currentNode = parent;
            }
            else {
                currentNode = null;
            }
        }
        return parents;

//        // Stare, do wyrzucenia jak nowe dobre
//        ASTNode parent1 = astNode1.getParent();
//        if(parent1 != null)
//        {
//            ASTNode parent2 = parent1.getParent();
//            if(checkIfEntityIsWhileOrIf(parent1))
//            {
//                //to jest pod liste
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
//            }
//        }
//    return parents;
    }

    public List<TNode> getParentedAstraBy(TNode p)
    {
        List<TNode> nodesParentedBy = new ArrayList<>();
        ASTNode astNode1 = (ASTNode) p;

        if(astNode1 != null)
        {
            if(checkIfEntityIsWhileOrIf(astNode1))
            {
                addChildrenOfParent(astNode1, nodesParentedBy);
            }
        }

        return nodesParentedBy;
    }
    private void addChildrenOfParent(ASTNode parent, List<TNode> nodesParentedBy) {
        if(parent == null)return;
        for (ASTNode child : parent.getChildren()) {
//            switch (child.getEntityType())
//            {
//                case STMTLIST -> {
//                    addChildrenOfParent(child, nodesParentedBy);
//                }
//                default -> {
//                    if (checkIfEntityIsWhileOrIf(child)) {
//                        nodesParentedBy.add(child);
//                        addChildrenOfParent(child, nodesParentedBy);
//                    }
//                }
//            }
            if(child.getEntityType() == EntityType.STMTLIST) {
                addChildrenOfParent(child, nodesParentedBy);
            }
            else if(checkIfEntityIsWhileOrIf(child)){
                nodesParentedBy.add(child);
                addChildrenOfParent(child, nodesParentedBy);
            }

//            // Stare, jak nowe dziala to usunac
//            nodesParentedBy.add(child);
//
//            if (checkIfEntityIsWhileOrIf(child)) {
//                addChildrenOfParent(child, nodesParentedBy);
//            }
        }
    }
    protected boolean checkIfEntityIsWhileOrIf(ASTNode n1)
    {
        if(n1 == null)
        {
            return false;
        }
        if(n1.getEntityType()== EntityType.WHILE || n1.getEntityType()==EntityType.IF)
        {
            return true;
        }
        return false;
    }
}
