package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.subscriptions.SubscriptionDto;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.exceptions.ConflictException;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsable de la gestion des abonnements aux thèmes par les utilisateurs.
 */
@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    /**
     * Récupère la liste des abonnements d'un utilisateur.
     *
     * @param username Le nom d'utilisateur.
     * @return La liste des abonnements de l'utilisateur.
     * @throws NotFoundException si l'utilisateur n'existe pas.
     */
    public List<Subscription> getUserSubscriptions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
        return subscriptionRepository.findByUser(user);
    }

    /**
     * Permet à un utilisateur de s'abonner à un thème.
     *
     * @param subscriptionDto Les informations concernant l'abonnement (ID du thème).
     * @param username        Le nom d'utilisateur.
     * @return L'abonnement nouvellement créé.
     * @throws NotFoundException si l'utilisateur ou le thème n'existe pas.
     * @throws ConflictException si l'utilisateur est déjà abonné au thème.
     */
    public Subscription subscribeToTopic(SubscriptionDto subscriptionDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));

        Topic topic = topicRepository.findById(subscriptionDto.getTopicId())
                .orElseThrow(() -> new NotFoundException("Le thème avec l'ID " + subscriptionDto.getTopicId() + " n'existe pas."));

        if (subscriptionRepository.findByUserAndTopic(user, topic).isPresent()) {
            throw new ConflictException("L'utilisateur est déjà abonné au thème '" + topic.getName() + "'.");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTopic(topic);

        return subscriptionRepository.save(subscription);
    }

    /**
     * Permet à un utilisateur de se désabonner d’un thème.
     *
     * @param topicId  L'identifiant du thème à désabonner.
     * @param username Le nom d'utilisateur.
     * @throws NotFoundException si l'utilisateur, le thème ou l'abonnement n'existe pas.
     */
    public void unsubscribeFromTopic(Long topicId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Tentative de désabonnement d'un utilisateur non trouvé : {}", username);
                    return new NotFoundException("Utilisateur non trouvé");
                });

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> {
                    logger.warn("Tentative de désabonnement d'un thème inexistant, ID : {}", topicId);
                    return new NotFoundException("Le thème avec l'ID " + topicId + " n'existe pas.");
                });

        Subscription subscription = subscriptionRepository.findByUserAndTopic(user, topic)
                .orElseThrow(() -> {
                    logger.warn("Tentative de désabonnement d'un thème auquel l'utilisateur {} n'est pas abonné, ID : {}", username, topicId);
                    return new NotFoundException("L'utilisateur n'est pas abonné à ce thème.");
                });

        subscriptionRepository.delete(subscription);
        logger.info("Utilisateur {} désabonné du thème avec ID : {}", username, topicId);
    }
}

