package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.topics.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.responses.SuccessResponse;
import com.openclassrooms.mddapi.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public ResponseEntity<?> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();

        if (topics.isEmpty()) {
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Aucun thème trouvé."));
        }

        return ResponseEntity.ok(topics);
    }

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDto topicDto) {
        Topic newTopic = topicService.createTopic(topicDto);
        return ResponseEntity.ok(newTopic);
    }
}
