package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.auth.LoginUserDto;
import com.openclassrooms.mddapi.dto.auth.RegisterUserDto;
import com.openclassrooms.mddapi.exceptions.ConflictException;
import com.openclassrooms.mddapi.exceptions.UnauthorizedException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.responses.LoginResponse;
import com.openclassrooms.mddapi.responses.UserResponse;
import com.openclassrooms.mddapi.services.JwtService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "Authentification", description = "Gestion de l'inscription, de la connexion et de l'authentification des utilisateurs")
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

    @Operation(summary = "Inscription d'un utilisateur", description = "Permet à un utilisateur de créer un compte.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscription réussie"),
            @ApiResponse(responseCode = "400", description = "Email ou nom d'utilisateur déjà utilisé, ou mot de passe non conforme")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        if (userService.existsByEmail(registerUserDto.getEmail())) {
            throw new ConflictException("Email déjà utilisé");
        }

        if (userService.existsByUsername(registerUserDto.getUsername())) {
            throw new ConflictException("Nom d'utilisateur déjà pris");
        }

        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        userService.saveUser(user);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    /**
     * Permet d'authentifier un utilisateur et d'obtenir un token JWT.
     */
    @Operation(summary = "Connexion d'un utilisateur", description = "Permet à un utilisateur de s'authentifier avec son email ou nom d'utilisateur et son mot de passe. Retourne un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Nom d'utilisateur, email ou mot de passe incorrect")
    })
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
                    .setToken(jwtToken)
                    .setExpiresIn(expiresIn)
                    .setTimestamp(Instant.now());

            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);

        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Nom d'utilisateur, email ou mot de passe incorrect");
        }
    }
}
