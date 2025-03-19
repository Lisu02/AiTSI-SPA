package QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.QueryProc.PreProc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PreProcTest {
    private final PreProc preProc = new PreProc();

    @Test
    void shouldThrowExceptionWhenRelationNameIsIncorrect() {
        String query = "stmt s1,s2; procedure p; Select p, s2 such that Modifies (s2, _) such that Cals (p, \"Second\") with p.procName = \"Third\"";
        Exception exception = assertThrows(InvalidQueryException.class, () -> {
            preProc.parseQuery(query);
        });
        assertEquals("Incorrect relation name: Cals",exception.getMessage());

    }
}
