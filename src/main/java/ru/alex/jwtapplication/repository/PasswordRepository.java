package ru.alex.jwtapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.jwtapplication.models.Password;
import ru.alex.jwtapplication.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer> {
    Optional<Password> findByPassword(String password);

    List<Password> findPasswordByPerson(Person person);
}
