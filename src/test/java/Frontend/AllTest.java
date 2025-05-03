package Frontend;

import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AllTest {

    @Test
    public void test(){

        Tokenizer tokenizer = Tokenizer.getInstance();
        Parser parser = Parser.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("IfText.txt");

        parser.getTokens(tokenList);

        AbstractionExtractor ae = new AbstractionExtractor();
        //ae.generateStarterAbstractions();


    }
}
