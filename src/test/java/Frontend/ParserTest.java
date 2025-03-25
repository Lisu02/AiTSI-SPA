package Frontend;

import org.example.Frontend.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {


    @Test
    void checkTokenTekst(){
        //given
        Parser parser = Parser.getInstance();
        String token = "Procedure";
        //when

        parser.checkToken(token);

        //then
        Assertions.assertTrue(true);
    }
}
