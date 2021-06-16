package com.vignesh.demo.service;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(long id) {
        super(id+" does not exists");
    }
}
