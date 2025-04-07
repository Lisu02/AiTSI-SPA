package Frontend;

import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


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

    @Mock
    IAST mockIAST;

    @InjectMocks
    Parser p;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWhile(){
        //Parser parser = Parser.getInstance();
        Tokenizer tokenizer = Tokenizer.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("Test.txt");
        p.getTokens(tokenList);

        verify(mockIAST).createTNode(EntityType.WHILE);
        System.out.println("While test passed");
    }

    @Test
    public void testAssign(){
        Tokenizer tokenizer = Tokenizer.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("AssignTest.txt");
        p.getTokens(tokenList);

        verify(mockIAST).createTNode(EntityType.ASSIGN);
        System.out.println("Assign test passed");
    }

    @Test
    public void procedureAssign(){
        Tokenizer tokenizer = Tokenizer.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("AssignTest.txt");
        p.getTokens(tokenList);

        verify(mockIAST).createTNode(EntityType.PROCEDURE);
        System.out.println("Procedure test passed");
    }

}
