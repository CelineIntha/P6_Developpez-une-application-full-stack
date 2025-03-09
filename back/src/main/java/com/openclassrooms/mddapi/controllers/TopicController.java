package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.topics.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.responses.TopicResponse;
import com.openclassrooms.mddapi.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
@Tag(name = "Thèmes", description = "Gestion des thèmes disponibles pour les abonnements")
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * Récupérer tous les thèmes disponibles.
     */
    @Operation(summary = "Récupérer tous les thèmes", description = "Retourne la liste de tous les thèmes disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des thèmes récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun thème trouvé")
    })
    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();

        if (topics.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<TopicResponse> topicResponses = topics.stream()
                .map(topic -> new TopicResponse(topic.getId(), topic.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(topicResponses);
    }


    /**
     * Créer un nouveau thème.
     */
    @Operation(summary = "Créer un thème", description = "Ajoute un nouveau thème à la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Thème créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@RequestBody TopicDto topicDto) {
        Topic newTopic = topicService.createTopic(topicDto);
        TopicResponse response = new TopicResponse(newTopic.getId(), newTopic.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
