package Frontend;

import org.example.Frontend.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    private Tokenizer tokenizer;

    @BeforeEach
    void init(){
        tokenizer = Tokenizer.getInstance();
    }

    @Test
    public void shouldChangeFilenameIfGivenFilenameDoesNotExist() throws FileNotFoundException {
        //given
        String filename = "nonExistingFilename.txt";
        //when
        Scanner scanner = tokenizer.getScanner(filename);
        //then
        assertEquals(scanner.ioException(),new Scanner(new FileReader("SimpleFirst.txt")).ioException());
    }

    @Test
    public void testTokenizeSimpleString()
    {
        System.out.print("Test 1...");
        String code = "x=2+y+3+p+5";

        List<String> slicedWord = tokenizer.sliceWord(code);
        List<String> expectedTokens = List.of(
                "x", "=", "2", "+", "y", "+","3", "+", "p", "+", "5");
        assertEquals(expectedTokens,slicedWord);
        System.out.println(" Passed");
    }

    @Test
    public void testTokenizeFromFile()
    {
        List<String> tokenList = tokenizer.getTokensFromFilename("ParserTest.txt");

        List<String> expectedTokens = List.of(
                "procedure", "nazwa", "{",
                "zmienna1", "=", "a", "+", "b", "+", "d", "+", "2", ";",
                "zmienna2", "=", "zmienna", "+", "2", ";",
                "zmienna3", "=", "zmienna", "+", "d", "+", "2", ";",
                "while", "zmienna1", "{",
                "nowosc", "=", "2", "+", "3", "+", "9", ";",
                "}", "}"
        );

        assertEquals(expectedTokens, tokenList);
        System.out.print("Passed");
    }

}
