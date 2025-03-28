package org.example.QueryProc.DataStructs;

import java.util.Set;

public class RelationSignature {
    private final Set<String> arg1;
    private final Set<String> arg2;
    public RelationSignature(Set<String> arg1, Set<String> arg2) {
        this.arg1 = Set.copyOf(arg1);
        this.arg2 = Set.copyOf(arg2);
    }
    public boolean isArg1TypeAllowed(String arg) {
        return arg1.contains(arg);
    }
    public boolean isArg2TypeAllowed(String arg) {
        return arg2.contains(arg);
    }
}
