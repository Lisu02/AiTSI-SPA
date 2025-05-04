package org.example.Frontend.Extractor;

import org.example.PKB.API.*;

import java.util.logging.Logger;

public class ModifiesExtractor {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final IModifies iModifies;

    public ModifiesExtractor(IAST iast, IModifies iModifies){
        this.iast = iast;
        this.iModifies = iModifies;
    }

    public void extract(TNode tNodeProcedure, TNode stmtListTNode){
        TNode stmtNode = iast.getFirstChild(stmtListTNode);
        TNode variableNode;

        while(stmtNode != null){
            EntityType stmtType = iast.getType(stmtNode);
            switch (stmtType){
                case ASSIGN: {
                    variableNode = iast.getFirstChild(stmtNode);
                    //log.fine("Modifies line "+iast.getAttr(stmtNode).getLine()+" : " + variableNode.toString().substring(43,62) + " and " + stmtNode.toString().substring(43,60));
                    iModifies.addModifies(tNodeProcedure,stmtNode,variableNode);
                    break;
                }
                case WHILE: {
                    TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode)); // variable(warunek) -> stmt
                    extract(tNodeProcedure,tmp);// while -> stmtList do metody
                    break;
                }
                case IF: {
                    TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                    extract(tNodeProcedure,tmp); // for if stmtList
                    tmp = iast.getLinkedNode(LinkType.RightSibling,tmp);
                    extract(tNodeProcedure,tmp); // for else stmtList
                    break;
                }
            }
            stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }
    }

}
