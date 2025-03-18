package org.example.PKB;

import java.util.*;

class Calls {
    private final Map<String, Set<String>> callsMap = new HashMap<>();

    public void setCalls(String caller, String callee) {
        callsMap.computeIfAbsent(caller, k -> new HashSet<>()).add(callee);
    }

    public Set<String> getCalledProcedures(String caller) {
        return callsMap.getOrDefault(caller, Collections.emptySet());
    }

    public boolean isCalled(String caller, String callee) {
        return callsMap.getOrDefault(caller, Collections.emptySet()).contains(callee);
    }
}
