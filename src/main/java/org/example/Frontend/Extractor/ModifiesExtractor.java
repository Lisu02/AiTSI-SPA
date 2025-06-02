package org.example.Frontend.Extractor;

import org.example.PKB.API.*;

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
//                    insideCalls(tNodeProcedure,stmtListTNode,tNodeStack,stmtNode);
//                    if(true) {
//                        isCalls = true;
//                        tNodeStack.push(stmtNode);
//                        TNode procedureTmp = null;
//                        TNode stmtListProc = null;
//                        if(stmtNode != null){
//                            procedureTmp = iast.getLinkedNode(LinkType.FirstChild,stmtNode);
//                            //System.out.println("procedureTMP:" + procedureTmp);
//                        }
//                        if(procedureTmp != null){
//                            stmtListProc = iast.getLinkedNode(LinkType.FirstChild,procedureTmp);
//                            stmtListProc = iast.getFirstChild(procedureTmp);
//                            //System.out.println("stmtListProc:" + stmtListProc);
//                        }
//
//                        //System.out.println(tNodeProcedure + " | " + stmtListProc + " | " + tNodeStack);
//                        extract(tNodeProcedure,stmtListProc,tNodeStack);
//                        //log.warning("Going calls ");
//                        //log.warning("CALL IN MODIFIES | CALL IN MODIFIES | CALL IN MODIFIES");
//                    }
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
