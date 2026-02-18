package com.CardMaster.exceptions.paa;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer not found with id: " + id + ". Please create a new customer first.");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
