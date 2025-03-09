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
     * Récupérer tous les abonnements d'un utilisateur.
     */
    public List<Subscription> getUserSubscriptions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
        return subscriptionRepository.findByUser(user);
    }

    /**
     * S'abonner à un thème.
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
     * Se désabonner d’un thème.
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
