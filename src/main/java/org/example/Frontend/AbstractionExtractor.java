    package org.example.Frontend;

    import org.example.PKB.API.*;
    import org.example.PKB.Source.ASTImplementations.ASTModifies;
    import org.example.PKB.Source.ASTImplementations.ASTUses;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.logging.Logger;

    public class AbstractionExtractor {

        private static final Logger log = Logger.getLogger(AbstractionExtractor.class.getName());

        //Na razie testowo imodifies
        private IAST iast;
        private IModifies iModifies;
        private IUses iUses;

        public AbstractionExtractor(){
            this(PKB.getAST(),ASTModifies.getInstance(),ASTUses.getInstance());
        }
        public AbstractionExtractor(IAST iast){
            this(iast,ASTModifies.getInstance(),ASTUses.getInstance());
        }
        public AbstractionExtractor(IAST iast, IModifies iModifies,IUses iuses){
            this.iast = iast;
            this.iModifies = iModifies;
            this.iUses=iuses;
        }

    //Generowanie Modifies oraz Uses do PKB
    public void generateStarterAbstractions(){
        TNode tNodeRoot = iast.getRoot();
        List<TNode> procedureList = new ArrayList<>();

        //Listowanie procedur
        TNode firstProcedureNode = iast.getLinkedNode(LinkType.FirstChild,tNodeRoot);
        procedureList.add(firstProcedureNode);
        TNode currentNode = firstProcedureNode;

        while (iast.getLinkedNode(LinkType.RightSibling,currentNode) != null){
            currentNode = iast.getLinkedNode(LinkType.RightSibling,currentNode);
            System.out.println("currentNode = " + currentNode);
            procedureList.add(currentNode);
        }

        System.out.println(procedureList);
        for(TNode procedureNode: procedureList){
            generateModifies(procedureNode,iast.getFirstChild(procedureNode));
            generateUses(procedureNode);
        }
    }

    //rekurencja
    private void generateModifies(TNode tNodeProcedure,TNode stmtListTNode){
        TNode stmtNode = iast.getFirstChild(stmtListTNode);
        TNode variableNode;

        while(stmtNode != null){
            if(iast.getType(stmtNode) == EntityType.ASSIGN){
                variableNode = iast.getFirstChild(stmtNode);
                iModifies.addModifies(tNodeProcedure,stmtNode,variableNode);
            }else {
                TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                generateModifies(tNodeProcedure,tmp);// while -> stmtList do metody
            }
            stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }
        System.out.println("End of modifies");
    }

    private void generateUses(TNode tNodeProcedure){
            TNode stmtList = iast.getFirstChild(tNodeProcedure);
            TNode currentNode = iast.getFirstChild(stmtList);
            TNode stmtNode = currentNode;
            do{
                if(iast.getType(stmtNode)==EntityType.WHILE){
                    currentNode=iast.getFirstChild(stmtNode);
                    Attr add=iast.getAttr(currentNode);
                    iUses.setUses(stmtNode, add.getVarName());
                    currentNode=iast.getLinkedNode(LinkType.RightSibling,currentNode);
                    currentNode=iast.getFirstChild(currentNode);
                    do{
                        TNode right=iast.getFirstChild(currentNode);
                        right=iast.getLinkedNode(LinkType.RightSibling,right);
                        iterateUses(right,currentNode,stmtNode);
                        currentNode=iast.getLinkedNode(LinkType.RightSibling,currentNode);
                    }while(currentNode!=null);
                }
                else if(iast.getType(stmtNode)==EntityType.ASSIGN){
                    currentNode=iast.getFirstChild(stmtNode);
                    currentNode=iast.getLinkedNode(LinkType.RightSibling,currentNode);
                    iterateUses(currentNode,stmtNode,stmtNode);
                }
                stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
            }while(iast.getLinkedNode(LinkType.RightSibling,stmtNode)!=null);
            System.out.println("End of generateUses");
    }
    private void iterateUses(TNode test,TNode stmt,TNode parent){
            if(iast.getType(test)==EntityType.VARIABLE){
                Attr add=iast.getAttr(test);
                try{
                    int name= Integer.parseInt(add.getVarName());
                    add.setConstantValue(name);
                }
                catch(NumberFormatException e){
                    if(stmt!=parent){
                        iUses.setUses(stmt, add.getVarName());
                        iUses.setUses(parent, add.getVarName());
                    }
                    else{
                        iUses.setUses(stmt, add.getVarName());
                    }
                }
            }
            else{
                TNode children=iast.getFirstChild(test);
                iterateUses(children,stmt,parent);
                children=iast.getLinkedNode(LinkType.RightSibling,children);
                iterateUses(children,stmt,parent);
            }
    }
}
