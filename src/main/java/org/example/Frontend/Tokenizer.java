package org.example.Frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Tokenizer {

    private static final Logger log = Logger.getLogger(Tokenizer.class.getName());
    private static Tokenizer tokenizerInstance = null;

    private Tokenizer() {}

    public static Tokenizer getInstance(){
        if (tokenizerInstance == null) {
            tokenizerInstance = new Tokenizer();
        }
        return tokenizerInstance;
    }

    public List<String> getTokensFromFilename(String filename){
        Scanner fileScanner;
        try{
            fileScanner = getScanner(filename);
        } catch (FileNotFoundException e) {
            log.throwing(Tokenizer.class.getName(), "getTokensFromFilename", e);
            throw new RuntimeException(e);
        }

        String word;
        int statmentNumber = 1; //todo: do rozwazenia liczenie statementow
        ArrayList<String> tokenArrayList = new ArrayList<>();

        while(fileScanner.hasNext()){
            word = fileScanner.next();
            if(word.length() > 1 && isGluedAssign(word)){
                List<String> slicedWords = sliceWord(word);
                tokenArrayList.addAll(slicedWords);
            }else {
                tokenArrayList.add(word);
            }

        }
        for (String token : tokenArrayList) {
            System.out.printf(token + "<-\n");
        }
        return tokenArrayList;
    }

    //Rozciecie slowa x=a+2+b; na x = a + 2 + b ; w ArrayList
    //Returns sliced word based on apperance of math operators during assignment
    public List<String> sliceWord(String word){
        List<String> slicedWord = new ArrayList<>();

        StringBuilder wordBuilder = new StringBuilder();
        for(Character c : word.toCharArray()){
            if(!isOperator(c)){
                wordBuilder.append(c);
            }else {
                slicedWord.add(Character.toString(c)); // Adding operator
                wordBuilder.delete(0, wordBuilder.length()); // Clearing
            }
            if(!wordBuilder.isEmpty()){
                slicedWord.add(wordBuilder.toString());
            }
        }
        return slicedWord;
    }


    private boolean isOperator(Character sign){
        return switch (sign) {
            case '+', '=', '*', ';' -> true;
            default -> false;
        };
    }

    //Sprawdzenie czy jest to assign
    private boolean isGluedAssign(String word){
        for(Character c : word.toCharArray()){
            if(isOperator(c)){
                return true;
            }
        }
        return false;
    }

    //todo: do zliczania stmt
    private boolean isNewStatment(String word){
        return word.endsWith("{") || word.endsWith(";");
    }

    public Scanner getScanner(String filename) throws FileNotFoundException {
        Scanner fileScanner;
        try{
            fileScanner = new Scanner(new FileReader(filename));
        }catch (FileNotFoundException e){
            log.severe("Provided filename was not found during tokenization process - loading" +
                    " standard SimpleFirst.txt");
            throw new FileNotFoundException(e.getMessage());
            //fileScanner = new Scanner(new FileReader("SimpleFirst.txt"));
        }
        return fileScanner;
    }
}
