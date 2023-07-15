package ru.alex.jwtapplication.util;

public class PersonNotLoginException extends RuntimeException{

    public PersonNotLoginException(String message){
        super(message);
    }
}
