package org.example.Frontend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ParserExperimental {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner consoleScaner = new Scanner(System.in);
        Scanner fileScanner = new Scanner(new FileReader("SimpleFirst.txt"));

        while(fileScanner.hasNext()){
            if(fileScanner.hasNextByte()){
                System.out.println(fileScanner.nextByte());
            }else {
                fileScanner.next();
            }
        }
        //String pierwszeSlowo = fileScanner.nextLine();

        //System.out.println(pierwszeSlowo);
        //System.out.println(sign);

    }
}
