package org.example.PKB.API;

import org.example.PKB.Source.AST;
import org.example.PKB.Source.Relations.Calls;
import org.example.PKB.Source.Relations.Next;
import org.example.PKB.Source.Relations.Uses;
import org.example.PKB.Source.VarTable;


public class PKB {

    private static IVarTable varTable = new VarTable();
    private static IAST ast = new AST();
    private static ICalls calls = new Calls();
    private static IModifies modifies;
    private static IUses uses = new Uses();
    private static INext next = new Next();
    public static IVarTable getVarTable()
    {
        return varTable;
    }

    public static IAST getAST()
    {
        return ast;
    }

    public static IUses getUses() {
        return uses;
    }

    public static ICalls getCalls() {
        return calls;
    }

    public static IModifies getModifies() {
        return modifies;
    }

    public static INext getNext() {return next;}

    public static void setCalls(ICalls calls) {
        PKB.calls = calls;
    }
    public static void setModifies(IModifies modifies) {
        PKB.modifies = modifies;
    }
}
