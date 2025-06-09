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

        Map<TNode, TNode> unitedNodes = new HashMap<>();
        Map<TNode, Set<TNode>> cyclesAll = new HashMap<>();
        iNext.setUnitedNodes(unitedNodes);
        iNext.setCycles(cyclesAll);
//        for(TNode n : iast.getNodesOfEntityTypes(EntityType.STMT))
        for(TNode n : getBegins(node))
        {
//            List<TNode> prevNodes = iNext.getPrevious(n);
//            if(prevNodes != null && (prevNodes.isEmpty() ||
//                    (iast.getType(n) == EntityType.WHILE && prevNodes.size() == 1)))
//            {
            Map<TNode, Integer> LOW = new HashMap<>();
            Map<TNode, TNode> United = new HashMap<>();
            Map<TNode, Integer> graphParents = new HashMap<>();
            Map<TNode, Set<TNode>> Cycles = new HashMap<>();

            getLOW(n, LOW, United, 1);
            UnionLOW(United);
            unitedNodes.putAll(United);

            BuildCyclesAstra(United, Cycles);
            cyclesAll.putAll(Cycles);

            BuildGraphAstra(n, United, graphParents);
            BuildAstra(n, United, graphParents);
//            }
        }
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

    /************************
    ****** Next Astra********
    ************************/

    private List<TNode> getBegins(TNode root)
    {
        List<TNode> begins = new ArrayList<>();
        if(root == null || iast.getType(root) != EntityType.PROGRAM) return begins;
        TNode proc = iast.getFirstChild(root);

        while (proc != null && iast.getType(proc) == EntityType.PROCEDURE) {
            TNode listOfNodes = iast.getFirstChild(proc);
            if (listOfNodes != null ){
                TNode begin = iast.getFirstChild(listOfNodes);
                if (begin != null) begins.add(begin);
            }
            proc = iast.getLinkedNode(LinkType.RightSibling, proc);
        }
        return begins;
    }

    private int getLOW(TNode node, Map<TNode, Integer> LOW, Map<TNode, TNode> Union, int preOrder) {
//        LOW.put(node, preOrder);
        LOW.put(node, iast.getAttr(node).getLine());
        preOrder++;

        Union.put(node, node);

        List<TNode> nextNodes = iNext.getNext(node);
        for(TNode n : nextNodes) {
            if(!LOW.containsKey(n)){
                preOrder = getLOW(n, LOW, Union, preOrder);
            }
            int myLow = LOW.get(Union.get(node));
            int nextLow = LOW.get(Union.get(n));
            if(myLow > nextLow)Union.put(node, Union.get(n));
        }

        return preOrder;
    }

    private TNode findLOW(TNode node, Map<TNode, TNode> Union)
    {
        if(Union.get(node) == node)return node;
        Union.put(node, findLOW(Union.get(node), Union));
        return Union.get(node);
    }

    private void UnionLOW(Map<TNode, TNode> Union)
    {
        for(TNode n : Union.keySet()) {
            findLOW(n, Union);
        }
    }

    private void BuildCyclesAstra(Map<TNode, TNode> Union, Map<TNode, Set<TNode>> Cycles)
    {
        for(Map.Entry<TNode, TNode> entry : Union.entrySet()) {
            if(entry.getKey() == entry.getValue())
            {
                Cycles.put(entry.getKey(), new HashSet<>());
                Set<TNode> cycle = Cycles.get(entry.getKey());
                for(Map.Entry<TNode, TNode> entryTo : Union.entrySet())
                {
                    if(entry.getKey() == entryTo.getValue()) {
                        cycle.add(entryTo.getKey());
                    }
                }
            }
        }
    }

    private void BuildGraphAstra(TNode node, Map<TNode, TNode> United, Map<TNode, Integer> graphParents)
    {
        graphParents.put(node, 0);
        for(TNode n : iNext.getNext(node)) {
            TNode un = United.get(n);
            if(n != United.get(n))continue;
            if(!graphParents.containsKey(n))BuildGraphAstra(n, United, graphParents);
            graphParents.put(n, graphParents.get(n) + 1);
        }
    }

    private void BuildAstra(TNode node, Map<TNode, TNode> Union, Map<TNode, Integer> graphParents)
    {
        if(node != Union.get(node))return;
        List<TNode> prevNodes = iNext.getPreviousAstra(node);

        if(iast.getType(node) == EntityType.WHILE)safeAddAstraRelation(node, node);

        for(TNode n : iNext.getNext(node)) {
            if(n != Union.get(n) || !graphParents.containsKey(n))continue;
            safeAddAstraRelation(node, n);

            for(TNode prevNode : prevNodes) {
                if(prevNode != Union.get(prevNode) || !graphParents.containsKey(prevNode))continue;
                safeAddAstraRelation(prevNode, n);
            }

            int parentsLeft = graphParents.get(n);
            parentsLeft--;
            graphParents.put(n, parentsLeft);
            if(parentsLeft == 0)BuildAstra(n, Union, graphParents);
        }

    }

    private void safeAddAstraRelation(TNode lNode, TNode rNode) {
        try{
            iNext.setNextAstra(lNode, rNode);
        }
        catch (Exception e) {
            return;
        }
    }

}
