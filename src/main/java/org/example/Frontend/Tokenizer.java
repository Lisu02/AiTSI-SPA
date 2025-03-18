package org.example.Frontend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Tokenizer {

    Logger log = Logger.getLogger(Tokenizer.class.getName());

    public List<String> getTokensFromFilename(String filename) throws FileNotFoundException {
        Scanner fileScanner = getScanner(filename);

        String word;
        int statmentCounter = 1;
        ArrayList<String> tokenArrayList = new ArrayList<>();

        while(fileScanner.hasNext()){
            word = fileScanner.next();
            tokenArrayList.add(word);
            log.fine("Line: "+statmentCounter + " -> " + word);
        }
        return tokenArrayList;
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
