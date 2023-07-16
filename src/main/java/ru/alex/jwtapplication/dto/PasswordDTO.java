package ru.alex.jwtapplication.dto;

import ru.alex.jwtapplication.models.Person;

public class PasswordDTO {
    private String password;
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
