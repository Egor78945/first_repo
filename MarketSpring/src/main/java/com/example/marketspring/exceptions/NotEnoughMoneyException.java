package com.example.marketspring.exceptions;

public class NotEnoughMoneyException extends Exception{
    public NotEnoughMoneyException(String message){
        super(message);
    }
}
