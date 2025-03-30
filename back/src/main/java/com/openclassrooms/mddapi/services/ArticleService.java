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

/**
 * Service chargé de la gestion des articles.
 */
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
     * Récupère les articles à afficher dans le fil d'actualité de l'utilisateur connecté.
     * Les articles retournés sont ceux :
     * <ul>
     *     <li>Créés par l'utilisateur</li>
     *     <li>Ou liés à un thème auquel l'utilisateur est abonné</li>
     * </ul>
     *
     * @param username Le nom d'utilisateur de l'utilisateur connecté.
     * @return Une liste d'articles correspondant au fil d'actualité personnalisé de l'utilisateur.
     * @throws NotFoundException si l'utilisateur n'existe pas.
     */
    public List<Article> getUserFeed(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));

        return articleRepository.findAllByAuthorOrSubscribedTopics(user.getId());
    }



    /**
     * Récupère un article spécifique à partir de son identifiant.
     *
     * @param id L'identifiant unique de l'article à récupérer.
     * @return L'article correspondant à l'identifiant fourni.
     * @throws NotFoundException si aucun article n'est trouvé avec l'identifiant donné.
     */
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Article non trouvé avec l'ID : {}", id);
                    return new NotFoundException("Article non trouvé avec l'ID : " + id);
                });
    }

    /**
     * Crée un nouvel article et l'enregistre en base de données.
     * L'article est associé à l'utilisateur authentifié et à un thème existant.
     *
     * @param articleDto Les données de l'article à créer (titre, contenu, ID du thème).
     * @param username Le nom d'utilisateur de l'auteur de l'article.
     * @return L'article nouvellement créé.
     * @throws NotFoundException si l'utilisateur ou le thème associé n'existe pas.
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
