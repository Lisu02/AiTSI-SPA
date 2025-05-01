package Frontend;

import lombok.extern.java.Log;
import org.example.Frontend.*;
import org.example.Main;
import org.example.PKB.API.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AbstractionExtractorTest {

    @Mock
    IAST iast;

    @InjectMocks
    AbstractionExtractor ae;

    @BeforeAll
    public static void config() throws IOException {
        LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));
    }

    //Tutaj odbywa sie budowa drzewa AST
    @BeforeEach
    public void init(){

        ae = new AbstractionExtractor(iast);

        TNode rootNode = new TNodeImpl();
        //procedure nazwa {
        TNode procedureNode = new TNodeImpl();
        TNode procedureStmtListNode = new TNodeImpl();
        // x = 5;
        TNode assignNode = new TNodeImpl();
        TNode variableNode = new TNodeImpl();
        TNode constantNode = new TNodeImpl();
        // while x { z = 9; } }
        TNode whileNode = new TNodeImpl();
        TNode whileVariableNode = new TNodeImpl();
        TNode whileStmtListNode = new TNodeImpl();
        TNode assignNode2 = new TNodeImpl();
        TNode variableNode2 = new TNodeImpl();
        TNode constantNode2 = new TNodeImpl();

        // New procedure block
        TNode procedureNode2 = new TNodeImpl();
        TNode procedureStmtListNode2 = new TNodeImpl();

        TNode assignNode3 = new TNodeImpl();
        TNode constantNode3 = new TNodeImpl();
        TNode variableNode3 = new TNodeImpl();


        //Budowa wszystkich relacji pomiedzy TNode'ami todo: poprawic when'y

        when(iast.getRoot()).thenReturn(rootNode); // PROGRAM
        when(iast.getFirstChild(rootNode)).thenReturn(procedureNode); // PROC
        when(iast.getLinkedNode(LinkType.FirstChild,rootNode)).thenReturn(procedureNode);
        when(iast.getLinkedNode(LinkType.RightSibling,procedureNode)).thenReturn(procedureNode2); // PROC2

        when(iast.getFirstChild(procedureNode)).thenReturn(procedureStmtListNode);

        when(iast.getFirstChild(procedureStmtListNode)).thenReturn(assignNode); // ASSIGN
        when(iast.getFirstChild(assignNode)).thenReturn(variableNode); // VAR
        when(iast.getLinkedNode(LinkType.RightSibling,variableNode)).thenReturn(constantNode); // CONST

        when(iast.getLinkedNode(LinkType.RightSibling,assignNode)).thenReturn(whileNode); // WHILE
        when(iast.getFirstChild(whileNode)).thenReturn(whileVariableNode); // VAR
        when(iast.getLinkedNode(LinkType.RightSibling,whileVariableNode)).thenReturn(whileStmtListNode);
        when(iast.getFirstChild(whileStmtListNode)).thenReturn(assignNode2); // ASSIGN
        when(iast.getFirstChild(assignNode2)).thenReturn(variableNode2); // VAR
        when(iast.getLinkedNode(LinkType.RightSibling,variableNode2)).thenReturn(constantNode2); // CONST
        when(iast.getLinkedNode(LinkType.RightSibling,constantNode2)).thenReturn(null);
        when(iast.getLinkedNode(LinkType.RightSibling,assignNode2)).thenReturn(null);
        when(iast.getLinkedNode(LinkType.RightSibling,whileNode)).thenReturn(null);




        //PROC2 kontynuacja
        when(iast.getFirstChild(procedureNode2)).thenReturn(procedureStmtListNode2);
        when(iast.getFirstChild(procedureStmtListNode2)).thenReturn(assignNode3); //ASSIGMENT
        when(iast.getFirstChild(assignNode3)).thenReturn(variableNode3);  // VAR
        when(iast.getLinkedNode(LinkType.RightSibling,variableNode3)).thenReturn(constantNode3); // CONST
        when(iast.getLinkedNode(LinkType.RightSibling,constantNode3)).thenReturn(null);
        when(iast.getLinkedNode(LinkType.RightSibling,assignNode3)).thenReturn(null);
        when(iast.getLinkedNode(LinkType.RightSibling,procedureNode2)).thenReturn(null);

        when(iast.getLinkedNode(any(),isNull())).thenReturn(null); // musza byc matchery
        //when(iast.getFirstChild(null)).thenReturn(null);
    }

    @Test
    public void modifiesTest(){


        ae.generateStarterAbstractions();

        assertTrue(true); //asercja do zmiany!
    }

    @Test
    public void realModifiesTest(){
        Tokenizer tokenizer = Tokenizer.getInstance();
        Parser parser = Parser.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("ParserTest.txt");
        parser.getTokens(tokenList);

        AbstractionExtractor ae2 = new AbstractionExtractor();

        ae2.generateStarterAbstractions();

        assertTrue(true);
    }


    @Test
    public void astTest(){

        Tokenizer tokenizer = Tokenizer.getInstance();
        Parser parser = Parser.getInstance();

        List<String> tokenList = tokenizer.getTokensFromFilename("ParserTest.txt");
        parser.getTokens(tokenList);

        //test do debugowania trawersowania
        IAST iast1 = PKB.getAST();
        TNode root = iast1.getRoot();
        TNode procedure = iast1.getFirstChild(root);
        TNode mabycNull = iast1.getLinkedNode(LinkType.RightSibling,procedure);
        TNode stmtList = iast1.getFirstChild(procedure);
        TNode firts = iast1.getFirstChild(stmtList);
        TNode second = iast1.getLinkedNode(LinkType.RightSibling,firts);
        TNode third = iast1.getLinkedNode(LinkType.RightSibling,second);
        TNode fourth = iast1.getLinkedNode(LinkType.RightSibling,third);
        TNode fifth = iast1.getLinkedNode(LinkType.FirstChild,fourth);
        TNode sixth = iast1.getLinkedNode(LinkType.RightSibling,fifth);
        TNode seventh = iast1.getLinkedNode(LinkType.RightSibling,sixth);
        System.out.println(fourth);
    }
}
