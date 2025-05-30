package org.example.Frontend.Extractor;

import org.example.Exceptions.RelationException;
import org.example.PKB.API.EntityType;
import org.example.PKB.API.IAST;
import org.example.PKB.API.ICalls;
import org.example.PKB.API.TNode;

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
                    } catch (RelationException e) {
                        Logger.getLogger(getClass().getName()).severe(e.getMessage());
                    }
                }
                case WHILE:

            }
        }
    }
}
