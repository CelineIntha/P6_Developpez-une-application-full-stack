package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.articles.ArticleDto;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    /**
     * Récupère la liste des articles triés du plus récent au plus ancien.
     */
    public List<Article> getAllArticles() {
        return articleRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Récupère un article
     */
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Article non trouvé avec l'ID : {}", id);
                    return new NotFoundException("Article non trouvé avec l'ID : " + id);
                });
    }

    /**
     * Créer un nouvel article
     */
    public Article createArticle(ArticleDto articleDto, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Utilisateur non trouvé pour la création d'un article : {}", username);
                    return new NotFoundException("Utilisateur non trouvé");
                });

        Topic topic = topicRepository.findById(articleDto.getTopicId())
                .orElseThrow(() -> {
                    logger.warn("Thème non trouvé pour la création d'un article, ID : {}", articleDto.getTopicId());
                    return new NotFoundException("Thème ou topic non trouvé");
                });


        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setAuthor(author);
        article.setTopic(topic);

        logger.info("Article créé avec succès : {}", article.getTitle());
        return articleRepository.save(article);
    }
}
