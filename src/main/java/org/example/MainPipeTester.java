package org.example;

import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;

import java.util.List;
import java.util.logging.LogManager;

public class MainPipeTester {
    public static void main(String[] args) throws Exception {

        //Aby uruchomić prawidłowo MainPipeTester trzeba dodać program arguments
        //do edit configuration i tam podać ścieżke do pliku z kodem do parsowania
        //np ParserTest.txt

        //Usuwamy loggera do działającego programu aby nie dawał na stdout komunikatów
        LogManager.getLogManager().readConfiguration(
                Main.class.getClassLoader().getResourceAsStream("logging_pipetester.properties")
        );

        String fileName = args[0];

        Parser parser = Parser.getInstance();
        Tokenizer tokenizer = Tokenizer.getInstance();
        AbstractionExtractor ae = new AbstractionExtractor();


        List<String> tokenList = tokenizer.getTokensFromFilename(fileName);

        parser.getTokens(tokenList);

        ae.generateStarterAbstractions();

        System.out.println("READY"); //informacja do Pipetester'a że program jest gotowy do przyjęcia poleceń PQL



    }
}
