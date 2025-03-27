package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;

public abstract class ASTFactory extends ASTEssential {
    public TNode createTNode(EntityType et)
    {
        switch (et)
        {
            default -> {
            return null;
            }
        }
    }
}
