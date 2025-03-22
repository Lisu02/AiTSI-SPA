package org.example.Exceptions;

public class NotImplementedRuntimeException extends RuntimeException {
    public NotImplementedRuntimeException()
    {
        super("Not implemented!");
    }
    public NotImplementedRuntimeException(String module, String error)
    {
        super("Module: " + module + ", Error: " + error);
    }
}
