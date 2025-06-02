package org.example.Frontend.Extractor;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.*;

import java.util.List;
import java.util.logging.Logger;

public class CallsExtractor {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final ICalls iCalls;
    private List<TNode> procedureList;

    public CallsExtractor(IAST iast, ICalls iCalls){
        this.iast = iast;
        this.iCalls = iCalls;

    }

    public void provide(List<TNode> procedureList){
        this.procedureList = procedureList;
    }

    public void extract(TNode procedureNode, TNode stmtListTNode){
        TNode stmtNode = iast.getFirstChild(stmtListTNode);

        while(stmtNode != null) {
            switch (iast.getType(stmtNode)){
                case CALL: {
                    TNode calledProcedure = iast.getFirstChild(stmtNode);
                    try{
                        iCalls.setCalls(procedureNode,calledProcedure);
                        log.info(iast.getType(stmtNode).name());
                        EntityType type = iast.getType(stmtNode);
                    } catch (RelationException e) {
                        Logger.getLogger(getClass().getName()).severe(e.getMessage());
                    }
                    break;
                }
                case WHILE: {
                    TNode whileStmtList = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                    extract(procedureNode,whileStmtList);
                    break;
                }
                case IF: {
                    TNode thenStmtList = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                    TNode elseStmtList = iast.getLinkedNode(LinkType.RightSibling,thenStmtList);
                    extract(procedureNode,thenStmtList );
                    extract(procedureNode,elseStmtList );
                    break;
                }
            }
            stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }
    }
}
