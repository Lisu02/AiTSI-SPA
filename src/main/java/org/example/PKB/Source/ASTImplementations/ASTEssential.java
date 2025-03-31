package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.Source.ASTNode;

import java.util.List;
import java.util.Map;

public abstract class ASTEssential implements IAST {

    protected ASTNode root;
    protected Map<EntityType, List<ASTNode>> entities;

}
