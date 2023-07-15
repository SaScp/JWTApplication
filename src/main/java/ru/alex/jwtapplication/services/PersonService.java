package ru.alex.jwtapplication.services;

import org.springframework.stereotype.Service;
import ru.alex.jwtapplication.models.Person;
import ru.alex.jwtapplication.repository.PersonRepository;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> findPersonByUsername(String username){
        return personRepository.findByUsername(username);
    }

    public Optional<Person> findPersonByPassword(String password){
        return personRepository.findByPassword(password);
    }
}
