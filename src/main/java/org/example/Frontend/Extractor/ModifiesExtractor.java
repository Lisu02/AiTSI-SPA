package org.example.Frontend.Extractor;

import org.example.PKB.API.*;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.Logger;

public class ModifiesExtractor {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final IModifies iModifies;
    private boolean isCalls = false;

    public ModifiesExtractor(IAST iast, IModifies iModifies){
        this.iast = iast;
        this.iModifies = iModifies;
    }

    public void extract(TNode tNodeProcedure, TNode stmtListTNode,Stack<TNode> tNodeStack){
        if(tNodeProcedure == null || stmtListTNode == null){
            //System.out.println("nulle!");
           // System.out.println(tNodeStack);
            //System.out.println("isCalls: " + isCalls);
        }
        if(tNodeStack == null){
            tNodeStack = new Stack<>();
        }
        TNode stmtNode = iast.getFirstChild(stmtListTNode);
        TNode variableNode;

        while(stmtNode != null){
            EntityType stmtType = iast.getType(stmtNode);
            switch (stmtType){
                case ASSIGN: {
                    tNodeStack.push(stmtNode);
                    variableNode = iast.getFirstChild(stmtNode);
                    log.info("Modifies line "+iast.getAttr(stmtNode).getLine()+" : " + variableNode.toString() + " and " + stmtNode.toString());
                    log.info(iast.getAttr(variableNode).getVarName());
                    iModifies.addModifies(tNodeProcedure,tNodeStack,variableNode); //todo: zmienic modifies na List<TNode>
                    tNodeStack.pop();
                    break;
                }
                case WHILE: {
                    tNodeStack.push(stmtNode);
                    TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode)); // variable(warunek) -> stmt
                    extract(tNodeProcedure,tmp,tNodeStack);// while -> stmtList do metody
                    tNodeStack.pop();
                    break;
                }
                case IF: {
                    tNodeStack.push(stmtNode);
                    TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode)); // omijamy tutaj variable w if
                    extract(tNodeProcedure,tmp,tNodeStack); // for if stmtList
                    tmp = iast.getLinkedNode(LinkType.RightSibling,tmp);
                    extract(tNodeProcedure,tmp,tNodeStack); // for else stmtList
                    tNodeStack.pop();
                    break;
                }
                case CALL: {
                    TNode procedureNode = iast.getFirstChild(stmtNode);
                    if(!tNodeStack.contains(procedureNode) || !procedureNode.equals(tNodeProcedure)){
                        tNodeStack.add(stmtNode);
//                        Stack<TNode> newStack = new Stack<>();
//                        try{
//                            newStack.add(tNodeStack.peek());
//                        }catch (EmptyStackException e){}
//                        newStack.add(stmtNode);
                        extract(tNodeProcedure,iast.getFirstChild(procedureNode),tNodeStack);
                    }
                    tNodeStack.pop();
                    break;
                }
            }
            stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }
        isCalls = false;
    }
    //stmtNode - calls [nazwa procedury]
    private void insideCalls(TNode tNodeProcedure, TNode stmtListTNode,Stack<TNode> tNodeStack,TNode stmtNode){
        
    }

}
