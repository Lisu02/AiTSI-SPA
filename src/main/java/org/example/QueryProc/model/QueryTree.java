package org.example.QueryProc.model;

import java.util.List;
import java.util.Set;

public record QueryTree(
        Set<Argument> synonyms,
        boolean isBoolean,
        List<Argument> returnValues,
        List<Relation> relations,
        List<WithStatement> withStatements
) {}
