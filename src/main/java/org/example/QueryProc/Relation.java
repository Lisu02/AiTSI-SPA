package org.example.QueryProc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Relation {
    private String name;
    private Argument arg1;
    private Argument arg2;
}
