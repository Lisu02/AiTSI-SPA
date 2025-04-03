package org.example.QueryProc.model;

import java.util.List;
public record QueryTree(
        boolean isBoolean,
        List<Argument> returnValues,
        List<Relation> relations,
        List<WithStatement> withStatements
) {}
