package org.example.PKB;
//sprawdzic z tymi public bo nie jestem pewien czy dobrze :/
//sprawdzic czy wszystkie metody w API
public class PKB {
    private final VarTable varTable = new VarTable();
    private final AST ast = new AST();
    private final Modifies modifies = new Modifies();
    private final Calls calls = new Calls();

    public VarTable getVarTable() {
        return varTable;
    }

    public AST getAST() {
        return ast;
    }

    public Modifies getModifies() {
        return modifies;
    }

    public Calls getCalls() {
        return calls;
    }
}

