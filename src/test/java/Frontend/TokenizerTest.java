package Frontend;

import org.example.Frontend.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
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
    @DisplayName("Slicing: +pepperonii+ should return 3 tokens")
    public void shouldSliceWordWithPlusSignsAroundWord(){
        //given
        String word = "+pepperonii+";
        //when
        List<String> slicedWord = tokenizer.sliceWord(word);
        //then
        assertIterableEquals(Arrays.asList("+","pepperonii","+"),slicedWord);
    }

    @Test
    @DisplayName("Slicing: +1+ should return 3 tokens")
    public void shouldSliceWordWithPlusSignsAroundSingleNumber(){
        //given
        String word = "+1+";
        //when
        List<String> slicedWord = tokenizer.sliceWord(word);
        //then
        assertIterableEquals(Arrays.asList("+","1","+"),slicedWord);
    }

    @Test
    @DisplayName("Slicing: +123+ should return 3 tokens")
    public void shouldSliceWordWithPlusSignsAroundThreeNumbers(){
        //given
        String word = "+123+";
        //when
        List<String> slicedWord = tokenizer.sliceWord(word);
        //then
        assertIterableEquals(Arrays.asList("+","123","+"),slicedWord);
    }

    @Test
    public void checkProperTokenizationProcessWithWhileAndCallsAndIf(){
        //given
        URL resource = getClass().getClassLoader()
                .getResource("frontend/tokenizerTestFile.txt");
        if (resource == null){fail("No file found");}
        File file = new File(resource.getFile());
        String absolutePath = file.getAbsolutePath();

        //when
        List<String> tokenList = tokenizer.getTokensFromFilename(absolutePath);

        //then
        List<String> expectedList = new LinkedList<String>(Arrays.asList(
                "procedure","First","{"
                ,"x","=","5",";",
                "a","=","x","+","3","+","1",";",
                "c","=","x","+","a","+","1","+","2","+","5",";",
                "while","c","{",
                "c","=","c","-","1",";",
                "}",
                "if","a","then","{",
                "call","Second",";",
                "}","else","{",
                "a","=","0",";",
                "}",
                "x","=","0",";",
                "}",
                "procedure","Second","{",
                "p","=","10",";",
                "d","=","20",";",
                "l","=","d","+","p",";",
                "}"
        ));
        assertIterableEquals(expectedList,tokenList);
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
