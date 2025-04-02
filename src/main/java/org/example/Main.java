package org.example;

import org.example.Exceptions.InvalidQueryException;
import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.PKB.API.TNode;
import org.example.QueryProc.Evaluator;
import org.example.QueryProc.PreProc;
import org.example.QueryProc.ResultProjector;
import org.example.QueryProc.model.QueryTree;

import java.io.IOException;
import java.util.Set;
import java.util.logging.LogManager;

public class Main {

    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));

        System.out.println("MAIN EMPTY");
        Tokenizer tokenizer = Tokenizer.getInstance();

        Parser parser = Parser.getInstance();

        parser.getTokens(tokenizer.getTokensFromFilename("ParserTest.txt"));

        AbstractionExtractor ae = new AbstractionExtractor();

        ae.generateStarterAbstractions();

        QueryTree queryTree = null;
        PreProc preProc = new PreProc();
        Evaluator evaluator = new Evaluator();
        ResultProjector resultProjector = new ResultProjector();
        try {
//            queryTree = preProc.parseQuery("stmt s1,s2; assign a; Select s1, s2 such that Modifies (s1, 2) such that Calls (s1, \"Second\") with a.procName= 19");
//            queryTree = preProc.parseQuery("stmt s1,s2,s3; procedure p; Select p, s3 such that Follows (s1,s2) such that Calls (p, \"Second\") with p.procName = \"Third\"");
//            queryTree = preProc.parseQuery("Boolean such that Follows (5,6)");
            queryTree = preProc.parseQuery("stmt s,s2; Select s such that Parent*(s,s2)");
        } catch (InvalidQueryException e) {
            System.out.println(e.getMessage());
        }
        Set<TNode> result = evaluator.evaluateQuery(queryTree);
        resultProjector.print(result);

        // Bierzemy instancje tokenizera oraz parsera
        // bierzemy tokeny z pliku od użytkownika
        // przekazujemy tokeny do parsera
        // parser wywołuje buduje drzewo za pomoca interfejsu PKB
    }
}