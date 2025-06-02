package org.example.PKB.API;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
    private static final Set<EntityType> STMT_TYPES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(EntityType.IF, EntityType.ASSIGN, EntityType.WHILE, EntityType.CALL, EntityType.PROG_LINE))
    );
    public boolean allows(EntityType type) {
        return this == type || (this == EntityType.STMT && STMT_TYPES.contains(type));
    }

    public boolean allows(Set<EntityType> types) {
        return types.contains(this);
    }
}
