package org.example.QueryProc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.PKB.API.EntityType;

import java.util.List;
@Data
@AllArgsConstructor
public class ArgumentDefinition {
    private List<String> arg1;
    private List<String> arg2;
    private EntityType defaultType;
    public boolean isArg1TypeAllowed(String arg) {
        return arg1.contains(arg);
    }
    public boolean isArg2TypeAllowed(String arg) {
        return arg2.contains(arg);
    }
}