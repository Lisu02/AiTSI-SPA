package org.example.PKB.API;

import java.util.Set;

public enum EntityType {
    PROGRAM,
    PROCEDURE,
    STMTLIST,
    STMT,
    ASSIGN,
    CALL,
    WHILE,
    IF,
    EXPR,
    PLUS,
    MINUS,
    TIMES,
    REF,
    VARIABLE,
    CONSTANT,
    INTEGER,
    STRING,
    BLANK,
    PROG_LINE;
    private static final Set<EntityType> STMT_TYPES = Set.of(EntityType.IF, EntityType.ASSIGN, EntityType.WHILE);
    public boolean allows(EntityType type) {
        return this == type || (this == EntityType.STMT && STMT_TYPES.contains(type));
    }
}
