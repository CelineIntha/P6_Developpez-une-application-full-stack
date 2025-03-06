package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.auth.LoginUserDto;
import com.openclassrooms.mddapi.dto.auth.RegisterUserDto;
import com.openclassrooms.mddapi.exceptions.UnauthorizedException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.responses.LoginResponse;
import com.openclassrooms.mddapi.responses.SuccessResponse;
import com.openclassrooms.mddapi.services.JwtService;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        if (userService.existsByEmail(registerUserDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SuccessResponse(HttpStatus.BAD_REQUEST.value(), "Email déjà utilisé"));
        }

        if (userService.existsByUsername(registerUserDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SuccessResponse(HttpStatus.BAD_REQUEST.value(), "Nom d'utilisateur déjà pris"));
        }

        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(HttpStatus.CREATED.value(), "Inscription réussie"));
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {
            String usernameOrEmail = loginUserDto.getUsernameOrEmail();
            User user;

            if (usernameOrEmail.contains("@")) {
                user = userService.findByEmail(usernameOrEmail);
            } else {
                user = userService.findByUsername(usernameOrEmail);
            }

            if (user == null) {
                throw new UnauthorizedException("Nom d'utilisateur, email ou mot de passe incorrect");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), loginUserDto.getPassword())
            );

            UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
            String jwtToken = jwtService.generateToken(userDetails);
            long expiresIn = jwtService.getExpirationTime();

            LoginResponse loginResponse = new LoginResponse()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Connexion réussie")
                    .setToken(jwtToken)
                    .setExpiresIn(expiresIn)
                    .setTimestamp(Instant.now());

            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);

        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Nom d'utilisateur, email ou mot de passe incorrect");
        }
    }


}
