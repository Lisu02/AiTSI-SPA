package org.example.Frontend;

import org.example.PKB.API.IModifies;
import org.example.PKB.API.TNode;

public class ImodifiesFrontendImpl implements IModifies {
    @Override
    public void addModifies(TNode procedureNode, TNode stmtNode, TNode modifiedNode) {
        System.out.printf("Adding modifies relationship in [%s,%s] for [%s]\n",procedureNode,stmtNode,modifiedNode);
    }
}
