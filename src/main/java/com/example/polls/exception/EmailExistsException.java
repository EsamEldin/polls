package com.example.polls.exception;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
public class EmailExistsException extends RuntimeException {
    
    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
