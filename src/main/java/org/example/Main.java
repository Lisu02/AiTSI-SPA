package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello, World!");
        Scanner consoleScaner = new Scanner(System.in);
        Scanner fileScanner = new Scanner(new FileReader("SimpleFirst.txt"));
        String userWord = consoleScaner.nextLine();

        String[] wordArray = userWord.split(" ");
        for (String word : wordArray) {
            getWord(word);
        }
        String word;
        int counter = 1;
        while(fileScanner.hasNextLine()){
            word = fileScanner.nextLine();
            System.out.println("Line: "+counter + " -> " + word);
            byte[] wordBytes = word.getBytes(StandardCharsets.UTF_8);
            System.out.println(wordBytes[0] +"-" + wordBytes[6]);
            if(wordBytes[wordBytes.length-1] == 10) {counter++;}
        }
        System.out.println("User word: " + userWord);
    }
    private static void getWord(String wordPart){
        switch (wordPart){
            case "SELECT" -> System.out.println("selekcik");
            case "FROM" -> System.out.println("Frommm");
        }
    }
}