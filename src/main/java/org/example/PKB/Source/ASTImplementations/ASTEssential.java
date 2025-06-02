package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class ASTEssential implements IAST {

    protected ASTNode root;
    protected Map<EntityType, Set<TNode>> entities = new LinkedHashMap<>();


}
