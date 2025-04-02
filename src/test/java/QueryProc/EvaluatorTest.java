package QueryProc;

import org.example.QueryProc.Evaluator;

public class EvaluatorTest {
    private final Evaluator evaluator = new Evaluator();

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