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

/**
 * Service responsable de la gestion des utilisateurs et de l'authentification.
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge un utilisateur par son email ou son nom d'utilisateur pour l'authentification.
     *
     * @param usernameOrEmail L'email ou le nom d'utilisateur de l'utilisateur à charger.
     * @return Les détails de l'utilisateur.
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec cet identifiant.
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
     * Sauvegarde ou met à jour un utilisateur en base de données.
     *
     * @param user L'utilisateur à sauvegarder.
     */
    public void saveUser(User user) {
        logger.info("Sauvegarde de l'utilisateur : {}", user.getEmail());
        userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email L'email de l'utilisateur recherché.
     * @return L'utilisateur correspondant à l'email.
     * @throws NotFoundException si aucun utilisateur n'est trouvé avec cet email.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'email : " + email));
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur de l'utilisateur recherché.
     * @return L'utilisateur correspondant au nom d'utilisateur.
     * @throws NotFoundException si aucun utilisateur n'est trouvé avec ce nom.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
    }

    /**
     * Vérifie si un utilisateur existe avec l'email fourni.
     *
     * @param email L'email à vérifier.
     * @return true si un utilisateur existe avec cet email, false sinon.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Vérifie si un utilisateur existe avec le nom d'utilisateur fourni.
     *
     * @param username Le nom d'utilisateur à vérifier.
     * @return true si un utilisateur existe avec ce nom, false sinon.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
