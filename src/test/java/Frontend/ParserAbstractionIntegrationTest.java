package Frontend;

import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;

public class ParserAbstractionIntegrationTest {

    @Test
    void jarzabekWczytanieTest(){
        Tokenizer tokenizer = Tokenizer.getInstance();
        Parser parser = Parser.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("jarzabek.txt");

        System.out.println(tokenList);

        parser.getTokens(tokenList);

        System.out.println("Koniec");
        Logger log = Logger.getLogger(this.getClass().getName());


    }
}
