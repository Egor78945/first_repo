package com.example.marketspring.exceptions;

public class BadEmailException extends Exception {
    public BadEmailException(String message) {
        super(message);
    }
}
