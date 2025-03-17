package org.example.QueryProc;

import java.util.List;
import java.util.Map;

public class QueryTree {
    private Map<String,String> synonyms;
    private String[] returnValues;
    private List<String[]> suchThatStatements;
    private List<String[]> withStatements;

    public QueryTree(Map<String, String> synonyms, String[] returnValues, List<String[]> suchThatStatements, List<String[]> withStatements) {
        this.synonyms = synonyms;
        this.returnValues = returnValues;
        this.suchThatStatements = suchThatStatements;
        this.withStatements = withStatements;
    }
}
