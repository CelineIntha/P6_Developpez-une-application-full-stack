package com.openclassrooms.mddapi.services;


import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsable de la gestion des thèmes (topics).
 */
@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    /**
     * Récupère la liste de tous les thèmes disponibles.
     *
     * @return La liste des thèmes enregistrés en base de données.
     */
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }
}