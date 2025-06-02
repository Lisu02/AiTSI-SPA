package org.example.Frontend.Extractor;

import org.example.PKB.API.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class NextExtractor {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final INext iNext;

    public NextExtractor(IAST iast, INext iNext) {
        this.iast = iast;
        this.iNext = iNext;
    }

    public void extract() {
        TNode node = iast.getRoot();
        extractNode(node);
    }

    private TNode extractNode(TNode node) {
        if(node == null)return null;
        TNode nextNode;
        switch (iast.getType(node)) {
            case PROGRAM:
                extractNode(iast.getFirstChild(node));
                break;

            case PROCEDURE:
                extractNode(iast.getFirstChild(node));
                extractNode(iast.getLinkedNode(LinkType.RightSibling, node));
                break;

            case STMTLIST:
                return extractNode(iast.getFirstChild(node));

            case ASSIGN:
                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if(nextNode == null)return node;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

            case CALL:
                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if(nextNode == null)return node;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

            case WHILE:
                nextNode = extractIfWhileHandler(node, iast.getLinkedNode(LinkType.RightSibling, iast.getFirstChild(node)));
                safeAddRelation(nextNode, node);

                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if (nextNode == null) return node;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

            case IF:
                nextNode = iast.getLinkedNode(LinkType.RightSibling, iast.getFirstChild(node));
                extractIfWhileHandler(node, nextNode);
                extractIfWhileHandler(node, iast.getLinkedNode(LinkType.RightSibling, nextNode));

                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if (nextNode == null) return node;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

        }
        return node;
    }
    private TNode extractIfWhileHandler(TNode condNode, TNode listNode)
    {
        TNode nextNode = iast.getFirstChild(listNode);
        if (nextNode == null) return condNode;
        safeAddRelation(condNode, nextNode);
        nextNode = extractNode(listNode);
        return nextNode;
    }

    private void safeAddRelation(TNode lNode, TNode rNode) {
        try{
            iNext.setNext(lNode, rNode);
        }
        catch (Exception e) {
            return;
        }
    }

    private void extractAstra(TNode prevNode, TNode node, Set<TNode>used) { /// TODO
//        if(node == null)return;
//        if(used.contains(node))return;
//
//        boolean flagWhile = iast.getType(node) == EntityType.WHILE;
//        boolean isWhile = flagWhile;
//
//        for(TNode it : iNext.getPrevious(node)) {
//            if(!used.contains(it))
//            {
//                if(flagWhile)flagWhile = false;
//                else return;
//            }
//        }
//        if(isWhile && )used.add(node);
//        for(TNode it : iNext.getPreviousAstra(prevNode))
//        {
//
//        }

    }

}
