package org.example;

import org.example.Frontend.Tokenizer;

public class Main {

    public static void main(String[] args) {
        System.out.println("MAIN EMPTY");
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.getTokensFromFilename("SimpleFirst.txt");
    }
}