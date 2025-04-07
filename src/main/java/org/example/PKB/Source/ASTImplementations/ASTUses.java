package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.*;
import org.example.PKB.Source.NodeImplementations.ProcedureNode;
import org.example.PKB.Source.NodeImplementations.StmtNode;
import org.example.PKB.Source.NodeImplementations.VariableNode;

import java.util.*;

public class ASTUses implements IUses {
    private final IAST AST = PKB.getAST();
    private Map<TNode, List<TNode>> variableMap = new HashMap<>();
    private Map<TNode, List<TNode>> nodeMap = new HashMap<>();
    private static IUses astUses = new ASTUses();
    private ASTUses() {

    }
    public static IUses getInstance() {
        return astUses;
    }
    @Override
    public void setUses(TNode node, String value) {
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        if(!(node instanceof ProcedureNode) && !(node instanceof StmtNode)) {
            //throw new ASTBuildException("Invalid ndoe");
        }
        TNode tmp = AST.getNodesOfEntityTypes(EntityType.VARIABLE).stream()
                .filter(v-> AST.getAttr(v).getVarName().equals(value))
                .findFirst()
                .orElse(null);

        if(tmp instanceof VariableNode variable) {
            add(variable,node,variableMap);
            add(node,variable,nodeMap);
        }
        else {
            //throw new ASTBuildException("Variable doesn't exist");
        }
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
    public List<TNode> getUses(TNode node) {
        return nodeMap.get(node) != null ? nodeMap.get(node) : Collections.emptyList();
    }

    @Override
    public List<TNode> getUsedBy(TNode var) {
        return variableMap.get(var) != null ? variableMap.get(var) : Collections.emptyList();
    }
    @Override
    public boolean isUsing(TNode node, TNode var) {
        return variableMap.get(var).contains(node);
    }
}
