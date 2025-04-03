package org.example.QueryProc.model;

import org.example.PKB.API.EntityType;

public record WithStatement (String name, EntityType type, String attribute, String constVal) {}
