package com.CardMaster.exceptions.cias;
//NEEDED
public class CardAccountNotFoundException extends RuntimeException {
    public CardAccountNotFoundException(Long id) {
        super("Card account not found with ID: " + id);
    }
}
