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
            case STMTLIST: {
                node = new StmtListNode();
                break;
            }
            case WHILE: {
                node = new WhileNode();
                break;
            }
            case VARIABLE: {
                node = new VariableNode();
                break;
            }
            case ASSIGN: {
                node = new AssignNode();
                break;
            }
            case PROCEDURE: {
                node = new ProcedureNode();
                break;
            }
            case PROGRAM: {
                node = new ProgramNode();
                break;
            }
            case PLUS: {
                node = new PlusNode();
                break;
            }
            case TIMES: {
                node = new TimesNode();
                break;
            }
            case MINUS: {
                node = new MinusNode();
                break;
            }
            case CONSTANT: {
                node = new ConstantNode();
                break;
            }
            case IF: {
                node = new IfNode();
                break;
            }
            case CALL: {
                node = new CallNode();
                break;
            }
            default: {
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
