package ru.alex.jwtapplication.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.alex.jwtapplication.services.PersonDetailsService;

import java.util.Optional;
@Component
public class AuthProvider implements AuthenticationProvider {
    private final PersonDetailsService personDetailsService;

    public AuthProvider(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = personDetailsService.loadUserByUsername(authentication.getName());
        if(!userDetails.getPassword().equals(authentication.getCredentials().toString())){
            throw new BadCredentialsException("Password is not correct");
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
