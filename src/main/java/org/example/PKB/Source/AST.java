package org.example.PKB.Source;

import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTImplementations.ASTLinks;

import java.util.List;

public class AST extends ASTLinks {
    @Override
    public List<TNode> getTNodes(String entityType) {
        return null;
    }

    @Override
    public TNode getTNode(String name) {
        return null;
    }

    @Override
    public TNode getTNode(int progLine) {
        return null;
    }
    // Duża szansa, że tu nic nie będzie poza jakimś prostym konstruktorem!
}
