package org.example.QueryProc.DataStructs;

import lombok.Getter;
import org.example.PKB.API.TNode;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
@Getter
public class RelationFunctions {
    Function<TNode, List<TNode>> byFunction;
    Function<TNode, TNode> function;
    BiPredicate<TNode, TNode> isFunction;

    RelationFunctions(Function<TNode, List<TNode>> byFunction,
                   Function<TNode, TNode> function,
                   BiPredicate<TNode, TNode> isFunction) {
        this.byFunction = byFunction;
        this.function = function;
        this.isFunction = isFunction;
    }
}
