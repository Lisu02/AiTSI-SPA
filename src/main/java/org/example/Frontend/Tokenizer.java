package org.example.Frontend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Tokenizer {

    Logger log = Logger.getLogger(Tokenizer.class.getName());


    // Tokenizer a+b powinien wyciągnąć tokeny a , + , b;
    public List<String> getTokensFromFilename(String filename){
        Scanner fileScanner;
        try{
            fileScanner = getScanner(filename);
        } catch (FileNotFoundException e) {
            log.throwing(Tokenizer.class.getName(), "getTokensFromFilename", e);
            throw new RuntimeException(e);
        }

        String word;
        int statmentNumber = 1;
        ArrayList<String> tokenArrayList = new ArrayList<>();

        while(fileScanner.hasNext()){
            word = fileScanner.next();
            tokenArrayList.add(word);
            log.info("Line: "+statmentNumber + " -> " + word);
            if(isNewStatment(word)){statmentNumber++;}

            Iterator<String> iterator = tokenArrayList.iterator();
            while(iterator.hasNext()){
                iterator.next();
            }
        }
        return tokenArrayList;
    }

    private boolean isNewStatment(String word){
        return word.endsWith("{") || word.endsWith(";");
    }

    public Scanner getScanner(String filename) throws FileNotFoundException {
        Scanner fileScanner;
        try{
            fileScanner = new Scanner(new FileReader(filename));
        }catch (FileNotFoundException e){
            log.warning("Provided filename was not found during tokenization process - loading" +
                    " standard SimpleFirst.txt");
            fileScanner = new Scanner(new FileReader("SimpleFirst.txt"));
        }
        return fileScanner;
    }
}
