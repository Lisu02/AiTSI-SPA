package org.example.Frontend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Logger;

public class Tokenizer {

    private static final Logger log = Logger.getLogger(Tokenizer.class.getName());
    private static Tokenizer tokenizerInstance = null;
    private static final Set<Character> SIGNS = new HashSet<>(
            Arrays.asList('(', ')', '{', '}', '[', ']', ';', ',', '=', '+', '-', '*', '/', '<', '>', '!', '&', '|')
    );

    private Tokenizer() {}

    public static Tokenizer getInstance(){
        if (tokenizerInstance == null) {
            tokenizerInstance = new Tokenizer();
        }
        return tokenizerInstance;
    }

    public List<String> getTokensFromFilename(String filename){
        List<String> tokenArrayList = new ArrayList<>();
        try (Scanner scanner = getScanner(filename)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                tokenArrayList.addAll(tokenizeLine(line));
            }
        } catch (FileNotFoundException e) {
            log.throwing(Tokenizer.class.getName(), "getTokensFromFilename", e);
            throw new RuntimeException(e);
        }

        log.fine("Tokenizer list:\n");
        for (String token : tokenArrayList) {
            log.info(token + "\n");
        }

        return tokenArrayList;
    }

    private List<String> tokenizeLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (Character.isWhitespace(c)) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else if (SIGNS.contains(c)) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(Character.toString(c));
            } else if (Character.isLetterOrDigit(c) || c == '_') {
                current.append(c);
            } else {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(Character.toString(c)); // for any other punctuation
            }
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens;
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
