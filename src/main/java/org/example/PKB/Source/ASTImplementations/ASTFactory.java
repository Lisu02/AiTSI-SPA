package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;
import org.example.PKB.Source.NodeImplementations.*;

import java.util.HashSet;
import java.util.Set;


public abstract class ASTFactory extends ASTEssential {
    public TNode createTNode(EntityType et)
    {
        ASTNode node;
        switch (et)
        {
            case STMTLIST ->{
                node = new StmtListNode();
            }
            case WHILE -> {
                node = new WhileNode();
            }
            case VARIABLE,CONSTANT -> {
                node = new VariableNode();
            }
            case ASSIGN -> {
                node = new AssignNode();
            }
            case PROCEDURE -> {
                node = new ProcedureNode();
            }
            case PROGRAM -> {
                node = new ProgramNode();
            }
            case PLUS -> {
                node = new PlusNode();
            }
            case TIMES -> {
                node = new TimesNode();
            }
            case MINUS -> {
                node = new MinusNode();
            }
            case CONSTANT -> {
                node = new ConstantNode();
            }
            case IF -> {
                node = new IfNode();
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
                entities.put(entityType, new HashSet<>());
            }
            entities.get(entityType).add(node);
        }

        return node;
    }
}
