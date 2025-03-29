package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.QueryProc.model.QueryTree;

public class QueryController {
    private static final PreProc preProc = new PreProc();
    private static final Evaluator evaluator = new Evaluator();
    public static void main(String[] args) {
        QueryTree queryTree = null;
        try {
//            queryTree = preProc.parseQuery("stmt s1,s2; assign a; Select s1, s2 such that Modifies (s1, 2) such that Calls (s1, \"Second\") with a.procName= 19");
            queryTree = preProc.parseQuery("stmt s1,s2,s3; procedure p; Select p, s3 such that Follows (s1,s2) such that Calls (p, \"Second\") with p.procName = \"7\"");
            //queryTree = preProc.parseQuery("Boolean such that Follows 5,6)");
        } catch (InvalidQueryException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(queryTree.withStatements());
        evaluator.evaluateQuery(queryTree);
        //System.out.println(queryTree);
    }

}
