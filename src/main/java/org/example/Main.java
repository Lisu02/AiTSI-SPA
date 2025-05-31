package org.example;

import org.example.Exceptions.InvalidQueryException;
import org.example.Frontend.Extractor.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.PKB.API.TNode;
import org.example.QueryProc.Evaluator;
import org.example.QueryProc.PreProc;
import org.example.QueryProc.ResultProjector;
import org.example.QueryProc.model.Argument;
import org.example.QueryProc.model.QueryTree;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogManager;

public class Main {

    public static void main(String[] args) throws Exception {
        //try {
            LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));

            System.out.println("MAIN EMPTY");
            Tokenizer tokenizer = Tokenizer.getInstance();

            Parser parser = Parser.getInstance();

            //parser.getTokens(tokenizer.getTokensFromFilename("pipe-tester/SimpleCode1PK.txt"));

            parser.getTokens(tokenizer.getTokensFromFilename("jarzabek.txt"));

            AbstractionExtractor ae = new AbstractionExtractor();

            ae.removeFakeProcedures();
            ae.generateStarterAbstractions();

            // Bierzemy instancje tokenizera oraz parsera
            // bierzemy tokeny z pliku od użytkownika
            // przekazujemy tokeny do parsera
            // parser wywołuje buduje drzewo za pomoca interfejsu PKB


            QueryTree queryTree = null;
            PreProc preProc = new PreProc();

            Evaluator evaluator = new Evaluator();
            ResultProjector resultProjector = new ResultProjector();
//        try {
//            queryTree = preProc.parseQuery("stmt s1,s2; assign a; Select s1, s2 such that Modifies (s1, 2) such that Calls (s1, \"Second\") with a.procName= 19");
//            queryTree = preProc.parseQuery("stmt s1,s2,s3; procedure p; Select p, s3 such that Follows (s1,s2) such that Calls (p, \"Second\") with p.procName = \"Third\"");
//            queryTree = preProc.parseQuery("Boolean such that Follows (5,6)");
//            queryTree = preProc.parseQuery("stmt p, ch; Select p, ch such that Parent* (p, ch)");
////            queryTree = preProc.parseQuery("Select BOOLEAN such that Follows (10, _)");
//            queryTree = preProc.parseQuery("stmt s; Select s such that Modifies (s, \"a\")");
            //queryTree = preProc.parseQuery("variable v; Select v such that Modifies (6, v)");
            //queryTree = preProc.parseQuery("if i;stmt s; Select i,s such that Follows (1, 2)");
            //queryTree = preProc.parseQuery("stmt s1, s2; Select s1 such that Parent* (14, s1) and Follows(s2,s1) with s2.stmt# = 15");
            //queryTree = preProc.parseQuery("stmt s; Select s such that Modifies (s, \"k\")");
            //queryTree = preProc.parseQuery("stmt s; Select s such that Modifies (s, \"k\")");
            //queryTree = preProc.parseQuery("stmt s; Select s such that Follows*(1, s)"); // 2, 3, 4, 5, 6, 119
            //queryTree = preProc.parseQuery("stmt s; Select s such that Follows(120, s)"); // 121
            //queryTree = preProc.parseQuery("procedure p1,p2; Select p1 such that Calls(p1,p2)"); // WYWALA SIE PKB ICALLS null ptr
              queryTree = preProc.parseQuery("stmt s; variable v; Select v such that Modifies(s,v)");

            //queryTree = preProc.parseQuery("stmt s; Select s such that Uses (s, \"b\")");

//        } catch (InvalidQueryException e) {
//            System.out.println(e.getMessage());
//        }
            Set<Map<Argument, TNode>> result = evaluator.evaluateQuery(queryTree);
            System.out.println(resultProjector.convertToString(result, queryTree.getReturnValues()));
//        }catch (Exception e){
//
//        }
    }
}