package org.example;

import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.QueryProc.ResultProjector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainPipeTester {
    public static void main(String[] args) throws Exception {

        //Aby uruchomić prawidłowo MainPipeTester trzeba dodać program arguments
        //do edit configuration i tam podać ścieżke do pliku z kodem do parsowania
        //np ParserTest.txt

        //Usuwamy loggera do działającego programu aby nie dawał na stdout komunikatów
        InputStream config = MainPipeTester.class.getClassLoader().getResourceAsStream("logging.properties");
        if (config != null) {
            LogManager.getLogManager().readConfiguration(config);
        } else {
            System.err.println("Nie znaleziono logging.properties w classpath!");
            // Ustawienie poziomu logowania na OFF
            Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            logger.setLevel(Level.OFF);  // Wyłącza logowanie na poziomie globalnym

            // Usuwanie wszystkich handlerów z głównego loggera
            for (java.util.logging.Handler handler : logger.getHandlers()) {
                logger.removeHandler(handler);
            }

            // Jeżeli są inne logery, usuwamy także ich handlery
            Logger exampleLogger = Logger.getLogger("org.example");
            exampleLogger.setLevel(Level.OFF);  // Wyłącza logowanie dla tej klasy
            for (java.util.logging.Handler handler : exampleLogger.getHandlers()) {
                exampleLogger.removeHandler(handler);
            }

            // Jeżeli chcesz całkowicie usunąć handler ConsoleHandler (który może logować na konsolę)
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.OFF);
            logger.addHandler(consoleHandler);
        }


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
            System.out.println("Podaj pierwszą linię zapytania PQL (np. 'stmt s;'):");
            String pqlQuery1 = userInput.readLine();  // Pierwsza linia zapytania (deklaracja)
            if(pqlQuery1.equals("1")) { //wpisz 1 w pierwszej linijce aby zabic program
                break;
            }

            System.out.println("Podaj drugą linię zapytania PQL (np. 'Select s such that Modifies(s, \"x\")'):");
            String pqlQuery2 = userInput.readLine();
            if (pqlQuery2 == null || pqlQuery2.trim().isEmpty()) {
                System.err.println("#Query missing SELECT part");
                continue;
            }
            resultProjector.exePqlQueryFromPipeTester(pqlQuery1,pqlQuery2);
        }




    }
}
