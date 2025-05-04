package org.example.QueryProc.model;

import lombok.Data;
import org.example.PKB.API.TNode;

import java.util.Set;
@Data
public class Result {
    private Relation relation;
    private Set<TNode> firstArguments;
    private Set<TNode> secondArguments;
}
