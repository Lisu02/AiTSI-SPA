package org.example.PKB.API;

import java.util.List;

public interface IModifies {

    //Modifies można albo sprawdzać stmt (numer stmt'u) albo jaka procedura np. ("Third)
    //modifiedNode to node z Variable ktory jest po lewej stronie podstawienia
    void addModifies(TNode procedureNode,TNode stmtNode, TNode variableNode);
    List<TNode> getModifies(TNode node);
    List<TNode> getModifiesBy(TNode var);
    boolean isModifying(TNode node, TNode var);
}
