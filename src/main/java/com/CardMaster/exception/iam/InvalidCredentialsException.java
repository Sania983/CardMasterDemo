package com.CardMaster.exception.iam;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid credentials provided");
    }
}
