package ru.alex.jwtapplication.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alex.jwtapplication.dto.PasswordDTO;
import ru.alex.jwtapplication.dto.PersonDTO;
import ru.alex.jwtapplication.models.Password;
import ru.alex.jwtapplication.models.Person;
import ru.alex.jwtapplication.security.JWTUtil;
import ru.alex.jwtapplication.services.PasswordService;
import ru.alex.jwtapplication.services.PersonService;

import java.util.Map;

@RestController
@RequestMapping("/")
public class MainController {
    private final PasswordService passwordService;
    private final PersonService personService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    public MainController(PasswordService passwordService, PersonService personService, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.passwordService = passwordService;
        this.personService = personService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String,String>> generatePassword(@RequestHeader("Authorization") String token){
        String username = jwtUtil.convertJWT(token.substring(7));
        Person person = personService.findPersonByUsername(username).orElse(null);
        if(person != null){
           String password = passwordService.generatePassword();
            PasswordDTO passwordDTO = new PasswordDTO();
            passwordDTO.setPassword(password);
            passwordDTO.setPerson(person);
            passwordService.savePassword(convert(passwordDTO));
            return new ResponseEntity<>(Map.of("generate password", password, "user", person.getUsername()), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(Map.of("error", "error"), HttpStatus.BAD_GATEWAY);
    }
    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestBody String password){
        passwordService.deletePassword(password);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    private Password convert(PasswordDTO passwordDTO){
        return modelMapper.map(passwordDTO, Password.class);
    }
}
