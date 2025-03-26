package org.example.Frontend;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    Logger log = Logger.getLogger(Parser.class.getName());

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

    void getTokens(List<String> tokens_out) {
        tokens = tokens_out;
        tokenIterator = tokens.iterator();
        nextToken = tokenIterator.next();
        statementNumber = 0;
    }

    void checkToken(String token) {
        if (tokenIterator.hasNext()) {
            if (nextToken.equals(token)) {
                nextToken = tokenIterator.next();
            } else if (token.equals("INTEGER")) {
                try {
                    int test = Integer.parseInt(nextToken);
                } catch (NumberFormatException e) {

                    log.severe(e.getMessage());
                    log.throwing(Parser.class.getName(), "checkTokens", e);
                }
            } else if (token.equals("NAME")) {
                Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
                Matcher matcher = pattern.matcher(nextToken);
                if (!matcher.matches()) {
                    log.severe("String podany w linii " + statementNumber + " nie jest prawidłową nazwą.");
                }
            } else {
                log.severe("Nieprawidłowy token w linii" + statementNumber);
            }
        } else {
            log.severe("Za mało tokenów");
        }
    }
    //void program() {
        //TNode root, child;
        //root = PKB.createTNode("program");
        //PKB.setRoot(root);
        //while(nextToken.equals("procedure")){
            //child=procedure();
            //PKB.setParentChildLink(root,child);
        //}
    //}
    //TNode procedure(){
        //statementNumber++;
        //TNode stmtLst, procedure;
        //checkToken("procedure");
        //procedure=PKB.createTNode("procedure");
        //checkToken("NAME");
        //PKB.setAttr(procedure, nextToken);
        //PKB.setAttr(procedure, statementNumber);
        //nextToken = tokenIterator.next();
        //checkToken("{");
        //stmtLst=stmtLst();
        //PKB.setParentChildLink(procedure,stmtLst);
        //checkToken("}");
        //return procedure;
    //}
    //TNode stmtLst(){
        //TNode stmtLst, stmt;
        //stmtLst=PKB.createTNode("stmtLst");
        //while(!nextToken.equals("}")){
        //stmt=stmt();
        //PKB.setParentChildLink(stmtLst,stmt);
        //}
        //return stmtLst;
    //}
    //TNode stmt(){
        //statementNumber++;
        //TNode stmt;
        //if(NextToken.equals("while")){
        // stmt=while();
        // }
        //else{
        // stmt=assign();
        // }
    //}
    //TNode assign(){
        //TNode assign, leftVar, expr;
        //assign=PKB.createTNode("assign");
        //checkToken("NAME");
        //PKB.setAttr(assign, statementNumber);
        //leftVar=PKB.createTNode("var");
        //PKB.setAttr(leftVar, nextToken);
        //PKB.setAttr(leftVar, statementNumber);
        //nextToken=tokenIterator.next();
        //PKB.SetParentChildLink(assign,leftVar);
        //checkToken("=");
        //expr=expr();
        //PKB.SetParentChildLink(assign,expr);
        //PKB.setAttr(expr, statementNumber);
        //checkToken(";");
        //return assign;
    // }
    //TNode while(){
        //TNode while, checkVar, stmtList;
        //while=PKB.createTNode("while");
        //checkToken("NAME");
        //PKB.setAttr(while,statementNumber);
        //checkVar=PKB.createTNode("var");
        //PKB.setAttr(checkVar, nextToken);
        //PKB.setAttr(checkVar, statementNumber);
        //nextToken=tokenIterator.next();
        //PKB.SetParentChildLink(while,checkVar);
        //checkToken("{");
        //stmtList=stmtList();
        //PKB.SetParentChildLink(while,stmtList);
        //checkToken("}");
        //return while;
    //}
}
