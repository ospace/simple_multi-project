package com.tistory.ospace.simpleproject.exception;

public class SimpleProjectException extends RuntimeException {
    private static final long serialVersionUID = 2288443710582826194L;

    public final int status;
    
    public SimpleProjectException(int status, String message) {
        super(message);
        this.status = status;
    }
    
    public SimpleProjectException(int status, String message, Exception e) {
        super(message, e);
        this.status = status;
    }
}
