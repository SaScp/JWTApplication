package ru.alex.jwtapplication.services;

import org.springframework.stereotype.Service;
import ru.alex.jwtapplication.models.Password;
import ru.alex.jwtapplication.repository.PasswordRepository;
import ru.alex.jwtapplication.util.PasswordNotFoundException;

import java.util.Optional;
import java.util.Random;

@Service
public class PasswordService {
    public final String alf = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@#$%^&*!";
    private final PasswordRepository passwordRepository;

    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public void savePassword(Password password){
        passwordRepository.save(password);
    }

    public String generatePassword(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            stringBuilder.append(alf.charAt(new Random().nextInt(alf.length() - 1)));
        }
        return stringBuilder.toString();
    }
    public void deletePassword(String password){
      //TODO переделать метод
    }
}
