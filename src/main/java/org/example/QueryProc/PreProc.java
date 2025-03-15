package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;

public class PreProc {
    public QueryTree parseQuery(String query) throws InvalidQueryException {
        if(!validate(query)) {
            throw new InvalidQueryException("Invalid query: " + query);
        }
        return crateQueryTree(query);
    }
    private boolean validate(String query) {
        return false;
    }
    private QueryTree crateQueryTree(String query) {
        return new QueryTree();
    }
}
