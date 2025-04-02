package org.example.PKB.API;

public interface IModifies {

    //Modifies można albo sprawdzać stmt (numer stmt'u) albo jaka procedura np. ("Third)
    //modifiedNode to node z Variable ktory jest po lewej stronie podstawienia
    public void addModifies(TNode procedureNode,TNode stmtNode, TNode modifiedNode);
}
