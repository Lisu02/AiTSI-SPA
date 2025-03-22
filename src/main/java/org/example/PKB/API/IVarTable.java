package org.example.PKB.API;

public interface IVarTable {
    public int insertVar(String varName);
    public String getVarName(int ind);
    public int getVarIndex(String varName);
    public int getSize();
    public boolean isIn(String varName);
}
