package org.example.PKB.API;

import org.example.Exceptions.ASTBuildException;

import java.util.List;
import java.util.Set;

/**
 * API interface for AST tree
 */
public interface IAST {

    public void removeFakeProcedures();


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
     * Evaluate all types of @p node.
     * @param node Node to be evaluated.
     * @return Set of Entity types of evaluated @p node.
     */
    public Set<EntityType> getAllTypes(TNode node);

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
     * Retrieves the node linked to the given node via a specified link type.
     * @param link Type of link.
     * @param node1 Node to check linkage.
     * @return Linked node or NULL if no link exists.
     */
    public TNode getLinkedNode(LinkType link, TNode node1);

    /**
     * Checks if two nodes are linked via a specified link type.
     * @param link Type of link.
     * @param node1 First node.
     * @param node2 Second node.
     * @return True if the nodes are linked, false otherwise.
     */
    public boolean isLinked(LinkType link, TNode node1, TNode node2);

    /**
     * Returns parent of @p c node in PQL relation.
     * @param c Children
     * @return Parent of @c node or NULL invalid entity given.
     */
    public TNode getParent(TNode c);

    /**
     * Retrieves all nodes that are parents of the given node.
     * @param p Parent node.
     * @return List of parent nodes.
     */
    public List<TNode> getParentedBy(TNode p);

    /**
     * Retrieves all nodes that are parents* of the given child node.
     * @param c Child node.
     * @return List of parent nodes.
     */
    public List<TNode> getParentAstra(TNode c);

    /**
     * Retrieves all  nodes that are in relation parents*(p,c)
     * @param p Parent node.
     * @return List of ancestor nodes.
     */
    public List<TNode> getParentedAstraBy(TNode p);


    /**
     * Retrieves the node that follows the given node.
     * @param n Node in question.
     * @return Node that follows or NULL if none exists.
     */
    public TNode getFollows(TNode n);

    /**
     * Retrieves all nodes that  follows*  the given node.
     * @param n Node in question.
     * @return List of nodes that follows*.
     */
    public List<TNode> getFollowsAstra(TNode n);

    /**
     * Retrieves the node that is followed by the given node.
     * @param n Node in question.
     * @return Node that is followed or NULL if none exists.
     */
    public TNode getFollowedBy(TNode n);

    /**
     * Retrieves the List of nodes that are followed* by the given node.
     * @param n Node in question.
     * @return List of nodes
     */
    public List<TNode> getFollowedAstraBy(TNode n);


    /**
     * Checks if a node is followed by another node.
     * @param n1 First node.
     * @param n2 Second node.
     * @return True if n1 is followed by n2, false otherwise.
     */
    public boolean isFollowed(TNode n1, TNode n2);

    /**
     * Checks if a node is followed* by another node.
     * @param n1 First node.
     * @param n2 Second node.
     * @return True if n1 is followed* by n2, false otherwise.
     */
    public boolean isFollowedAstra(TNode n1, TNode n2);

    /**
     * Checks if a node is a direct parent of another node.
     * @param n1 First node.
     * @param n2 Second node.
     * @return True if n1 is the parent of n2, false otherwise.
     */
    public boolean isParent(TNode n1, TNode n2);


    /**
     * Checks if a node is a parent* of another node.
     * @param n1 First node.
     * @param n2 Second node.
     * @return True if n1 is the parent of n2, false otherwise.
     */
    public boolean isParentAstra(TNode n1, TNode n2);


    /**
     * Returns a list of all nodes in tree being type of @p et
     * @param et Type of nodes to find
     * @return A list of nodes or NULL, if no nodes o this type are in list.
     */
    public Set<TNode> getNodesOfEntityTypes(EntityType et);


}
