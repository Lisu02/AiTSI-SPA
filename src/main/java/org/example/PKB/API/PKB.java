package org.example.PKB.API;

import org.example.PKB.Source.VarTable;

public class PKB {

    private static IVarTable varTable = new VarTable();
    private static IAST ast;
    private static ICalls calls;
    private static IModifies modifies;
    public static IVarTable getVarTable()
    {
        return varTable;
    }

    public static IAST getAST()
    {
        return ast;
    }

    public static ICalls getCalls() {
        return calls;
    }

    public static IModifies getModifies() {
        return modifies;
    }
    public static void setCalls(ICalls calls) {
        PKB.calls = calls;
    }
    public static void setModifies(IModifies modifies) {
        PKB.modifies = modifies;
    }
}
