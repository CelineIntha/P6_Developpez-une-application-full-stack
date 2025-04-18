package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.users.UpdateUserDto;
import com.openclassrooms.mddapi.exceptions.ConflictException;
import com.openclassrooms.mddapi.exceptions.UnauthorizedException;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.responses.*;
import com.openclassrooms.mddapi.services.JwtService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "Gestion d'un compte utilisateur et de ses informations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les informations de l'utilisateur connecté.
     */
    @GetMapping("/me")
    @Operation(summary = "Récupérer les informations de l'utilisateur connecté",
            description = "Retourne les informations de l'utilisateur actuellement authentifié, y compris ses abonnements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informations utilisateur récupérées avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT requis"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("Requête reçue sur /me");

        if (userDetails == null) {
            logger.warn("Utilisateur non authentifié");
            throw new UnauthorizedException("Utilisateur non authentifié.");
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Token non valide ou absent");
            throw new UnauthorizedException("Token invalide ou absent.");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        logger.info("Email extrait du token: {}", email);

        User user = userService.findByEmail(email);

        List<TopicResponse> subscribedTopics = user.getSubscriptions().stream()
                .map(subscription -> {
                    var topic = subscription.getTopic();
                    return new TopicResponse(topic.getId(), topic.getName(), topic.getDescription());
                })
                .toList();

        logger.info("Utilisateur {} trouvé avec {} abonnements", user.getUsername(), subscribedTopics.size());

        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                subscribedTopics
        );

        return ResponseEntity.ok(response);
    }


    /**
     * Permet à l'utilisateur de mettre à jour ses informations (email, username, mot de passe).
     */
    @Operation(summary = "Mettre à jour les informations de l'utilisateur", description = "Permet à l'utilisateur de modifier son email, son nom d'utilisateur ou son mot de passe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie (si l'email change, retourne un nouveau token)"),
            @ApiResponse(responseCode = "204", description = "Mise à jour réussie (aucun contenu retourné)"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT requis"),
            @ApiResponse(responseCode = "409", description = "Email ou nom d'utilisateur déjà pris"),
    })
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody @Valid UpdateUserDto userDTO) {

        if (userDetails == null) {
            throw new UnauthorizedException("Utilisateur non authentifié.");
        }

        String usernameOrEmail = userDetails.getUsername();
        logger.info("Identifiant extrait du token : {}", usernameOrEmail);

        User user = usernameOrEmail.contains("@")
                ? userService.findByEmail(usernameOrEmail)
                : userService.findByUsername(usernameOrEmail);

        if (user == null) {
            throw new NotFoundException("Utilisateur non trouvé.");
        }

        boolean emailChanged = false;

        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            if (userService.existsByUsername(userDTO.getUsername())) {
                throw new ConflictException("Ce nom d'utilisateur est déjà pris.");
            }
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userService.existsByEmail(userDTO.getEmail())) {
                throw new ConflictException("Cet email est déjà utilisé.");
            }
            user.setEmail(userDTO.getEmail());
            emailChanged = true;
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userService.saveUser(user);

        if (emailChanged) {
            String newToken = jwtService.generateToken(user);
            return ResponseEntity.ok(new UpdateUserResponse(newToken));
        }

        return ResponseEntity.noContent().build();
    }



}
