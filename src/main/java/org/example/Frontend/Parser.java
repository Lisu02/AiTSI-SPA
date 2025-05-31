package org.example.Frontend;

import org.example.Exceptions.ASTBuildException;
import org.example.PKB.API.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    Logger log = Logger.getLogger(Parser.class.getName());
    //IASTImplementationFrontend iast = new IASTImplementationFrontend();
    IAST iast = PKB.getAST();

    //Singleton
    private static Parser parserInstance = null;
    private Parser() {
    }
    public static Parser getInstance() {
        if (parserInstance == null) {
            parserInstance = new Parser();
        }
        return parserInstance;
    }


    private int statementNumber;
    private String nextToken;
    private List<String> tokens;
    private Iterator<String> tokenIterator;

    public void getTokens(List<String> tokens_out) {
        tokens = tokens_out;
        tokenIterator = tokens.iterator();
        nextToken = tokenIterator.next();
        statementNumber = -1;
        program();
    }

    public void checkToken(String token) {
        if (tokenIterator.hasNext()) {
            if (nextToken.equals(token)) {
                nextToken = tokenIterator.next();
            } else if (token.equals("INTEGER")) {
                checkIfTokenIsNumber(token);
            } else if (token.equals("NAME")) {
                checkIfTokenIsName(nextToken);
            } else {
                log.severe("Nieprawidłowy token w linii " + statementNumber +" oczekiwano "+token+", a otrzymano"+nextToken);
            }
        } else {
            log.info("Koniec tokenow - checkToken.hasNext()");
        }
    }


    private void checkIfTokenIsName(String nextToken){
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9#_]*");
        Pattern digits=Pattern.compile("[^1-9][^0-9]*");
        Matcher matcher = pattern.matcher(nextToken);
        Matcher matchDigit=digits.matcher(nextToken);
        if (!matcher.matches() && matchDigit.matches()) {
            log.severe("String "+ nextToken+" podany w linii " + statementNumber + " nie jest prawidłową nazwą.");
        }
    }

    private void checkIfTokenIsNumber(String token) {
        try{
            Integer.parseInt(token);
        } catch (NumberFormatException e) {
            log.severe(e.getMessage());
            log.throwing(Parser.class.getName(), "checkTokens", e);
        }
    }


    void program() {
        TNode root, child;
        root = iast.createTNode(EntityType.PROGRAM);
        iast.setRoot(root);
        statementNumber++;
        while(nextToken.equals("procedure")){
            child=procedure();
            try{
                iast.setParentChildLink(root,child);
            } catch (ASTBuildException e) {
                log.severe("Setting parent-child link failed: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    TNode procedure(){
        TNode stmtLst, procedure;
        checkToken("procedure");
        procedure=iast.createTNode(EntityType.PROCEDURE);
        checkToken("NAME");
        Attr at = new Attr();
        at.setLine(statementNumber+1);
        at.setProcName(nextToken);
        iast.setAttr(procedure, at);
        nextToken = tokenIterator.next();
        checkToken("{");
        stmtLst=stmtLst();
        try {
            iast.setParentChildLink(procedure,stmtLst);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        checkToken("}");
        return procedure;
    }

    TNode stmtLst(){
        TNode stmtLst, stmt;
        stmtLst=iast.createTNode(EntityType.STMTLIST);
        while(!nextToken.equals("}")){
        stmt=stmt();
        try {
            iast.setParentChildLink(stmtLst,stmt);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        }
        return stmtLst;
    }

    TNode stmt(){
        statementNumber++;
        switch (nextToken){
            case "while":
                return whileStmt();
            case "if":
                return ifStmt();
            case "call":
                return callStmt();
            default:
                return assignStmt();
        }
    }

    private TNode callStmt() {
        TNode proc;
        TNode call;
        checkToken("call");
        call= iast.createTNode(EntityType.CALL);
        checkToken("NAME");
        Attr at=new Attr();
        at.setLine(statementNumber);
        at.setProcName(nextToken);
        iast.setAttr(call,at);
        proc = iast.createTNode(EntityType.PROCEDURE);
        Attr atProc = new Attr();
        atProc.setLine(-1); //todo tu nie ma być -1 poźniej -Adrian 27.05.2025
        atProc.setProcName(nextToken);
        iast.setAttr(proc,atProc);
        try{
            iast.setParentChildLink(call,proc);
        } catch (ASTBuildException e) {
            System.out.println("PARSER 166 LINE OF CODE " + e.getMessage());
        }

        nextToken=tokenIterator.next();
        checkToken(";");
        return call;
    }

    private TNode ifStmt() {
        TNode ifNode, checkVar, ifStmt,elseStmt;
        checkToken("if");
        ifNode= iast.createTNode(EntityType.IF);
        checkToken("NAME");
        Attr at=new Attr();
        at.setLine(statementNumber);
        iast.setAttr(ifNode,at);
        at.setVarName(nextToken);
        checkVar=iast.createTNode(EntityType.VARIABLE);
        iast.setAttr(checkVar, at);
        nextToken=tokenIterator.next();
        try {
            iast.setParentChildLink(ifNode,checkVar);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        checkToken("then");
        checkToken("{");
        ifStmt=stmtLst();
        try {
            iast.setParentChildLink(ifNode,ifStmt);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        checkToken("}");
        checkToken("else");
        checkToken("{");
        elseStmt=stmtLst();
        try {
            iast.setParentChildLink(ifNode,elseStmt);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        checkToken("}");
        return ifNode;
    }

    /*
    *   checkToken zmiana arguentów
    *   Check token drobny refaktor
    *
    *
     */

    TNode whileStmt(){
        TNode whil, checkVar, stmtList;
        checkToken("while");
        whil=iast.createTNode(EntityType.WHILE);
        checkToken("NAME");
        Attr at=new Attr();
        at.setLine(statementNumber);
        iast.setAttr(whil,at);
        at.setVarName(nextToken);
        checkVar=iast.createTNode(EntityType.VARIABLE);
        iast.setAttr(checkVar, at);
        nextToken=tokenIterator.next();
        try {
            iast.setParentChildLink(whil,checkVar);
            checkToken("{");
            stmtList=stmtLst();
            iast.setParentChildLink(whil,stmtList);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        checkToken("}");
        return whil;
    }

    TNode assignStmt(){
        TNode assign, leftVar, expr;
        assign=iast.createTNode(EntityType.ASSIGN);
        checkToken("NAME"); // [zmienna] = 2 + 5 ;
        Attr at = Attr.builder()
                .line(statementNumber)
                .build();
        iast.setAttr(assign, at);
        try{
            int cons=Integer.parseInt(nextToken);
            leftVar=iast.createTNode(EntityType.CONSTANT);
            Attr as = Attr.builder()
                    .line(statementNumber)
                    .constantValue(cons)
                .build();
            iast.setAttr(leftVar,as);
        }catch(NumberFormatException e){
            leftVar=iast.createTNode(EntityType.VARIABLE);
            Attr as = Attr.builder()
                    .line(statementNumber)
                    .varName(nextToken)
                    .build();
            iast.setAttr(leftVar,as);
        }
        nextToken=tokenIterator.next();
        try {
            iast.setParentChildLink(assign,leftVar);
            checkToken("="); // zmienna [=] 2 + 5 ;
            expr=expr();
            iast.setParentChildLink(assign,expr);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        checkToken(";");
        return assign;
     }

    TNode expr() {
        Stack<String> operator = new Stack<>();
        Stack<TNode> values = new Stack<>();
        operator.push("First");

        if (!nextToken.equals("(")) {
            checkToken("NAME");
            try {
                int cons = Integer.parseInt(nextToken);
                TNode constant = iast.createTNode(EntityType.CONSTANT);
                Attr as = Attr.builder()
                        .line(statementNumber)
                        .constantValue(cons)
                        .build();
                iast.setAttr(constant, as);
                values.push(constant);
            } catch (NumberFormatException e) {
                TNode variable = iast.createTNode(EntityType.VARIABLE);
                Attr as = Attr.builder()
                        .line(statementNumber)
                        .varName(nextToken)
                        .build();
                iast.setAttr(variable, as);
                values.push(variable);
            }
            nextToken = tokenIterator.next();
        }

        log.info("next token przed whilem ze znakiem ';' -> " + nextToken);

        while (!Objects.equals(nextToken, ";") && !Objects.equals(nextToken, ")")) {
            switch (nextToken) {
                case "(":
                    nextToken = tokenIterator.next();
                    TNode par = expr();
                    values.push(par);
                    checkToken(")");
                    break;
                case "-":
                case "*":
                case "+":
                    while (!operator.peek().equals("First") &&
                            precedence(operator.peek()) >= precedence(nextToken)) {
                        applyOperator(values, operator.pop());
                    }
                    operator.push(nextToken);
                    nextToken = tokenIterator.next();
                    break;
                default:
                    checkToken("NAME");
                    try {
                        int cons = Integer.parseInt(nextToken);
                        TNode constant = iast.createTNode(EntityType.CONSTANT);
                        Attr as = Attr.builder()
                                .line(statementNumber)
                                .constantValue(cons)
                                .build();
                        iast.setAttr(constant, as);
                        values.push(constant);
                    } catch (NumberFormatException e) {
                        TNode variable = iast.createTNode(EntityType.VARIABLE);
                        Attr as = Attr.builder()
                                .line(statementNumber)
                                .varName(nextToken)
                                .build();
                        iast.setAttr(variable, as);
                        values.push(variable);
                    }
                    nextToken = tokenIterator.next();
                    break;
            }
        }

        while (!operator.peek().equals("First")) {
            applyOperator(values, operator.pop());
        }

        return values.isEmpty() ? null : values.pop();
    }

    private void applyOperator(Stack<TNode> values, String op) {
        TNode right = values.pop();
        TNode left = values.pop();
        TNode expr = null;

        switch (op) {
            case "+":
                expr = iast.createTNode(EntityType.PLUS);
                break;
            case "-":
                expr = iast.createTNode(EntityType.MINUS);
                break;
            case "*":
                expr = iast.createTNode(EntityType.TIMES);
                break;
        }

        try {
            iast.setParentChildLink(expr, left);
            iast.setParentChildLink(expr, right);
        } catch (ASTBuildException e) {
            log.severe("Setting parent-child link failed: " + e.getMessage());
            throw new RuntimeException(e);
        }

        values.push(expr);
    }

    private int precedence(String op) {
        switch (op) {
            case "*":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

}
