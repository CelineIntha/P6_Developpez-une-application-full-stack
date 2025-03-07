package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.subscriptions.SubscriptionDto;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.responses.SubscriptionResponse;
import com.openclassrooms.mddapi.responses.SuccessResponse;
import com.openclassrooms.mddapi.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Abonnements", description = "Gestion des abonnements")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Récupérer les abonnements de l'utilisateur connecté.
     */
    @GetMapping
    @Operation(summary = "Récupérer les abonnements de l'utilisateur", description = "Retourne la liste des thèmes auxquels l'utilisateur est abonné.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des abonnements récupérés avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT requis")
    })
    public ResponseEntity<?> getUserSubscriptions(@AuthenticationPrincipal UserDetails userDetails) {
        List<SubscriptionResponse> subscriptions = subscriptionService.getUserSubscriptions(userDetails.getUsername())
                .stream()
                .map(subscription -> new SubscriptionResponse(
                        subscription.getId(),
                        subscription.getTopic().getId(),
                        subscription.getTopic().getName()
                ))
                .collect(Collectors.toList());

        if (subscriptions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse(HttpStatus.OK.value(), "Aucun abonnement trouvé."));
        }

        return ResponseEntity.ok(subscriptions);
    }


    /**
     * S'abonner à un thème.
     */
    @Operation(summary = "S'abonner à un thème", description = "Permet à l'utilisateur de s'abonner à un thème spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Abonnement réussi"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT requis")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> subscribeToTopic(@RequestBody SubscriptionDto subscriptionDto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        Subscription subscription = subscriptionService.subscribeToTopic(subscriptionDto, userDetails.getUsername());

        SubscriptionResponse subscriptionResponse = new SubscriptionResponse(
                subscription.getId(),
                subscription.getTopic().getId(),
                subscription.getTopic().getName()
        );

        SuccessResponse response = new SuccessResponse(
                HttpStatus.CREATED.value(),
                "Abonnement réussi sur le thème " + subscriptionResponse.getTopicName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Se désabonner d’un thème.
     */
    @Operation(summary = "Se désabonner d'un thème", description = "Permet à l'utilisateur de se désabonner d'un thème spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Désabonnement réussi"),
            @ApiResponse(responseCode = "401", description = "Non authentifié - Token JWT requis"),
            @ApiResponse(responseCode = "404", description = "Thème non trouvé ou non abonné")
    })
    @DeleteMapping("/{topicId}")
    public ResponseEntity<SuccessResponse> unsubscribeFromTopic(@PathVariable Long topicId,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        subscriptionService.unsubscribeFromTopic(topicId, userDetails.getUsername());

        SuccessResponse response = new SuccessResponse(
                HttpStatus.OK.value(),
                "Désabonnement réussi du thème avec l'ID : " + topicId
        );

        return ResponseEntity.ok(response);
    }
}
