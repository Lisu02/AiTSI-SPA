package org.example.QueryProc.staticVal;

import org.example.PKB.API.*;
import org.example.PKB.Source.ASTImplementations.ASTModifies;
import org.example.PKB.Source.ASTImplementations.ASTUses;
import org.example.PKB.Source.Relations.Calls;
import org.example.QueryProc.model.ArgumentDefinition;
import org.example.QueryProc.model.RelationFunctions;

import java.util.*;

public class GrammarRules {
    private static IAST IAST = PKB.getAST();
    private final static IModifies modifiesInterface = ASTModifies.getInstance();
    private final static IUses usesInterface = ASTUses.getInstance();
    private final static ICalls callsInterface = Calls.getInstance();
    private final static INext nextInterface = PKB.getNext();
    private static final List<String> STATEMENT_TYPES = Collections.unmodifiableList(
            Arrays.asList("stmt", "assign", "call", "while", "if", "blank", "integer", "prog_line")
    );
    private static final List<String> PROCEDURE_TYPES = Collections.unmodifiableList(
            Arrays.asList("procedure", "blank", "string")
    );
    private static final List<String> VARIABLE_TYPES = Collections.unmodifiableList(
            Arrays.asList("variable", "blank", "string")
    );
    private static final List<String> ALL_TYPES = Collections.unmodifiableList(
            Arrays.asList("procedure", "string", "stmt", "assign", "call", "while", "if", "integer", "prog_line")
    );

    // Map: RELATION_DEFINITIONS
    public static final Map<String, ArgumentDefinition> RELATION_DEFINITIONS;
    static {
        Map<String, ArgumentDefinition> relDefs = new HashMap<>();
        relDefs.put("Follows", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT));
        relDefs.put("Follows*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT));
        relDefs.put("Parent", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT));
        relDefs.put("Parent*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT));
        relDefs.put("Modifies", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE));
        relDefs.put("Modifies*", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE));
        relDefs.put("Uses", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE));
        relDefs.put("Uses*", new ArgumentDefinition(ALL_TYPES, VARIABLE_TYPES, EntityType.VARIABLE));
        relDefs.put("Calls", new ArgumentDefinition(PROCEDURE_TYPES, PROCEDURE_TYPES, EntityType.PROCEDURE));
        relDefs.put("Calls*", new ArgumentDefinition(PROCEDURE_TYPES, PROCEDURE_TYPES, EntityType.PROCEDURE));
        relDefs.put("Next", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT));
        relDefs.put("Next*", new ArgumentDefinition(STATEMENT_TYPES, STATEMENT_TYPES, EntityType.STMT));
        RELATION_DEFINITIONS = Collections.unmodifiableMap(relDefs);
    }

    // Utility method
    private static <T> List<T> safeList(T item) {
        return item == null ? Collections.emptyList() : Collections.singletonList(item);
    }

    // Map: RELATION_FUNCTIONS
    public static final Map<String, RelationFunctions> RELATION_FUNCTIONS;
    static {
        Map<String, RelationFunctions> relFuncs = new HashMap<>();
        relFuncs.put("Follows", new RelationFunctions(
                ast -> safeList(IAST.getFollowedBy(ast)),
                ast -> safeList(IAST.getFollows(ast)),
                IAST::isFollowed
        ));
        relFuncs.put("Follows*", new RelationFunctions(
                IAST::getFollowedAstraBy,
                IAST::getFollowsAstra,
                IAST::isFollowedAstra
        ));
        relFuncs.put("Parent", new RelationFunctions(
                IAST::getParentedBy,
                ast -> safeList(IAST.getParent(ast)),
                IAST::isParent
        ));
        relFuncs.put("Parent*", new RelationFunctions(
                IAST::getParentedAstraBy,
                IAST::getParentAstra,
                IAST::isParentAstra
        ));
        relFuncs.put("Modifies", new RelationFunctions(
                modifiesInterface::getModifies,
                modifiesInterface::getModifiesBy,
                modifiesInterface::isModifying
        ));
        relFuncs.put("Uses", new RelationFunctions(
                usesInterface::getUses,
                usesInterface::getUsedBy,
                usesInterface::isUsing
        ));
        relFuncs.put("Calls", new RelationFunctions(
                callsInterface::getCalls,
                callsInterface::getCalledBy,
                callsInterface::isCalls
        ));
        relFuncs.put("Calls*", new RelationFunctions(
                callsInterface::getCallsAstra,
                callsInterface::getCalledByAstra,
                callsInterface::isCallsAstra
        ));
        relFuncs.put("Next", new RelationFunctions(
                nextInterface::getNext,
                nextInterface::getPrevious,
                nextInterface::isNext
        ));
        relFuncs.put("Next*", new RelationFunctions(
                nextInterface::getNextAstra,
                nextInterface::getPreviousAstra,
                nextInterface::isNextAstra
        ));
        RELATION_FUNCTIONS = Collections.unmodifiableMap(relFuncs);
    }

    // Set: ENTITIES
    public static final Set<EntityType> ENTITIES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    EntityType.PROCEDURE,
                    EntityType.STMT,
                    EntityType.ASSIGN,
                    EntityType.IF,
                    EntityType.WHILE,
                    EntityType.VARIABLE,
                    EntityType.CALL,
                    EntityType.PROG_LINE
            ))
    );

    // Map: ATTRIBUTES
    public static final Map<String, Set<EntityType>> ATTRIBUTES;
    static {
        Map<String, Set<EntityType>> attrMap = new HashMap<>();
        attrMap.put("procName", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                EntityType.PROCEDURE,
                EntityType.CALL,
                EntityType.STRING
        ))));
        attrMap.put("varName", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                EntityType.VARIABLE,
                EntityType.STRING
        ))));
        attrMap.put("value", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                EntityType.CONSTANT,
                EntityType.PROG_LINE,
                EntityType.INTEGER
        ))));
        attrMap.put("stmt#", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                EntityType.STMT,
                EntityType.ASSIGN,
                EntityType.WHILE,
                EntityType.IF,
                EntityType.CALL,
                EntityType.PROG_LINE,
                EntityType.INTEGER
        ))));
        ATTRIBUTES = Collections.unmodifiableMap(attrMap);
    }
}
