package org.example.QueryProc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithStatement {
    private Argument arg1;
    private String attribute;
    private Argument arg2;
}

