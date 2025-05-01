package org.example.PKB.API;

/**
 * API interface for AST tree
 */
public interface IVarTable {
    /**
     * Insert variable of @p varName name into VarTable.
     * @param varName Variable name.
     * @return Index of variable inside array.
     */
    public int insertVar(String varName);

    /**
     * Returns name of variable by id in array.
     * @param ind Variable id in array.
     * @return Name of variable.
     */
    public String getVarName(int ind);

    /**
     * Insert id in array variable of @p varName name.
     * @param varName Variable name.
     * @return Id from array if variable name exists, otherwise -1.
     */
    public int getVarIndex(String varName);

    /**
     * Returns current size of VarTable.
     * @return VarTable size.
     */
    public int getSize();

    /**
     * Checks if @p varName exists inside VarTable.
     * @param varName Variable name.
     * @return True if name exists inside, false otherwise.
     */
    public boolean isIn(String varName);
}
