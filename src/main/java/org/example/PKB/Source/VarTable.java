package org.example.PKB.Source;

import org.example.Exceptions.NotImplementedRuntimeException;
import org.example.PKB.API.IVarTable;

import java.util.*;

public class VarTable implements IVarTable {

    private final Map<String, Integer> varMap = new LinkedHashMap<>();
    private final List<String> varList = new ArrayList<>();
    public int insertVar(String varName)
    {
        varList.add(varName);
        int index = varList.size() - 1;
        varMap.put(varName, index);
        return index;
    }
    public String getVarName(int ind)
    {
        return varList.get(ind);
    }
    public int getVarIndex(String varName)
    {
        return varMap.getOrDefault(varName, -1) ;
    }
    public int getSize()
    {
        return varList.size();
    }
    public boolean isIn(String varName)
    {
        return varMap.containsKey(varName);
    }
}
