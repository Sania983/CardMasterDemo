package com.CardMaster.exceptions.paa;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super("Document not found with id: " + id);
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }
}
