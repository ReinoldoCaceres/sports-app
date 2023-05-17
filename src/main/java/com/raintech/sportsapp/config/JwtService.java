package com.raintech.sportsapp.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // The secret key used to sign the JWTs
    private static final String SECRET_KEY = "3373367638792F423F4528482B4D6251655468576D5A7134743777217A244326";

    // Retrieves the signing key from the secret key string
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decode the secret key string from base64 to bytes
        return Keys.hmacShaKeyFor(keyBytes); // Generate a signing key using the decoded bytes
    }

    // Extracts all the claims from the JWT token
    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder() // Create a new parser builder instance
                .setSigningKey(getSigninKey()) // Set the signing key for the parser to use
                .build() // Build the parser
                .parseClaimsJws(jwtToken) // Parse the JWT token and return a Jws<Claims> object
                .getBody(); // Extract the Claims object from the Jws<Claims> object
    }

    // Extracts a specific claim from the JWT token using a claims resolver function
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken); // Extract all the claims from the JWT token
        return claimsResolver.apply(claims); // Use the claims resolver function to extract the desired claim from the Claims object
    }

    // Extracts the username from the JWT token
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject); // Use the extractClaim method to extract the username from the JWT token
    }

    // Generates a JWT token with the provided user details and additional claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        long nowMillis = System.currentTimeMillis(); // Get the current time in milliseconds
        Date issuedAt = new Date(nowMillis); // Create a new Date object representing the current time
        Date expiration = new Date(nowMillis + 1000 * 60 * 24); // Create a new Date object representing the expiration time of the JWT token
        Key signingKey = getSigninKey(); // Get the signing key
        JwtBuilder builder = Jwts.builder() // Create a new JwtBuilder instance
                .setClaims(extraClaims) // Set the additional claims for the JWT token
                .setSubject(userDetails.getUsername()) // Set the username as the subject of the JWT token
                .setIssuedAt(issuedAt) // Set the issue time of the JWT token
                .setExpiration(expiration) // Set the expiration time of the JWT token
                .signWith(signingKey, SignatureAlgorithm.HS256); // Set the signing key and algorithm for the JWT token

        return builder.compact(); // Build the JWT token and return it as a compact string
    }

    // Generates a JWT token with the provided user details and no additional claims
    public String generateToken(UserDetails userDetails) {
        // Calls the overloaded generateToken method with an empty map of additional claims
        return generateToken(new HashMap<>(), userDetails);
    }

    // Checks if the provided JWT token is valid for the given user details
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Extracts the username from the provided JWT token
        final String username = extractUsername(token);
        // Compares the extracted username with the username in the provided user details,
        // and checks if the token has not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Checks if the provided JWT token has expired
    private boolean isTokenExpired(String token) {
        // Extracts the expiration date from the JWT token and compares it with the current date
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration date from the JWT token
    private Date extractExpiration(String token) {
        // Calls the extractClaim method with a Claims resolver function that extracts the expiration date
        // from the JWT claims and returns it as a Date object
        return extractClaim(token, Claims::getExpiration);
    }

}
