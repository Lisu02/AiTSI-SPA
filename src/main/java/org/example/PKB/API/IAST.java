package org.example.PKB.API;

import org.example.Exceptions.ASTBuildException;

import java.util.List;

/**
 * API interface for AST tree
 */
public interface IAST {
    /**
     * Produces new node of type corresponding to @p et.
     * @param et node type.
     * @return New node if entity type exists and makes sense to exist, otherwise NULL.
     */
    public TNode createTNode(EntityType et);

    /**
     * Set @p node as root to tree.
     * @param node new root.
     */
    public void setRoot(TNode node);

    /**
     * Sett attribute @p attr to node @p n.
     * @param n Destination node,
     * @param attr Attribute to set.
     */
    public void setAttr(TNode n, Attr attr);

    /**
     * Connect two nodes in Parent-Child relation
     * @param p Parent,
     * @param c Child.
     * @throws ASTBuildException when it's impossible to connect two instances.
     */
    public void setParentChildLink(TNode p, TNode c) throws ASTBuildException;

    /**
     * Returns current root of the tree.
     * @return Root of the tree if it's set, otherwise NULL.
     */
    public TNode getRoot();

    /**
     * Evaluate type of @p node.
     * @param node Node to be evaluated.
     * @return Entity type of evaluated @p node, or NULL if incorrect entity was given.
     */
    public EntityType getType(TNode node);

    /**
     * Returns stored attribute inside node
     * @param node Node for extraction.
     * @return Extracted attribute from node, or NULL if nothing stored or invalid entity given.
     */
    public Attr getAttr(TNode node);

    /**
     * Returns first child of a @p node.
     * @param p Parent.
     * @return First @p child or NULL if no children or invalid entity given.
     */
    public TNode getFirstChild(TNode p);

    /**
     *
     * @param link
     * @param node1
     * @return
     */
    public TNode getLinkedNode(LinkType link, TNode node1);

    /**
     *
     * @param link
     * @param node1
     * @param node2
     * @return
     */
    public boolean isLinked(LinkType link, TNode node1, TNode node2);

    /**
     * Returns parent of @p c node in PQL relation.
     * @param c Children
     * @return Parent of @c node or NULL invalid entity given.
     */
    public TNode getParent(TNode c);

    /**
     *
     * @param p
     * @return
     */
    public List<TNode> getParentedBy(TNode p);

    /**
     *
     * @param c
     * @return
     */
    public List<TNode> getParentAstra(TNode c);

    /**
     *
     * @param p
     * @return
     */
    public List<TNode> getParentedAstraBy(TNode p);

    /**
     *
     * @param n
     * @return
     */
    public TNode getFollows(TNode n);

    /**
     *
     * @param n
     * @return
     */
    public List<TNode> getFollowsAstra(TNode n);

    /**
     *
     * @param n
     * @return
     */
    public TNode getFollowedBy(TNode n);

    /**
     *
     * @param n
     * @return
     */
    public List<TNode> getFollowedAstraBy(TNode n);

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    public boolean isFollowed(TNode n1, TNode n2);

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    public boolean isFollowedAstra(TNode n1, TNode n2);

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    public boolean isParent(TNode n1, TNode n2);

    /**
     *
     * @param n1
     * @param n2
     * @return
     */
    public boolean isParentAstra(TNode n1, TNode n2);

    /**
     * Returns a list of all nodes in tree being type of @p et
     * @param et Type of nodes to find
     * @return A list of nodes or NULL, if no nodes o this type are in list.
     */
    public List<TNode> getNodesOfEntityTypes(EntityType et);

}
