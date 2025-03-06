package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.articles.ArticleDto;
import com.openclassrooms.mddapi.dto.articles.CommentDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.responses.ArticleResponse;
import com.openclassrooms.mddapi.responses.CommentResponse;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    /**
     * Permet de récupérer tous les articles.
     */
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        List<ArticleResponse> articles = articleService.getAllArticles().stream()
                .map(article -> new ArticleResponse(
                        article.getId(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCreatedAt(),
                        article.getAuthor().getUsername(),
                        article.getTopic().getName(),
                        article.getComments().stream()
                                .map(comment -> new CommentResponse(
                                        comment.getId(),
                                        comment.getContent(),
                                        comment.getAuthor().getUsername(),
                                        comment.getCreatedAt()))
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());

        return ResponseEntity.ok(articles);
    }

    /**
     * Permet de créer un nouvel article.
     */
    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleDto articleDto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Article article = articleService.createArticle(articleDto, userDetails.getUsername());

        return ResponseEntity.ok(new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getAuthor().getUsername(),
                article.getTopic().getName(),
                List.of()
        ));
    }

    /**
     * Permet de récupérer un article par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(article -> ResponseEntity.ok(new ArticleResponse(
                        article.getId(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCreatedAt(),
                        article.getAuthor().getUsername(),
                        article.getTopic().getName(),
                        article.getComments().stream()
                                .map(comment -> new CommentResponse(
                                        comment.getId(),
                                        comment.getContent(),
                                        comment.getAuthor().getUsername(),
                                        comment.getCreatedAt()))
                                .collect(Collectors.toList())
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Permet d'ajouter un commentaire à un article.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id,
                                                      @RequestBody CommentDto commentDto,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.addComment(id, commentDto, userDetails.getUsername());

        return ResponseEntity.ok(new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt()
        ));
    }
}
