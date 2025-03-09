package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.users.UpdateUserDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
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
        if (user == null) {
            logger.warn("Utilisateur non trouvé avec l'email: {}", email);
            throw new NotFoundException("Utilisateur non trouvé.");
        }

        List<TopicResponse> subscribedTopics = user.getSubscriptions().stream()
                .map(subscription -> new TopicResponse(subscription.getTopic().getId(), subscription.getTopic().getName()))
                .toList();

        logger.info("Utilisateur {} trouvé avec {} abonnements", user.getUsername(), subscribedTopics.size());

        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                subscribedTopics
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    /**
     * Permet à l'utilisateur de mettre à jour ses informations (email, username, mot de passe).
     */
    @PutMapping("/update")
    @Operation(summary = "Mettre à jour les informations de l'utilisateur",
            description = "Permet à l'utilisateur de modifier son email, son nom d'utilisateur ou son mot de passe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT requis"),
            @ApiResponse(responseCode = "409", description = "Email ou nom d'utilisateur déjà pris"),
    })
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody @Valid UpdateUserDto userDTO) {
        try {
            if (userDetails == null) {
                throw new UnauthorizedException("Utilisateur non authentifié.");
            }

            String usernameOrEmail = userDetails.getUsername();
            logger.info("Identifiant extrait du token : {}", usernameOrEmail);

            User user = usernameOrEmail.contains("@") ? userService.findByEmail(usernameOrEmail) : userService.findByUsername(usernameOrEmail);

            if (user == null) {
                throw new NotFoundException("Utilisateur non trouvé.");
            }

            boolean emailChanged = false;

            if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
                if (userService.existsByUsername(userDTO.getUsername())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            new ErrorResponse(HttpStatus.CONFLICT.value(), "Ce nom d'utilisateur est déjà pris.", LocalDateTime.now())
                    );
                }
                user.setUsername(userDTO.getUsername());
            }

            if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
                if (userService.existsByEmail(userDTO.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            new ErrorResponse(HttpStatus.CONFLICT.value(), "Cet email est déjà utilisé.", LocalDateTime.now())
                    );
                }
                user.setEmail(userDTO.getEmail());
                emailChanged = true;
            }

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            userService.saveUser(user);

            String newToken = emailChanged ? jwtService.generateToken(user) : null;

            return ResponseEntity.ok(
                    new UpdateUserResponse(
                            HttpStatus.OK.value(),
                            "Mise à jour réussie" + (emailChanged ? ". Utilisez le nouveau token pour vos prochaines requêtes." : ""),
                            newToken
                    )
            );

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Une erreur interne est survenue.", LocalDateTime.now())
            );
        }
    }


}
