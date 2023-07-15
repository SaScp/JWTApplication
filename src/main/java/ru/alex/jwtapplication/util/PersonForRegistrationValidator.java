package ru.alex.jwtapplication.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alex.jwtapplication.models.Person;
import ru.alex.jwtapplication.services.PersonService;

@Component
public class PersonForRegistrationValidator implements Validator {
    private final PersonService personService;

    public PersonForRegistrationValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personService.findPersonByUsername(person.getUsername()).isPresent()){
            errors.rejectValue("username", "", "a user with the name already exists");
        }

    }
}
