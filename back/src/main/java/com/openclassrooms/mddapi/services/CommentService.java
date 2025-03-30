package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.articles.CommentDto;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service chargé de la gestion des commentaires liés aux articles.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Ajoute un commentaire à un article existant.
     * Le commentaire est associé à l'utilisateur authentifié et à l'article ciblé.
     *
     * @param articleId  L'identifiant de l'article auquel le commentaire doit être ajouté.
     * @param commentDto Les données du commentaire à ajouter.
     * @param username   Le nom d'utilisateur de l'auteur du commentaire.
     * @return Le commentaire nouvellement créé et enregistré en base de données.
     * @throws NotFoundException si l'utilisateur ou l'article associé n'existe pas.
     */
    public Comment addComment(Long articleId, CommentDto commentDto, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> {
                    logger.warn("Tentative d'ajout de commentaire sur un article inexistant, ID : {}", articleId);
                    return new NotFoundException("Article non trouvé");
                });

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setAuthor(author);
        comment.setArticle(article);

        logger.info("Commentaire ajouté à l'article ID : {}", articleId);
        return commentRepository.save(comment);
    }
}
