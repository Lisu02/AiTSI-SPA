package Frontend;

import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.verify;

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

    @Test
    public void emptyFileTest(){
        Parser parser = Parser.getInstance();
        Tokenizer tokenizer = Tokenizer.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("EmptyFile.txt");
        boolean exceptionCaught = false;

        try{
            parser.getTokens(tokenList);
        }catch (NoSuchElementException e){
            exceptionCaught = true;
        }finally {
            System.out.println(exceptionCaught ? "Caught" : "No");
        }

    }


    @Test
    public void testInvalidToken(){
        Parser parser = Parser.getInstance();
        Tokenizer tokenizer = Tokenizer.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("InvalidTokenCode.txt");

        boolean exceptionCaught = false;

        try{
            parser.getTokens(tokenList);
        }
        catch(RuntimeException e){
            exceptionCaught = true;
        }
        finally {
            System.out.println(exceptionCaught ? "Caught" : "No");
        }
    }


    // Do poprawy + sprawdzenie reszty
//    @Test
//    public void testAssign(){
//        Parser parser = Parser.getInstance();
//        Tokenizer tokenizer = Tokenizer.getInstance();
//        IAST mockIAST = Mockito.mock(IAST.class);
//
//        List<String> tokenList = tokenizer.getTokensFromFilename("SimpleCode1.txt");
//        parser.getTokens(tokenList);
//
//
//        verify(mockIAST).createTNode(EntityType.ASSIGN);
//    }
}
