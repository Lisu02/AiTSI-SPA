package org.example.QueryProc.staticVal;

import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.PKB;
import org.example.QueryProc.model.ArgumentDefinition;
import org.example.QueryProc.model.RelationFunctions;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarRules {
    private static IAST IAST = PKB.getAST();
    private static final List<String> STATEMENT_TYPES = List.of("stmt", "assign", "call", "while", "if", "blank", "integer", "prog_line");
    private static final List<String> PROCEDURE_TYPES = List.of("procedure", "blank", "string");
    private static final List<String> VARIABLE_TYPES = List.of("variable", "blank", "string");
    private static final List<String> ALL_TYPES = List.of("procedure", "string", "stmt", "assign", "call", "while", "if", "integer", "prog_line");

    public static final Map<String, ArgumentDefinition> RELATION_DEFINITIONS = Map.of(
            "Follows", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT),
            "Follows*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT),
            "Parent", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT),
            "Parent*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT),
            "Modifies", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE),
            "Modifies*", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE),
            "Uses", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE),
            "Uses*", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE),
            "Calls", new ArgumentDefinition(PROCEDURE_TYPES, PROCEDURE_TYPES, EntityType.PROCEDURE),
            "Calls*", new ArgumentDefinition(PROCEDURE_TYPES, PROCEDURE_TYPES, EntityType.PROCEDURE)
    );
    private static <T> List<T> safeList(T item) {
        return item == null ? Collections.emptyList() : Collections.singletonList(item);
    }
    public static final Map<String, RelationFunctions> RELATION_FUNCTIONS = Map.of(
            "Follows", new RelationFunctions(ast -> safeList(IAST.getFollowedBy(ast)),ast -> safeList(IAST.getFollows(ast)),IAST::isFollowed),
            "Follows*", new RelationFunctions(IAST::getFollowedAstraBy,IAST::getFollowsAstra,IAST::isFollowedAstra),
            "Parent", new RelationFunctions(IAST::getParentedBy,ast -> safeList(IAST.getParent(ast)),IAST::isParent),
            "Parent*", new RelationFunctions(IAST::getParentedAstraBy,IAST::getParentAstra,IAST::isParentAstra),
            "Modifies", new RelationFunctions(null,null,null),
            "Modifies*", new RelationFunctions(null,null,null),
            "Uses", new RelationFunctions(null,null,null),
            "Uses*", new RelationFunctions(null,null,null));

    public static final Set<EntityType> ENTITIES = Set.of(
            EntityType.PROCEDURE, EntityType.STMT, EntityType.ASSIGN, EntityType.IF, EntityType.WHILE, EntityType.VARIABLE
    );
    public static final Map<String, Set<EntityType>> ATTRIBUTES = Map.of(
            "procName", Set.of(EntityType.PROCEDURE, EntityType.CALL),
            "varName", Set.of(EntityType.VARIABLE),
            "value", Set.of(EntityType.CONSTANT),
            "stmt#", Set.of(EntityType.STMT, EntityType.ASSIGN, EntityType.WHILE, EntityType.IF, EntityType.CALL)
    );
}
