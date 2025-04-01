package org.example;

import org.example.Frontend.AbstractionExtractor;
import org.example.Frontend.Parser;
import org.example.Frontend.Tokenizer;

public class Main {

    public static void main(String[] args) {
        System.out.println("MAIN EMPTY");
        Tokenizer tokenizer = Tokenizer.getInstance();

        Parser parser = Parser.getInstance();

        parser.getTokens(tokenizer.getTokensFromFilename("ParserTest.txt"));

        AbstractionExtractor ae = new AbstractionExtractor();

        ae.generateStarterAbstractions();

        // Bierzemy instancje tokenizera oraz parsera
        // bierzemy tokeny z pliku od użytkownika
        // przekazujemy tokeny do parsera
        // parser wywołuje buduje drzewo za pomoca interfejsu PKB
    }
}