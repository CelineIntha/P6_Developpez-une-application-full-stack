package com.openclassrooms.mddapi.configuration;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Configuration
public class TopicInitializer {

    private static final Logger logger = LoggerFactory.getLogger(TopicInitializer.class);

    @Bean
    public CommandLineRunner initTopics(TopicRepository topicRepository) {
        return args -> {
            List<String> topics = List.of("Java", "Spring Boot", "UX Design", "Angular", "Frontend", "Backend");

            for (String topicName : topics) {
                if (topicRepository.findByName(topicName).isEmpty()) {
                    Topic topic = new Topic();
                    topic.setName(topicName);
                    topicRepository.save(topic);
                    logger.info("Topic '{}' ajouté à la base de données.", topicName);
                }
            }
        };
    }
}
