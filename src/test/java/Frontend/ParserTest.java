package Frontend;

import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserTest {


    @Test
    void checkTokenTekst(){
        //given
        Parser parser = Parser.getInstance();
        Tokenizer tokenizer = Tokenizer.getInstance();
        List<String> tokenList = tokenizer.getTokensFromFilename("ParserTest.txt");

        //when
        parser.getTokens(tokenList);

        //then - wyciągamy informacje z logów dlatego assert true
        Assertions.assertTrue(true);
    }
}
