package ru.alex.jwtapplication.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alex.jwtapplication.dto.AutherizationDTO;
import ru.alex.jwtapplication.dto.PersonDTO;
import ru.alex.jwtapplication.models.Person;
import ru.alex.jwtapplication.security.JWTUtil;
import ru.alex.jwtapplication.services.RegistrationAndLoginService;
import ru.alex.jwtapplication.util.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ModelMapper modelMapper;
    private final RegistrationAndLoginService registrationService;
    private final JWTUtil jwtUtil;
    private final PersonForRegistrationValidator personValidatior;

    private final AuthenticationManager authenticationManager;

    public AuthController(ModelMapper modelMapper, RegistrationAndLoginService registrationService, JWTUtil jwtUtil, PersonForRegistrationValidator personValidatior, AuthenticationManager authenticationManager) {
        this.modelMapper = modelMapper;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.personValidatior = personValidatior;

        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid PersonDTO personDTO,
                                                           BindingResult bindingResult){
        Person person = convert(personDTO);
        personValidatior.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder stringBuilder = new StringBuilder();
            for (var error : fieldErrors){
                stringBuilder.append(error.getDefaultMessage());
            }
            throw new PersonNotCreatedException(stringBuilder.toString());
        }
        registrationService.registration(person);

        String token = jwtUtil.generateToken(person.getUsername());
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AutherizationDTO autherizationDTO,
                                                     BindingResult bindingResult){
        Person person = convert(autherizationDTO);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(autherizationDTO.getUsername(), autherizationDTO.getPassword());
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e){
            throw new UsernameNotFoundException("User Not Found");
        }



        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder stringBuilder = new StringBuilder();
            for (var error : fieldErrors){
                stringBuilder.append(error.getDefaultMessage());
            }
            throw new PersonNotLoginException(stringBuilder.toString());
        }

        String token = jwtUtil.generateToken(autherizationDTO.getUsername());

        return new ResponseEntity<>(Map.of("new token", token), HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> ExHandler(PersonNotCreatedException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> ExHandler(PersonNotLoginException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    public Person convert(PersonDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }
    public Person convert(AutherizationDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }
}
