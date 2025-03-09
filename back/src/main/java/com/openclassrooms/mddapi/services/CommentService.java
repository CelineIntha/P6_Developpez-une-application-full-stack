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
     * Ajouter un commentaire à un article.
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
