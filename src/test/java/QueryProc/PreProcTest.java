package QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.QueryProc.PreProc;
import org.example.QueryProc.QueryTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PreProcTest {
    private final PreProc preProc = new PreProc();

    static Stream<Arguments> queryProvider() throws IOException {
        return Files.lines(Path.of("src/test/resources/invalid_query_list"))
                .map(line -> line.split("#", 2))
                .map(parts -> Arguments.of(parts[0].trim(), parts[1].trim()));
    }

    static Stream<Arguments> validQueryProvider() throws IOException {
        return Files.lines(Path.of("src/test/resources/valid_query_list"))
                .map(Arguments::of);
    }

    static Stream<Arguments> synonymProvider() throws IOException{
        return Files.lines(Path.of("src/test/resources/synonyms_query_list"))
                .map(line -> line.split("#", 2))
                .map(parts -> Arguments.of(parts[0].trim(),parseSynonyms(parts[1].trim())));
    }

    private static Map<String,String> parseSynonyms(String data){
        Map<String,String> synonyms = new HashMap<>();
        for (String pair : data.split(",")){
            String[] keyValue = pair.split("=");
            synonyms.put(keyValue[0], keyValue[1]);
        }
        return synonyms;
    }

    @ParameterizedTest
    @MethodSource("queryProvider")
    void shouldThrowExceptionWhenQueryIsInvalid(String query, String expectedMessage) {
        Exception exception = assertThrows(InvalidQueryException.class, () -> {
            preProc.parseQuery(query);
        });
        assertEquals(expectedMessage,exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("validQueryProvider")
    void shouldParseValidQuery(String query){
        assertDoesNotThrow(() -> preProc.parseQuery(query));
    }

    @ParameterizedTest
    @MethodSource("synonymProvider")
    void shouldSeparateSynonymsCorrectly(String query, Map<String,String> expectedSynonyms) throws InvalidQueryException{
        QueryTree queryTree = preProc.parseQuery(query);
        expectedSynonyms.forEach((synonym,expectedType) ->
            assertEquals(expectedType,queryTree.getSynonyms().get(synonym)));
    }

    @Test
    void shouldThrowExceptionWhenQueryIsEmpty(){
        Exception exception = assertThrows(InvalidQueryException.class, () -> preProc.parseQuery(""));
        assertEquals("Missing Select: ", exception.getMessage());
    }

}
