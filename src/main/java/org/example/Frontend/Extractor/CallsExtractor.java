package org.example.Frontend.Extractor;

import org.example.PKB.API.IAST;
import org.example.PKB.API.ICalls;
import org.example.PKB.API.TNode;

import java.util.logging.Logger;

public class CallsExtractor {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final IAST iast;
    private final ICalls iCalls;

    public CallsExtractor(IAST iast, ICalls iCalls){
        this.iast = iast;
        this.iCalls = iCalls;

    }

    public void extract(TNode procedureNode, TNode stmtListTNode){
        if(iCalls == null){
            log.severe("No iCalls interface cannot extract abstractions");
        }
    }
}
