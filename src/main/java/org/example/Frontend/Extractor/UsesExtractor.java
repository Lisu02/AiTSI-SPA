package org.example.Frontend.Extractor;

import org.example.PKB.API.*;

import java.util.Stack;
import java.util.logging.Logger;

public class UsesExtractor {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final IUses iUses;

    public UsesExtractor(IAST iast, IUses iUses){
        this.iast = iast;
        this.iUses = iUses;
    }

    public void extract(TNode tNodeProcedure,TNode stmtListTNode,Stack<TNode> tNodeStack){
        if(tNodeStack == null){
            tNodeStack = new Stack<>();
        }
        if(stmtListTNode == null){
            stmtListTNode = iast.getFirstChild(tNodeProcedure);
        }
        TNode currentTNode = iast.getFirstChild(stmtListTNode); //pierwszy tnode w stmtList
        while (currentTNode != null){
            EntityType currentType = iast.getType(currentTNode);
            switch (currentType){
                case ASSIGN -> {
                    tNodeStack.push(currentTNode); //assign node
                    TNode leftVarNode = iast.getFirstChild(currentTNode);
                    leftVarNode = iast.getLinkedNode(LinkType.RightSibling,leftVarNode);
                    traverseAssignForUses(tNodeProcedure,leftVarNode,(Stack<TNode>) tNodeStack.clone());
                    tNodeStack.pop();
                }
                case WHILE -> {
                    tNodeStack.push(currentTNode);

                    TNode variableTNode = iast.getFirstChild(currentTNode);
                    iUses.setUses(tNodeProcedure,tNodeStack,variableTNode);

                    TNode whileStmtList = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(currentTNode));
                    extract(tNodeProcedure,whileStmtList,tNodeStack);
                    tNodeStack.pop(); //zdjęcie while ze stosu ponieważ idziemy dalej
                }
                case IF -> log.warning("IF nie ma implementacji USES");
                case CALL -> log.warning("CALL nie ma implementacji USES");
            }

            currentTNode = iast.getLinkedNode(LinkType.RightSibling,currentTNode);
        }
    }

    private void traverseAssignForUses(TNode tNodeProcedure,TNode leftVarNode,Stack<TNode> tNodeStack){
        TNode currentTNode = iast.getFirstChild(leftVarNode);
        if(currentTNode == null){
            if(iast.getType(leftVarNode) == EntityType.VARIABLE){
                try{
                    Integer.parseInt(iast.getAttr(leftVarNode).getVarName());
                }catch (NumberFormatException e) {
                    iUses.setUses(tNodeProcedure,tNodeStack,leftVarNode); // Adding right side variable node
                    log.info("Mam nazwe zmiennej nie liczba git!");
                }
            }
        }
        while (currentTNode != null){
            if(iast.getType(currentTNode) == EntityType.VARIABLE){
                try{
                    Integer.parseInt(iast.getAttr(currentTNode).getVarName());
                }catch (NumberFormatException e) {
                    iUses.setUses(tNodeProcedure,tNodeStack,currentTNode); // Adding right side variable node
                    log.info("Mam nazwe zmiennej nie liczba git!");
                }
            }else {
                traverseAssignForUses(tNodeProcedure,currentTNode,tNodeStack);
            }
            currentTNode = iast.getLinkedNode(LinkType.RightSibling,currentTNode);
        }
    }


}
