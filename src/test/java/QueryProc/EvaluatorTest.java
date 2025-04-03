package QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;
import org.example.QueryProc.Evaluator;
import org.example.QueryProc.PreProc;
import org.example.QueryProc.ResultProjector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluatorTest {
    private final PreProc preProc = new PreProc();
    private final Evaluator evaluator = new Evaluator();
    private final ResultProjector resultProjector = new ResultProjector();
    @BeforeAll
    public static void setUp() {
        Tokenizer tokenizer = Tokenizer.getInstance();

        Parser parser = Parser.getInstance();

        parser.getTokens(tokenizer.getTokensFromFilename("ParserTest.txt"));

        AbstractionExtractor ae = new AbstractionExtractor();

        ae.generateStarterAbstractions();
    }

    static Stream<Arguments> queryProvider() throws IOException {
        return Files.lines(Path.of("src/test/resources/queryEvaluator1"))
                //.filter(line-> !line.isEmpty() && !line.startsWith("//"))
                .map(line -> line.split("!", 2))
                .map(parts ->
                        Arguments.of(parts[0].trim(),
                        Arrays.stream(parts[1]
                                .split(","))
                                .map(String::trim)
                                .collect(Collectors.toList())));
    }
    @ParameterizedTest
    @MethodSource("queryProvider")
    void evaluateQuery(String query, List<String> expectedResult) throws InvalidQueryException {
        List<String> result = resultProjector.convertToString(evaluator.evaluateQuery(preProc.parseQuery(query)));

        Collections.sort(expectedResult);
        Collections.sort(result);

        assertEquals(expectedResult,result);
    }

//    @Test
//    void shouldReturnEmptySetForEmptyRelationList(){
//        List<Relation> emptyRelations = new ArrayList<>();
//        List<Argument> returnValues = Arrays.asList(new Argument("s","stmt"));
//        QueryTree queryTree = new QueryTree(true,returnValues,emptyRelations,new ArrayList<>());
//
//        Set<TNode> result = evaluator.evaluateQuery(queryTree);
//
//        Set<TNode> expected = new HashSet<>();
//        assertEquals(expected,result);
//    }
//
//    @Test
//    void shouldReturnCorrectResultsForSingleValidRelation() {
//        Argument arg1 = new Argument("a1", "assign");
//        Argument arg2 = new Argument("a2", "assign");
//
//        Relation relation = new Relation("Follows", arg1, arg2);
//        IAST ast = PKB.getAST();
//        TNode node1 = ast.createTNode(EntityType.ASSIGN);
//        TNode node2 = ast.createTNode(EntityType.ASSIGN);
//
//        try {
//            PKB.getAST().setParentChildLink(node1, node2);
//        } catch (ASTBuildException e) {
//            fail("Exception was thrown while setting parent-child link: " + e.getMessage());
//        }
//        QueryTree queryTree = new QueryTree(false,List.of(arg1), List.of(relation), List.of());
//
//        Set<TNode> result = evaluator.evaluateQuery(queryTree);
//
//        assertNotNull(result, "Result should not be null");
//        assertTrue(result.contains(node1), "Result should contain node1");
//    }
//    @Test
//    void shouldReturnEmptySetForUnmatchedRelation() {
//        List<Relation> relations = Arrays.asList(
//                new Relation("Follows", new Argument("s1", "stmt"), new Argument("s2", "stmt"))
//        );
//        List<Argument> returnValues = Arrays.asList(new Argument("s1", "stmt"));
//        QueryTree queryTree = new QueryTree(true, returnValues, relations, new ArrayList<>());
//
//        Set<TNode> result = evaluator.evaluateQuery(queryTree);
//
//        assertTrue(result.isEmpty(), "Expected an empty result set when there are no matching relations");
//    }


}