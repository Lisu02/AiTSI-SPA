package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;

public class QueryController {
    private static final PreProc preProc = new PreProc();
    private static final Evaluator evaluator = new Evaluator();
    public static void main(String[] args) {
        QueryTree queryTree = null;
        try {
            queryTree = preProc.parseQuery("stmt s1; Select s1 such that Follows (s1, 2)");
        } catch (InvalidQueryException e) {
            System.out.println(e.getMessage());
        }
        String result = evaluator.evaluateQuery(queryTree);
        System.out.println(result);
    }

}
