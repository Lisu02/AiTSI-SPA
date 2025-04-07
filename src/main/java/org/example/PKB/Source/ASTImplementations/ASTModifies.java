package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.IAST;
import org.example.PKB.API.IModifies;
import org.example.PKB.API.PKB;
import org.example.PKB.API.TNode;

import java.util.*;

public class ASTModifies implements IModifies {
    private final IAST AST = PKB.getAST();
    private Map<TNode, List<TNode>> variableStmtMap = new HashMap<>();
//    private final Map<TNode, List<TNode>> variableProcMap = new HashMap<>();
    private Map<TNode, List<TNode>> nodeMap = new HashMap<>();
    private static IModifies astModifies = new ASTModifies();
    private ASTModifies() {
        super();
    }
    public static IModifies getInstance() {
        return astModifies;
    }
    @Override
    public void addModifies(TNode procedureNode, TNode stmtNode, TNode variableNode) {

//        if(!(procedureNode instanceof ProcedureNode)) {
//            throw new ASTBuildException("Invalid procedureNode");
//        }
//        if(!(stmtNode instanceof StmtNode)) {
//            throw new ASTBuildException("Invalid stmtNode");
//        }
//        if(!(variableNode instanceof VariableNode)) {
//            throw new ASTBuildException("Invalid variableNode");
//        }

        add(variableNode,stmtNode,variableStmtMap);
        add(variableNode,procedureNode,variableStmtMap);
        add(stmtNode,variableNode,nodeMap);
        add(procedureNode,variableNode,nodeMap);
    }
    private void add(TNode keyNode, TNode valueNode, Map<TNode, List<TNode>> map) {
        if(map.containsKey(keyNode) && !map.get(keyNode).contains(valueNode)) {
            map.get(keyNode).add(valueNode);
        }
        else {
            map.put(keyNode,new ArrayList<>(List.of(valueNode)));
        }
    }

    @Override
    public List<TNode> getModifies(TNode node) {
        return nodeMap.get(node) != null ? nodeMap.get(node) : Collections.emptyList();
    }

    @Override
    public List<TNode> getModifiesBy(TNode var) {
        return variableStmtMap.get(var) != null ? variableStmtMap.get(var) : Collections.emptyList();
    }
    @Override
    public boolean isModifying(TNode node, TNode var) {
        return variableStmtMap.get(var).contains(node);
    }
}
