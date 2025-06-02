package org.example;

import org.example.Frontend.Extractor.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.PKB.API.TNode;
import org.example.QueryProc.Evaluator;
import org.example.QueryProc.PreProc;
import org.example.QueryProc.ResultProjector;
import org.example.QueryProc.model.Argument;
import org.example.QueryProc.model.QueryTree;

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
//            parser.getTokens(tokenizer.getTokensFromFilename("SimpleCode1.txt"));

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
              //queryTree = preProc.parseQuery("stmt s; variable v; Select v such that Modifies(s,v)");

//            queryTree = preProc.parseQuery("stmt s; variable v;\n" +
//                    "Select v such that Modifies(s,v)"); // 121
//            QueryTree queryTree2 = preProc.parseQuery("stmt s;\n" +
//                    "Select s such that Modifies(s,\"cs4\")"); // 121
            //QueryTree queryTree2 = preProc.parseQuery("stmt s; while w; variable v; Select s such that Parent(w,s) such that Modifies(s,v)");
           // queryTree = preProc.parseQuery("stmt s; variable v; Select s such that Modifies(s,v)");

            //queryTree = preProc.parseQuery("stmt s; Select s such that Uses (s, \"b\")");

//        } catch (InvalidQueryException e) {
//            System.out.println(e.getMessage());
//        }
            //Set<Map<Argument, TNode>> result = evaluator.evaluateQuery(queryTree);
 //           Set<Map<Argument, TNode>> result2 = evaluator.evaluateQuery(queryTree2);
//            System.out.println(result.size());
//            System.out.println(resultProjector.convertToString(result, queryTree.getReturnValues()));
           // System.out.println(resultProjector.toPipeTesterFormat(result, queryTree.getReturnValues()));

            //QueryTree queryTree2 = preProc.parseQuery("stmt s; variable v; Select s such that Modifies(s,\"area\""); // 6, 11, 12, 14, 15, 16, 29, 32
            //QueryTree queryTree2 = preProc.parseQuery("variable v; Select v such that Modifies (\"Init\", v)"); //x1, x2, y1, y2, left, right, top, bottom, incre, decrement
            //QueryTree queryTree2 = preProc.parseQuery("while w; Select w such that Uses(w, \"tmp\")"); //6, 12, 16, 26, 29, 47, 59, 69, 79, 89, 95, 101, 103, 105, 136, 143, 180, 181
            QueryTree queryTree2 = preProc.parseQuery("variable v; Select v such that Uses (5, v)");
            Set<Map<Argument, TNode>> result2 = evaluator.evaluateQuery(queryTree2);
            System.out.println(resultProjector.toPipeTesterFormat(result2, queryTree2.getReturnValues()));
            System.out.println("incre, top, bottom, decrement");
 ///           System.out.println(resultProjector.toPipeTesterFormat(result2, queryTree.getReturnValues()));

//            Set<Map<Argument, TNode>> result2 = evaluator.evaluateQuery(queryTree2);
////            System.out.println(result.size());
////            System.out.println(resultProjector.convertToString(result, queryTree.getReturnValues()));
//            System.out.println(resultProjector.toPipeTesterFormat(result, queryTree2.getReturnValues()));
//        }catch (Exception e){
//
//        }
    }
}