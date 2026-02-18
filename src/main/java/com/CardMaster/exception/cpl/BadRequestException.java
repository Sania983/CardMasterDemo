package com.CardMaster.exception.cpl;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}