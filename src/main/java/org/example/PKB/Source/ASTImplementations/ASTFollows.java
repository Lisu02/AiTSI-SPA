package org.example.PKB.Source.ASTImplementations;

import org.example.PKB.API.TNode;
import org.example.PKB.Source.ASTNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ASTFollows extends ASTParents {
    public TNode getFollows(TNode n) {
        try {
            ASTNode astNode = (ASTNode) n;
            ASTNode parent = astNode.getParent();

            if (parent != null) {
                return  parent.getChild(astNode.getId() - 1);
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {

        }
        return null;
    }

    public List<TNode> getFollowsAstra(TNode n)
    {
        ASTNode astNode = (ASTNode) n;
        ASTNode parent = astNode.getParent();
        if (parent != null) {
            return parent.getChildren().stream()
                    .filter(child -> child.getId() < astNode.getId())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    public TNode getFollowedBy(TNode n)
    {
        try {
            ASTNode astNode = (ASTNode) n;
            ASTNode parent = astNode.getParent();

            if (parent != null) {
                return  parent.getChild(astNode.getId() + 1);
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {

        }
       return null;
    }
    public List<TNode> getFollowedAstraBy(TNode n)
    {
        ASTNode astNode = (ASTNode) n;
        ASTNode parent = astNode.getParent();
        if (parent != null) {
            return parent.getChildren().stream()
                    .filter(child -> child.getId() > astNode.getId())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
