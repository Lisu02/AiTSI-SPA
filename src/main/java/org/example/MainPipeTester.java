package org.example;

import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.QueryProc.ResultProjector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

        System.out.println("READY");
        ResultProjector resultProjector = new ResultProjector();
        while(true) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            //System.out.println("Podaj pierwszą linię zapytania PQL (np. 'stmt s;'):");
            String pqlQuery1 = userInput.readLine();  // Pierwsza linia zapytania (deklaracja)
            if(pqlQuery1.equals("1")) { //wpisz 1 w pierwszej linijce aby zabic program
                break;
            }

           // System.out.println("Podaj drugą linię zapytania PQL (np. 'Select s such that Modifies(s, \"x\")'):");
            String pqlQuery2 = userInput.readLine();
            if (pqlQuery2 == null || pqlQuery2.trim().isEmpty()) {
                System.err.println("#Query missing SELECT part");
                continue;
            }
            resultProjector.exePqlQueryFromPipeTester(pqlQuery1,pqlQuery2);
        }




    }
}
