package org.example.PKB.Old;


import java.util.*;


public class ASTNode {
    //Chyba nic nie pominalem ale sprawdzcie Damian i Lewy
    enum EntityType { PROGRAM, PROCEDURE, STMTLIST, STMT, ASSIGN, CALL, WHILE, IF, PLUS, MINUS, TIMES, VARIABLE, CONSTANT }

    private final EntityType type;
    private final String value;
    private final List<ASTNode> children = new ArrayList<>();
    private ASTNode rightSibling;

    public ASTNode(EntityType type, String value) {
        this.type = type;
        this.value = value;
    }

    public void addChild(ASTNode child) {
        this.children.add(child);
    }

    public void setRightSibling(ASTNode sibling) {
        this.rightSibling = sibling;
    }

    public EntityType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public ASTNode getRightSibling() {
        return rightSibling;
    }
}

