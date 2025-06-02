    package org.example.Frontend;

    import org.example.PKB.API.*;
    import org.example.PKB.Source.ASTImplementations.ASTModifies;
    import org.example.PKB.Source.ASTImplementations.ASTUses;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Stack;
    import java.util.logging.Logger;

    public class AbstractionExtractor {

        private static final Logger log = Logger.getLogger(AbstractionExtractor.class.getName());

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
            log.fine("currentNode = " + currentNode);
            procedureList.add(currentNode);
        }

        log.info(procedureList.toString());
        for(TNode procedureNode: procedureList){
            generateModifies(procedureNode,iast.getFirstChild(procedureNode));
            generateUsesPoprawka(procedureNode,iast.getFirstChild(procedureNode),null);
        }
    }

    //rekurencja 14 kwietnia update do IF-ELSE + switch case
    private void generateModifies(TNode tNodeProcedure,TNode stmtListTNode){
        TNode stmtNode = iast.getFirstChild(stmtListTNode);
        TNode variableNode;

        while(stmtNode != null){
            EntityType stmtType = iast.getType(stmtNode);
            switch (stmtType){
                case ASSIGN : {
                    variableNode = iast.getFirstChild(stmtNode);
                    log.fine("Modifies line "+iast.getAttr(stmtNode).getLine()+" : " + variableNode.toString().substring(43,62) + " and " + stmtNode.toString().substring(43,60));
                    iModifies.addModifies(tNodeProcedure,stmtNode,variableNode);
                    break;
                }
                case WHILE : {
                    TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode)); // variable(warunek) -> stmt
                    generateModifies(tNodeProcedure,tmp);// while -> stmtList do metody
                    break;
                }
                case IF: {
                    TNode tmp = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(stmtNode));
                    generateModifies(tNodeProcedure,tmp); // for if stmtList
                    tmp = iast.getLinkedNode(LinkType.RightSibling,tmp);
                    generateModifies(tNodeProcedure,tmp); // for else stmtList
                    break;
                }
            }
            stmtNode = iast.getLinkedNode(LinkType.RightSibling,stmtNode);
        }
    }

    //todo: mozna przebudowac na stack z tNodeProcedure
    public void generateUsesPoprawka(TNode tNodeProcedure,TNode stmtListTNode,Stack<TNode> tNodeStack){
            //
            // procedure -> *assign* -> while -> *assign* -> if -> if -> assign
        //Stos<TNode> lista stmt'ow czyli while -> if -> if .... itd dla danego assign
        //Jeżeli wychodzimy z danego stmtList to dajemy pop
        if(tNodeStack == null){
            tNodeStack = new Stack<>();
        }
        if(stmtListTNode == null){
            stmtListTNode = iast.getFirstChild(tNodeProcedure);
        }
        TNode currentTNode = iast.getFirstChild(stmtListTNode); //pierwszy tnode w stmtList
        while (currentTNode != null){
            EntityType currentType = iast.getType(currentTNode);
            switch (currentType){
                case ASSIGN: {
                    tNodeStack.push(currentTNode); //assign node
                    TNode leftVarNode = iast.getFirstChild(currentTNode);
                    leftVarNode = iast.getLinkedNode(LinkType.RightSibling,leftVarNode);
                    traverseAssignForUses(tNodeProcedure,leftVarNode,(Stack<TNode>) tNodeStack.clone());
                    tNodeStack.pop();
                    break;
                }
                case WHILE: {
                    tNodeStack.push(currentTNode);

                    TNode variableTNode = iast.getFirstChild(currentTNode);
                    iUses.setUses(tNodeProcedure,tNodeStack,variableTNode);

                    TNode whileStmtList = iast.getLinkedNode(LinkType.RightSibling,iast.getFirstChild(currentTNode));
                    generateUsesPoprawka(tNodeProcedure,whileStmtList,tNodeStack);
                    tNodeStack.pop(); //zdjęcie while ze stosu ponieważ idziemy dalej
                    break;
                }
                case IF:
                    log.warning("IF nie ma implementacji USES");
                    break;
                case CALL:
                    log.warning("CALL nie ma implementacji USES");
                    break;
            }
            currentTNode = iast.getLinkedNode(LinkType.RightSibling,currentTNode);
        }
    }
    //Problem tNodeProcedure czy wrzucic do tNodeStack + przechodzenie po nod'ach procedur i dodawanie kilka razy uses?
    //w sytuacji kiedy istnieje call możemy przejść kilka razy tą samą procedurę na innym callStack'u (tNodeStack)
    // good enough +
    //Przechodzenie po wszystkich nazwach TNode'ow aby dodac je do relacji uses
        /*
        * EntityType.CONSTANT nie jest uzywane nawet x = 2; [2] dostaje variable tnode oraz entityType.variable
        * Uses chyba zlicza również po zmienną po lewej stronie
        * */
    public void traverseAssignForUses(TNode tNodeProcedure,TNode leftVarNode,Stack<TNode> tNodeStack){
        TNode currentTNode = iast.getFirstChild(leftVarNode);
        if(currentTNode == null){
            if(iast.getType(leftVarNode) == EntityType.VARIABLE){
                try{
                    Integer.parseInt(iast.getAttr(leftVarNode).getVarName());
                }catch (NumberFormatException e) {
                    iUses.setUses(tNodeProcedure,tNodeStack,leftVarNode); // Adding right side variable node
                    log.info("Mam nazwe zmiennej nie liczba git!");
                }
            }
        }
        while (currentTNode != null){
            if(iast.getType(currentTNode) == EntityType.VARIABLE){
                try{
                    Integer.parseInt(iast.getAttr(currentTNode).getVarName());
                }catch (NumberFormatException e) {
                    iUses.setUses(tNodeProcedure,tNodeStack,currentTNode); // Adding right side variable node
                    log.info("Mam nazwe zmiennej nie liczba git!");
                }
            }else {
                traverseAssignForUses(tNodeProcedure,currentTNode,tNodeStack);
            }
            currentTNode = iast.getLinkedNode(LinkType.RightSibling,currentTNode);
        }
    }
}
