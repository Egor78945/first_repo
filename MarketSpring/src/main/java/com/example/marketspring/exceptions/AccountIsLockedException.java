package com.example.marketspring.exceptions;

public class AccountIsLockedException extends Exception{
    public AccountIsLockedException(){
        super("Your account is locked.");
    }
}
