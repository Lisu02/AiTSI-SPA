package org.example.PKB;

import java.util.*;

public class Modifies {
    private final Map<Integer, Set<String>> modifiesStmt = new HashMap<>();
    private final Map<String, Set<String>> modifiesProc = new HashMap<>();

    public void setModifies(int stmt, String var) {
        modifiesStmt.computeIfAbsent(stmt, k -> new HashSet<>()).add(var);
    }

    public void setModifies(String proc, String var) {
        modifiesProc.computeIfAbsent(proc, k -> new HashSet<>()).add(var);
    }

    public Set<String> getModified(int stmt) {
        return modifiesStmt.getOrDefault(stmt, Collections.emptySet());
    }

    public Set<String> getModified(String proc) {
        return modifiesProc.getOrDefault(proc, Collections.emptySet());
    }

    public boolean isModified(int stmt, String var) {
        return modifiesStmt.getOrDefault(stmt, Collections.emptySet()).contains(var);
    }

    public boolean isModified(String proc, String var) {
        return modifiesProc.getOrDefault(proc, Collections.emptySet()).contains(var);
    }
}
