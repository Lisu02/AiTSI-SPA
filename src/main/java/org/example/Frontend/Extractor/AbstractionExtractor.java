    package org.example.Frontend.Extractor;

    import org.example.PKB.API.*;
    import org.example.PKB.Source.ASTImplementations.ASTModifies;
    import org.example.PKB.Source.ASTImplementations.ASTUses;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Stack;
    import java.util.logging.Logger;

    public class AbstractionExtractor {

        private static final Logger log = Logger.getLogger(AbstractionExtractor.class.getName());

        //Na razie testowo imodifies
        private IAST iast;
        private ModifiesExtractor modifiesExtractor;
        private UsesExtractor usesExtractor;
        private CallsExtractor callsExtractor;

        public AbstractionExtractor(){
            this(PKB.getAST(),ASTModifies.getInstance(),ASTUses.getInstance(),null); //temporary null for calls implementation
        }
        public AbstractionExtractor(IAST iast){
            this(iast,ASTModifies.getInstance(),ASTUses.getInstance(),null);
        }
        public AbstractionExtractor(IAST iast, IModifies iModifies,IUses iUses, ICalls iCalls){
            this.iast = iast;
            this.modifiesExtractor = new ModifiesExtractor(iast,iModifies);
            this.usesExtractor = new UsesExtractor(iast,iUses);
            this.callsExtractor = new CallsExtractor(iast,iCalls);
        }

    //Generowanie Modifies oraz Uses do PKB
    public void generateStarterAbstractions(){
        TNode tNodeRoot = iast.getRoot();
        List<TNode> procedureList = new ArrayList<>();
        TNode firstProcedureNode = iast.getLinkedNode(LinkType.FirstChild,tNodeRoot);
        procedureList.add(firstProcedureNode);

        TNode currentNode = firstProcedureNode;
        while (iast.getLinkedNode(LinkType.RightSibling,currentNode) != null){
            currentNode = iast.getLinkedNode(LinkType.RightSibling,currentNode);
            log.fine("currentNode = " + currentNode);
            procedureList.add(currentNode);
        }
        log.info("Amount of detected procedures -> {" + procedureList + "}\n");

        for(TNode procedureNode: procedureList){
            modifiesExtractor.extract(procedureNode,iast.getFirstChild(procedureNode),null);
            usesExtractor.extract(procedureNode,iast.getFirstChild(procedureNode),null);
        }
    }







}
