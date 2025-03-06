package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.articles.ArticleDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

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
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    /**
     * Créer un nouvel article
     */
    public Article createArticle(ArticleDto articleDto, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Topic topic = topicRepository.findById(articleDto.getTopicId())
                .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setAuthor(author);
        article.setTopic(topic);

        return articleRepository.save(article);
    }
}
