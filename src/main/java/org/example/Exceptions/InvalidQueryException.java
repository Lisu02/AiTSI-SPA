package org.example.Exceptions;

public class InvalidQueryException extends Exception{
    public InvalidQueryException(String message) {
        super(message);
    }
    public InvalidQueryException() {
        this("Invalid query");
    }
}
