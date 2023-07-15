package ru.alex.jwtapplication.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.alex.jwtapplication.security.JWTUtil;
import ru.alex.jwtapplication.services.PersonDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final PersonDetailsService personDetailsService;
    private final JWTUtil jwtUtil;

    public JWTFilter(PersonDetailsService personDetailsService, JWTUtil jwtUtil) {
        this.personDetailsService = personDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String JWTToken = request.getHeader("Authorization");
        if(JWTToken != null && !JWTToken.isBlank() && JWTToken.startsWith("Bearer ")){
            String token = JWTToken.substring(7);
            if(token.isBlank()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token with begin");
            }else {
                try {
                    String username = jwtUtil.convertJWT(token);
                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());
                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }catch (Exception e){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
