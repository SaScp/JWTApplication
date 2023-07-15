package ru.alex.jwtapplication.util;

public class PersonNotCreatedException extends RuntimeException{

    public PersonNotCreatedException(String message){
        super(message);
    }
}
