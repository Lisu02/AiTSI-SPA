    package org.example.Frontend.Extractor;

    import org.example.PKB.API.*;
    import org.example.PKB.Source.ASTImplementations.ASTModifies;
    import org.example.PKB.Source.ASTImplementations.ASTUses;
    import org.example.PKB.Source.ASTNode;
    import org.example.PKB.Source.NodeImplementations.CallNode;
    import org.example.PKB.Source.NodeImplementations.ProcedureNode;
    import org.example.PKB.Source.Relations.Calls;

    import java.util.ArrayList;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Set;
    import java.util.logging.Logger;

    public class AbstractionExtractor {

        private static final Logger log = Logger.getLogger(AbstractionExtractor.class.getName());

        //Na razie testowo imodifies
        private IAST iast;
        private ModifiesExtractor modifiesExtractor;
        private UsesExtractor usesExtractor;
        private CallsExtractor callsExtractor;
        private CallsAstraExtractor callsAstraExtractor;
        private NextExtractor nextExtractor;

        public AbstractionExtractor(){
            this(PKB.getAST(),ASTModifies.getInstance(),ASTUses.getInstance(), Calls.getInstance(), PKB.getNext()); //temporary null for calls implementation
        }
        public AbstractionExtractor(IAST iast){
            this(iast,ASTModifies.getInstance(),ASTUses.getInstance(),null,null);
        }
        public AbstractionExtractor(IAST iast, IModifies iModifies,IUses iUses, ICalls iCalls, INext iNext){
            this.iast = iast;
            this.modifiesExtractor = new ModifiesExtractor(iast,iModifies);
            this.usesExtractor = new UsesExtractor(iast,iUses);
            this.callsExtractor = new CallsExtractor(iast,iCalls);
            this.callsAstraExtractor = new CallsAstraExtractor(iast, iCalls);
            this.nextExtractor = new NextExtractor(iast, iNext);
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
        callsExtractor.provide(procedureList);
        try{
            for(TNode procedureNode: procedureList){
                modifiesExtractor.extract(procedureNode,iast.getFirstChild(procedureNode),null);
                usesExtractor.extract(procedureNode,iast.getFirstChild(procedureNode),null);
                callsExtractor.extract(procedureNode,iast.getFirstChild(procedureNode));
                callsAstraExtractor.extract(procedureNode,new LinkedList<>(),iast.getFirstChild(procedureNode));
                nextExtractor.extract();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

        /*
        *   Usuwamy sztuczne procedury które powstały na skutek
        *   polecenia calls ponieważ jeżeli calls ma procedure która jeszcze nie jest w drzewie ast
        *   to tworzona jest sztuczna procedura wydmuszka ( widać w debugerze )
        *
         */
        public void removeFakeProcedures() throws Exception{
            Set<TNode> callTNodes =  iast.getNodesOfEntityTypes(EntityType.CALL);
            Set<TNode> procedureNodes = iast.getNodesOfEntityTypes(EntityType.PROCEDURE);
            //Podmianka fake procedures w TNode call na prawdziwe
            for(TNode callTNode: callTNodes){
                if(callTNode instanceof CallNode){
                    CallNode callNode = (CallNode) callTNode;
                    ASTNode procedureNode = callNode.getChild(0);
                    String fakeProcName = procedureNode.getAttr().getProcName();

                    ProcedureNode realProcedure = (procedureNodes.stream()
                            .map(procTNode -> (ProcedureNode) procTNode)
                            .filter(procNode -> procNode.getAttr().getLine() != -1)
                            .filter(procNode -> procNode.getAttr().getProcName().equals(fakeProcName))
                            .findFirst()
                            .orElseThrow(RuntimeException::new));

                    callNode.setNextChild(realProcedure);

                }else {
                    throw new RuntimeException("W AST-Entities-Call sa TNode'y inne niż typu Call! (instanceof)");
                }
            }
            //Usuwanie ProcedureNode w entities linkedhashmapi'e w AST
            iast.removeFakeProcedures();
        }


    }
