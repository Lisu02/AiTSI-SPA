package QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.QueryProc.PreProc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PreProcTest {
    private final PreProc preProc = new PreProc();

    static Stream<Arguments> queryProvider() throws IOException {
        return Files.lines(Path.of("src/test/resources/invalid_query_list"))
                .map(line -> line.split("#", 2))
                .map(parts -> Arguments.of(parts[0].trim(), parts[1].trim()));
    }

    @ParameterizedTest
    @MethodSource("queryProvider")
    void shouldThrowExceptionWhenQueryIsInvalid(String query, String expectedMessage) {
        Exception exception = assertThrows(InvalidQueryException.class, () -> {
            preProc.parseQuery(query);
        });
        assertEquals(expectedMessage,exception.getMessage());
    }
}
