package org.example.Frontend;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IASTImplementationFrontend implements IAST {
    @Override
    public TNode createTNode(EntityType et) {
        System.out.println("Creating node type: " + et.name());
        return new TNodeImpl();
    }

    @Override
    public void setRoot(TNode node) {
        System.out.println("Setting root for node" + node);
    }

    @Override
    public void setAttr(TNode n, Attr attr) {
        System.out.println("Setting attr for node - " + attr.toString());
    }

    @Override
    public void setParentChildLink(TNode p, TNode c) throws ASTBuildException {
        System.out.println("Setting parent - " + p +  " child link- " + c);
    }

    @Override
    public TNode getRoot() {
        return null;
    }

    @Override
    public EntityType getType(TNode node) {
        return null;
    }

    @Override
    public Set<EntityType> getAllTypes(TNode node) {
        return new HashSet<>();
    }

    @Override
    public Attr getAttr(TNode node) {
        return null;
    }

    @Override
    public TNode getFirstChild(TNode p) {
        return null;
    }

    @Override
    public TNode getLinkedNode(LinkType link, TNode node1) {
        return null;
    }

    @Override
    public boolean isLinked(LinkType link, TNode node1, TNode node2) {
        return false;
    }

    @Override
    public TNode getParent(TNode c) {
        return null;
    }

    @Override
    public List<TNode> getParentedBy(TNode p) {
        return Collections.emptyList();
    }

    @Override
    public List<TNode> getParentAstra(TNode c) {
        return null;
    }

    @Override
    public List<TNode> getParentedAstraBy(TNode p) {
        return Collections.emptyList();
    }

    @Override
    public TNode getFollows(TNode n) {
        return null;
    }

    @Override
    public List<TNode> getFollowsAstra(TNode n) {
        return Collections.emptyList();
    }

    @Override
    public TNode getFollowedBy(TNode n) {
        return null;
    }

    @Override
    public List<TNode> getFollowedAstraBy(TNode n) {
        return Collections.emptyList();
    }

    @Override
    public boolean isFollowed(TNode n1, TNode n2) {
        return false;
    }

    @Override
    public boolean isFollowedAstra(TNode n1, TNode n2) {
        return false;
    }

    @Override
    public boolean isParent(TNode n1, TNode n2) {
        return false;
    }

    @Override
    public boolean isParentAstra(TNode n1, TNode n2) {
        return false;
    }

    @Override
    public Set<TNode> getNodesOfEntityTypes(EntityType et) {
        return Collections.emptySet();
    }
}
