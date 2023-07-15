package ru.alex.jwtapplication.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.jwtapplication.models.Person;
import ru.alex.jwtapplication.repository.PersonRepository;

@Service
public class RegistrationAndLoginService {
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public RegistrationAndLoginService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    public void registration(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }
}
