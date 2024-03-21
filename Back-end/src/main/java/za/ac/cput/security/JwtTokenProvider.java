package za.ac.cput.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import za.ac.cput.exception.JwtAuthenticationException;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    public String createToken(String username, Set<Role> roles) {
        log.info("Creating JWT token for user: {}", username);
        // Fetch user details including roles and permissions
        User user = (User) userDetailsService.loadUserByUsername(username);


        // Log the roles and their permissions
        log.info("Roles and permissions:");
        roles.forEach(role -> log.info("Role: {}, Permissions: {}", role.getName(), role.getPermissions()));

        // Extract roles and permissions from Role objects
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> permissions = roles.stream()
                .flatMap(role -> role.getPermissions() == null ? Stream.empty() : role.getPermissions().stream()) // Handle null permissions
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        String token = Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSignInKey())
                .claim("roles", roleNames) // Add roles to the token claim
                .claim("permissions", user.getRoles()) // Add permissions to the token claim
                .compact();
        log.info("Generated JWT token: {}", token);
        return token;
    }

    private SecretKey getSignInKey() {
        String SECRET_KEY = "339e342a830a1526094d805b60c422e15bb4bf0f8797f2d99846f381ac6566b8";
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        log.info("Retrieved UserDetails for user: {}", userDetails.getUsername());
        log.debug("Retrieved password for user: {}", userDetails.getPassword());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        String username;
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Error decoding username from JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("Error decoding JWT token", e, HttpStatus.UNAUTHORIZED);
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails user) {
        try {
            String username = getUsername(token);
            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

}

