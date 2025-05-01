package org.example.Exceptions;

public class SolutionDoesNotExist extends Exception{
    public SolutionDoesNotExist(String message) {
        super(message);
    }
    public SolutionDoesNotExist() {
        this("No solution found");
    }
}
