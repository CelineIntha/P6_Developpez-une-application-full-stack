package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.topics.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;


    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public Topic createTopic(TopicDto topicDto) {
        if (topicRepository.findByName(topicDto.getName()).isPresent()) {
            throw new RuntimeException("Ce thème existe déjà.");
        }

        Topic topic = new Topic();
        topic.setName(topicDto.getName());

        return topicRepository.save(topic);
    }
}
