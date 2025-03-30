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
            List<Topic> initialTopics = List.of(
                    new Topic(null, "Java", "Java est un langage de programmation robuste et polyvalent, utilisé principalement pour développer des applications backend, mobiles et d'entreprise."),
                    new Topic(null, "Spring Boot", "Spring Boot est un framework Java qui simplifie la création d'applications web et microservices, en offrant une configuration minimale et des outils intégrés."),
                    new Topic(null, "UX Design", "Le UX Design consiste à concevoir des interfaces intuitives et agréables, en plaçant l'utilisateur au centre du processus de création."),
                    new Topic(null, "Angular", "Angular est un framework front-end basé sur TypeScript, idéal pour développer des applications web dynamiques, évolutives et performantes."),
                    new Topic(null, "Frontend", "Le développement Frontend regroupe les technologies et pratiques liées à l'interface utilisateur d'une application web ou mobile."),
                    new Topic(null, "Backend", "Le développement Backend concerne la logique métier, la gestion des bases de données et l'ensemble des fonctionnalités côté serveur d'une application.")
            );


            for (Topic topic : initialTopics) {
                if (topicRepository.findByName(topic.getName()).isEmpty()) {
                    topicRepository.save(topic);
                    logger.info("Topic '{}' ajouté à la base de données.", topic.getName());
                }
            }

        };
    }
}
