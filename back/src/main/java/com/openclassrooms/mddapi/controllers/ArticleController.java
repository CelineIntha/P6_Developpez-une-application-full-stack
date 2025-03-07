package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.articles.ArticleDto;
import com.openclassrooms.mddapi.dto.articles.CommentDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.responses.ArticleResponse;
import com.openclassrooms.mddapi.responses.CommentResponse;
import com.openclassrooms.mddapi.responses.SuccessResponse;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
@SecurityRequirement(name = "bearerAuth")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    /**
     * Permet de récupérer tous les articles.
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les articles", description = "Retourne la liste de tous les articles disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des articles récupérée avec succès"),
            @ApiResponse(responseCode = "200", description = "Aucun article trouvé"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT manquant ou invalide"),
            @ApiResponse(responseCode = "403", description = "Accès interdit - Permissions insuffisantes")
    })
    public ResponseEntity<?> getAllArticles() {
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

        if (articles.isEmpty()) {
            return ResponseEntity.ok(new SuccessResponse(
                    HttpStatus.OK.value(),
                    "Aucun article trouvé."
            ));
        }

        return ResponseEntity.ok(articles);
    }



    /**
     * Permet de créer un nouvel article.
     */
    @Operation(summary = "Créer un article", description = "Ajoute un nouvel article à la base de données. Nécessite une authentification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non autorisé - Token JWT requis"),
            @ApiResponse(responseCode = "403", description = "Accès interdit - Permissions insuffisantes")
    })
    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleDto articleDto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Article article = articleService.createArticle(articleDto, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ArticleResponse(
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
    @Operation(summary = "Récupérer un article par ID", description = "Retourne un article spécifique basé sur son identifiant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article trouvé"),
            @ApiResponse(responseCode = "404", description = "Article non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non autorisé - Token JWT requis"),
            @ApiResponse(responseCode = "403", description = "Accès interdit - Permissions insuffisantes")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);

        ArticleResponse articleResponse = new ArticleResponse(
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
        );

        return ResponseEntity.ok(articleResponse);
    }

    /**
     * Permet d'ajouter un commentaire à un article.
     */
    @Operation(summary = "Ajouter un commentaire à un article", description = "Ajoute un commentaire sous un article spécifique. Nécessite une authentification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commentaire ajouté avec succès"),
            @ApiResponse(responseCode = "401", description = "Non autorisé - Token JWT requis"),
            @ApiResponse(responseCode = "404", description = "Article non trouvé")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<SuccessResponse> addComment(@PathVariable Long id,
                                                      @RequestBody CommentDto commentDto,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.addComment(id, commentDto, userDetails.getUsername());

        SuccessResponse response = new SuccessResponse(
                HttpStatus.CREATED.value(),
                "Commentaire ajouté à l'article avec l'ID: " + id
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
