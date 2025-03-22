package org.example.PKB.Old;

import java.util.HashMap;
import java.util.Map;

class VarTable {
    private final Map<String, Integer> varMap = new HashMap<>();
    private int indexCounter = 0;

    public int insertVar(String varName) {
        return varMap.computeIfAbsent(varName, v -> indexCounter++);
    }

    public String getVarName(int index) {
        return varMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(index))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public int getVarIndex(String varName) {
        return varMap.getOrDefault(varName, -1);
    }

    public int getSize() {
        return varMap.size();
    }

    public boolean isIn(String varName) {
        return varMap.containsKey(varName);
    }
}
