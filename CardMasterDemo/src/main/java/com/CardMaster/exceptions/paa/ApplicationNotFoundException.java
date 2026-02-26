package com.CardMaster.exceptions.paa;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(Long id) {
        super("Application not found with id: " + id);
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
