package org.example.Frontend.Extractor;

import org.example.PKB.API.*;

import java.util.*;
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

    private List<TNode> extractNode(TNode node) {
        if(node == null)return null;
        TNode nextNode;
        List<TNode> nextNodes;
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
                if(nextNode == null)break;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

            case CALL:
                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if(nextNode == null)break;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

            case WHILE:
                nextNodes = extractWhileHandler(node, iast.getLinkedNode(LinkType.RightSibling, iast.getFirstChild(node)));
                for(TNode n : nextNodes) {
                    safeAddRelation(n, node);
                }

                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if (nextNode == null) break;
                safeAddRelation(node, nextNode);
                return extractNode(nextNode);

            case IF:
                nextNode = iast.getLinkedNode(LinkType.RightSibling, iast.getFirstChild(node));
                nextNodes = new ArrayList<>();
                nextNodes.addAll(extractIfHandler(node, nextNode));
                nextNodes.addAll(extractIfHandler(node, iast.getLinkedNode(LinkType.RightSibling, nextNode)));

                nextNode = iast.getLinkedNode(LinkType.RightSibling, node);
                if (nextNode == null) return nextNodes;
                for (TNode n : nextNodes) {
                    safeAddRelation(n, nextNode);
                }
                return extractNode(nextNode);

        }
        return new ArrayList<>(Collections.singletonList(node));
    }
    private List<TNode> extractIfHandler(TNode condNode, TNode listNode)
    {
        TNode nextNode = iast.getFirstChild(listNode);
        if (nextNode == null) return new ArrayList<>(Collections.singletonList(condNode));
        safeAddRelation(condNode, nextNode);
        return extractNode(listNode);
    }

    private List<TNode> extractWhileHandler(TNode condNode, TNode listNode)
    {
        TNode nextNode = iast.getFirstChild(listNode);
        if (nextNode == null) return new ArrayList<>(Collections.singletonList(condNode));
        safeAddRelation(condNode, nextNode);
        return extractNode(listNode);
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
