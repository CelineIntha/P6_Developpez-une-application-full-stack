package com.openclassrooms.mddapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsable de la génération, validation et extraction d'informations des tokens JWT.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Récupère la clé de signature à partir de la clé secrète configurée.
     *
     * @return La clé cryptographique utilisée pour signer les tokens JWT.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrait l'identifiant de l'utilisateur (subject) à partir d'un token JWT.
     *
     * @param token Le token JWT.
     * @return L'identifiant de l'utilisateur (email ou username).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait une information des claims du token JWT.
     *
     * @param token Le token JWT.
     * @param claimsResolver Fonction permettant de récupérer un champ particulier des claims.
     * @param <T> Le type de l'information extraite.
     * @return La valeur extraite du token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait l'ensemble des claims contenus dans un token JWT.
     *
     * @param token Le token JWT.
     * @return Les claims du token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrait la date d'expiration d'un token JWT.
     *
     * @param token Le token JWT.
     * @return La date d'expiration du token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Vérifie si un token JWT est expiré.
     *
     * @param token Le token JWT.
     * @return true si le token est expiré, false sinon.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Génère un token JWT pour l'utilisateur authentifié.
     *
     * @param userDetails Les informations de l'utilisateur.
     * @return Le token JWT généré.
     */
    public String generateToken(UserDetails userDetails) {
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Vérifie si un token JWT est valide pour un utilisateur donné.
     *
     * @param token Le token JWT.
     * @param userDetails Les informations de l'utilisateur.
     * @return true si le token est valide, false sinon.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usernameOrEmail = extractUsername(token);
        return (usernameOrEmail.equals(userDetails.getUsername()) || usernameOrEmail.equals(((User) userDetails).getEmail()))
                && !isTokenExpired(token);
    }

    /**
     * Retourne la durée d'expiration du token en secondes.
     *
     * @return Durée d'expiration du token (en secondes).
     */
    public long getExpirationTime() {
        return jwtExpiration / 1000;
    }
}
