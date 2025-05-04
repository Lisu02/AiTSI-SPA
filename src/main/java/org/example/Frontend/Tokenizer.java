package org.example.Frontend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Logger;

public class Tokenizer {

    private static final Logger log = Logger.getLogger(Tokenizer.class.getName());
    private static Tokenizer tokenizerInstance = null;
    private static Set<Character> SIGNS = new HashSet<>(Arrays.asList('+', '=', '*', ';'));

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
        boolean isCalls = false;
        while(fileScanner.hasNext()){
            word = fileScanner.next();
            if(word.length() > 1 && isGluedAssign(word) && !isCalls){  // sprawdzic czy poprzedni token to nie call
                List<String> slicedWords = sliceWord(word);
                tokenArrayList.addAll(slicedWords);
            } else if (isCalls && word.lastIndexOf(';') != -1) {
                tokenArrayList.add(word.replace(";",""));
                tokenArrayList.add(";");
            } else {
                tokenArrayList.add(word);
            }
            isCalls = false;
            if(word.equals("call")){
                isCalls = true;
                log.info("CALLS APPEARED \n");
            }

        }
        log.fine("Tokenizer list:\n");
        for (String token : tokenArrayList) {
            log.info(token + "\n");
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
            } else if (isOperator(c) && wordBuilder.length() > 0) {
                slicedWord.add(wordBuilder.toString().trim());

                slicedWord.add(Character.toString(c));
                wordBuilder.delete(0,wordBuilder.length());
            } else {
                slicedWord.add(Character.toString(c)); // Adding operator
                wordBuilder.delete(0, wordBuilder.length()); // Clearing
            }
//            if(!wordBuilder.isEmpty()){
//                slicedWord.add(wordBuilder.toString());
//            }
        }
        if(wordBuilder.length() > 0){

            slicedWord.add(wordBuilder.toString().trim());
            wordBuilder.delete(0,wordBuilder.length());
        }
        return slicedWord;
    }


    private boolean isOperator(Character sign){
//        return switch (sign) {
//            case '+', '=', '*', ';' -> true; //usuniecie srednika ';'
//            default -> false;
//        };
        return SIGNS.contains(sign);
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
