package org.example.QueryProc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
public class QueryTree {
    private Set<Argument> synonyms;
    private boolean isBoolean;
    private List<Argument> returnValues;
    private List<Relation> relations;
    private List<WithStatement> withStatements;
}


