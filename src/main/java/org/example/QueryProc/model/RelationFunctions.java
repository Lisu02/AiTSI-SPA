package org.example.QueryProc.model;

import org.example.PKB.API.TNode;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public record RelationFunctions(
        Function<TNode, List<TNode>> byFunction,
        Function<TNode, List<TNode>> function,
        BiPredicate<TNode, TNode> isFunction
) {}