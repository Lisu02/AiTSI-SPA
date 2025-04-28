package org.example.QueryProc.model;

import org.example.PKB.API.TNode;

import java.util.Set;

public record Result(Relation relation, Set<TNode> firstArguments, Set<TNode> secondArguments) {
}
