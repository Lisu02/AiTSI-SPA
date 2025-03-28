package org.example.QueryProc.DataStructs;

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
