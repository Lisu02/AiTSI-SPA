package Frontend;

import org.example.Frontend.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    private Tokenizer tokenizer;

    @BeforeEach
    void init(){
        tokenizer = new Tokenizer();
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
}
