package org.example;

import org.example.Frontend.Tokenizer;

public class Main {

    public static void main(String[] args) {
        System.out.println("MAIN EMPTY");
        Tokenizer tokenizer = Tokenizer.getInstance();
        tokenizer.getTokensFromFilename("SimpleFirst.txt");

        // Bierzemy instancje tokenizera oraz parsera
        // bierzemy tokeny z pliku od użytkownika
        // przekazujemy tokeny do parsera
        // parser wywołuje buduje drzewo za pomoca interfejsu PKB
    }
}