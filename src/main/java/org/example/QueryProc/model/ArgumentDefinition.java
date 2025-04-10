package org.example.QueryProc.model;

import org.example.PKB.API.EntityType;

import java.util.List;

public record ArgumentDefinition(List<String> arg1, List<String> arg2, EntityType defaultType) {
    public boolean isArg1TypeAllowed(String arg) {
        return arg1.contains(arg);
    }
    public boolean isArg2TypeAllowed(String arg) {
        return arg2.contains(arg);
    }
}