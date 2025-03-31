package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;
import org.example.PKB.Source.NodeImplementations.AssignNode;
import org.example.PKB.Source.NodeImplementations.VariableNode;
import org.example.PKB.Source.NodeImplementations.WhileNode;

import java.util.ArrayList;
import java.util.Set;

public abstract class ASTFactory extends ASTEssential {
    public TNode createTNode(EntityType et)
    {
        ASTNode node;
        switch (et)
        {
            case WHILE -> {
                node = new WhileNode();
            }
            case VARIABLE -> {
                node = new VariableNode();
            }
            case ASSIGN -> {
                node = new AssignNode();
            }
            default -> {
            return null;
            }
        }

        Set<EntityType> nodeEntities = node.getAllEntityTypes();
        for (EntityType entityType : nodeEntities)
        {
            if(!entities.containsKey(entityType))
            {
                entities.put(entityType, new ArrayList<>());
            }
            entities.get(entityType).add(node);
        }

        return node;
    }
}
