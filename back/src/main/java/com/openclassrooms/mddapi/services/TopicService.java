package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.topics.TopicDto;
import com.openclassrooms.mddapi.exceptions.ConflictException;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    private TopicRepository topicRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Le topic avec l'ID " + id + " n'existe pas."));
    }

    public Topic createTopic(TopicDto topicDto) {
        if (topicRepository.findByName(topicDto.getName()).isPresent()) {
            throw new ConflictException("Le thème '" + topicDto.getName() + "' existe déjà.");
        }

        Topic topic = new Topic();
        topic.setName(topicDto.getName());

        logger.info("Nouveau thème créé : {}", topic.getName());
        return topicRepository.save(topic);
    }
}
