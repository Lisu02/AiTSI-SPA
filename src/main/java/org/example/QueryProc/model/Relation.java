package org.example.QueryProc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Relation{
    private String name;
    private Argument arg1;
    private Argument arg2;
}
