package org.example.Frontend;
import org.example.PKB.API.IUses;
import org.example.PKB.API.TNode;
public class IUsesFrontendImpl implements IUses{
    @Override
    public void setUses(TNode node, String value){
        System.out.printf("Adding uses relationship in "+ node +" for value "+value);
    }
}
