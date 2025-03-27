package org.example.PKB.API;

import org.example.Exceptions.ASTBuildException;

import java.util.List;

public interface IAST {
    public TNode createTNode(EntityType et);
    public void setRoot(TNode node);
    public void setAttr(TNode n, Attr attr);
    public void setParentChildLink(TNode p, TNode c) throws ASTBuildException;
    public TNode getRoot();
    public EntityType getType(TNode node);
    public Attr getAttr(TNode node);
    public TNode getFirstChild(TNode p);
    public TNode getLinkedNode(LinkType link, TNode node1);
    public boolean isLinked(LinkType link, TNode node1, TNode node2);
    public TNode getParent(TNode c);
    public List<TNode> getParentedBy(TNode p);

    // Astra -> Astreisk -> Nawa symbolu * na klawiaturze, bo nie może być częścią nazwy
    public TNode getParentAstra(TNode c);
    public List<TNode> getParentedAstraBy(TNode p);
    public TNode getFollows(TNode n);
    public List<TNode> getFollowsAstra(TNode n);
    public TNode getFollowedBy(TNode n);
    public List<TNode> getFollowedAstraBy(TNode n);
    public boolean isFollowed(TNode n1, TNode n2);
    public boolean isFollowedAstra(TNode n1, TNode n2);
    public boolean isParent(TNode n1, TNode n2);
    public boolean isParentAstra(TNode n1, TNode n2);
    List<TNode> getTNodes(String entityType);
    TNode getTNode(String name);
    TNode getTNode(int progLine);
}
