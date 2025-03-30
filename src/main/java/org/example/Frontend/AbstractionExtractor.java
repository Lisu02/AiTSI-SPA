    package org.example.Frontend;

    import org.example.PKB.API.*;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.logging.Logger;

    public class AbstractionExtractor {

        private static final Logger log = Logger.getLogger(AbstractionExtractor.class.getName());

        //Na razie testowo imodifies
        private IAST iast;
        private IModifies iModifies;

        public AbstractionExtractor(){
            this(new IASTImplementationFrontend(),new ImodifiesFrontendImpl());
        }
        public AbstractionExtractor(IAST iast){
            this(iast,new ImodifiesFrontendImpl());
        }
        public AbstractionExtractor(IAST iast, IModifies iModifies){
            this.iast = iast;
            this.iModifies = iModifies;
        }

    //Root - program
    //Procedure
    //StmtList
    //Stmt
    //Stmt

    //Generowanie Modifies ora Uses do PKB
    public void generateStarterAbstractions(){
        TNode tNodeRoot = iast.getRoot();
        List<TNode> procedureList = new ArrayList<>();

        //Listing procedures
        TNode firstProcedureNode = iast.getLinkedNode(LinkType.FirstChild,tNodeRoot);
        procedureList.add(firstProcedureNode);
        TNode currentNode = firstProcedureNode;

        while (iast.getLinkedNode(LinkType.RightSibling,currentNode) != null){
            currentNode = iast.getLinkedNode(LinkType.RightSibling,currentNode);
            System.out.println("currentNode = " + currentNode);
            procedureList.add(currentNode);
        }

        for(TNode procedureNode: procedureList){
            System.out.println("procedureNode = " + procedureNode);

            generateModifies(procedureNode);
        }





        //generateModifies(tNodeRoot);
        generateUses(tNodeRoot);
    }

    private void generateModifies(TNode tNodeProcedure){
        TNode stmtList = iast.getFirstChild(tNodeProcedure);
        TNode currentNode = iast.getFirstChild(stmtList);
        TNode stmtNode = currentNode;
        System.out.println("currentNode in modifies -> " + currentNode); //todo: jest blad z petla do while
        do{
            if(iast.getType(currentNode) == EntityType.ASSIGN){
                currentNode = iast.getFirstChild(currentNode);
                iModifies.addModifies(tNodeProcedure,stmtNode,currentNode);
            }
            System.out.println("currentNode in modifies -> " + currentNode);
            currentNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }while (iast.getLinkedNode(LinkType.RightSibling,currentNode) != null);

        System.out.println("End of generateModifies");
    }

    private void generateUses(TNode tNodeRoot){
        System.out.println("generateUses - not implemented yet!");
    }

}
