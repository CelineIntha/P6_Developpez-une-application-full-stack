package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge un utilisateur par son email ou son username pour l'authentification.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.info("Tentative de chargement de l'utilisateur : {}", usernameOrEmail);

        return userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> {
                    logger.warn("Utilisateur non trouvé : {}", usernameOrEmail);
                    return new UsernameNotFoundException("Utilisateur non trouvé : " + usernameOrEmail);
                });
    }

    /**
     * Sauvegarde ou met à jour un utilisateur.
     */
    public void saveUser(User user) {
        logger.info("Sauvegarde de l'utilisateur : {}", user.getEmail());
        userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par son email.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'email : " + email));
    }

    /**
     * Recherche un utilisateur par son username.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
    }

    /**
     * Vérifie si un utilisateur existe par son email.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Vérifie si un utilisateur existe par son nom d'utilisateur.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
