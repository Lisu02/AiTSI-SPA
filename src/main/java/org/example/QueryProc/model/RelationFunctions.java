package org.example.QueryProc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.PKB.API.TNode;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
@Data
@AllArgsConstructor
public class RelationFunctions {
    private Function<TNode, List<TNode>> byFunction;
    private Function<TNode, List<TNode>> function;
    private BiPredicate<TNode, TNode> isFunction;
}

