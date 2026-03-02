package com.CardMaster.exceptions.cias;
//NEEDED
public class InvalidCreditLimitException extends RuntimeException {
    public InvalidCreditLimitException() {
        super("Credit limit must be provided and positive");
    }
}
