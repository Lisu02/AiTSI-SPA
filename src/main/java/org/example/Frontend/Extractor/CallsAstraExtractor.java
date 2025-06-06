package org.example.Frontend.Extractor;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.IAST;
import org.example.PKB.API.ICalls;
import org.example.PKB.API.LinkType;
import org.example.PKB.API.TNode;

import java.util.List;
import java.util.logging.Logger;

public class CallsAstraExtractor {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final ICalls iCalls;

    public CallsAstraExtractor(IAST iast, ICalls iCalls){
        this.iast = iast;
        this.iCalls = iCalls;
    }

    public void extract(TNode primaryProcedureNode,List<TNode> visitedNodesList, TNode stmtListTNode) throws RelationException {
        if(stmtListTNode == null) return;
        TNode stmtNode = iast.getFirstChild(stmtListTNode);
        while (stmtNode != null){
            switch (iast.getType(stmtNode)){
                case CALL: {
                    TNode procedureNode = iast.getFirstChild(stmtNode);
                    if(!visitedNodesList.contains(procedureNode) || !procedureNode.equals(primaryProcedureNode)){
                        iCalls.setCallsAstra(primaryProcedureNode,procedureNode);
                        visitedNodesList.add(procedureNode);
                        extract(primaryProcedureNode,visitedNodesList,iast.getFirstChild(procedureNode));
                    }
                    break;
                }
                case WHILE: {
                    TNode stmtListWhile = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                    extract(primaryProcedureNode,visitedNodesList,stmtListWhile);
                    break;
                }
                case IF: {
                    TNode stmtListThen = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                    TNode stmtListElse = iast.getLinkedNode(LinkType.RightSibling,stmtListThen);
                    extract(primaryProcedureNode,visitedNodesList,stmtListThen);
                    extract(primaryProcedureNode,visitedNodesList,stmtListElse);
                    break;
                }
            }
            stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }

    }

}
