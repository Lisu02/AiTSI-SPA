package org.example.QueryProc.staticVal;

import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.QueryProc.model.ArgumentDefinition;
import org.example.QueryProc.model.RelationFunctions;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarRules {
    private static IAST IAST = PKB.getAST();
    private static final List<String> STATEMENT_TYPES = List.of("stmt", "assign", "call", "while", "if", "_", "integer", "prog_line");
    private static final List<String> PROCEDURE_TYPES = List.of("procedure", "_", "string");
    private static final List<String> VARIABLE_TYPES = List.of("variable", "_", "string");
    private static final List<String> ALL_TYPES = List.of("procedure", "string", "stmt", "assign", "call", "while", "if", "integer", "prog_line");

    public static final Map<String, ArgumentDefinition> RELATION_DEFINITIONS = Map.of(
            "Follows", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES),
            "Follows*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES),
            "Parent", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES),
            "Parent*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES),
            "Modifies", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES),
            "Modifies*", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES),
            "Uses", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES),
            "Uses*", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES),
            "Calls", new ArgumentDefinition(PROCEDURE_TYPES, PROCEDURE_TYPES),
            "Calls*", new ArgumentDefinition(PROCEDURE_TYPES, PROCEDURE_TYPES)
    );
    public static final Map<String, RelationFunctions> RELATION_FUNCTIONS = Map.of(
//            "Follows", new RelationFunctions(IAST::getFollowedBy,IAST::getFollows,IAST::isFollowed),
//            "Follows*", new RelationFunctions(IAST::getFollowedAstraBy,IAST::getFollowsAstra,IAST::isFollowedAstra),
//            "Parent", new RelationFunctions(IAST::getParentedBy,IAST::getParent,IAST::isParent),
//            "Parent*", new RelationFunctions(IAST::getParentedAstraBy,IAST::getParentAstra,IAST::isParentAstra),
            "Modifies", new RelationFunctions(null,null,null),
            "Modifies*", new RelationFunctions(null,null,null),
            "Uses", new RelationFunctions(null,null,null),
            "Uses*", new RelationFunctions(null,null,null));

    public static final Set<String> ENTITIES = Set.of(
            "procedure", "stmtLst", "stmt", "assign", "if", "while", "variable"
    );
    public static final Map<String, Set<String>> ATTRIBUTES = Map.of(
            "procName", Set.of("procedure", "call"),
            "varName", Set.of("variable"),
            "value", Set.of("constant"),
            "stmt#", Set.of("stmt", "assign", "while", "if", "call")
    );
}
