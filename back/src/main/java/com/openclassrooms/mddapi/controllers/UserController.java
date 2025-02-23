package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.users.UpdateUserDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.responses.UserResponse;
import com.openclassrooms.mddapi.services.JwtService;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

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
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestHeader("Authorization") String authHeader) {
        if (userDetails == null || authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(new UserResponse(user.getId(), user.getEmail(), user.getUsername()));
    }

    /**
     * Permet à l'utilisateur de mettre à jour ses informations (email, username, mot de passe).
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody @Valid UpdateUserDto userDTO) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
        }

        String usernameOrEmail = userDetails.getUsername();
        System.out.println("Identifiant extrait du token : " + usernameOrEmail);

        User user = usernameOrEmail.contains("@") ? userService.findByEmail(usernameOrEmail) : userService.findByUsername(usernameOrEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }

        boolean emailChanged = false;

        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            if (userService.existsByUsername(userDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ce nom d'utilisateur est déjà pris.");
            }
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cet email est déjà utilisé.");
            }
            user.setEmail(userDTO.getEmail());
            emailChanged = true;
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updatedUser = userService.saveUser(user);

        String newToken = emailChanged ? jwtService.generateToken(updatedUser) : null;

        return ResponseEntity.ok()
                .header("Authorization", newToken != null ? "Bearer " + newToken : "")
                .body(Map.of(
                        "message", "Mise à jour réussie" + (emailChanged ? ". Utilisez le nouveau token pour vos prochaines requêtes." : ""),
                        "token", newToken
                ));
    }


}
