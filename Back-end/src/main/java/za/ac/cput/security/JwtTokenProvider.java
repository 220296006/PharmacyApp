package za.ac.cput.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}") // Use environment variable for secret key
    private String secretKey;

    @Autowired
    private UserDetailsService userDetailsService;

    public String createToken(String username, Set<Role> roles) {
        log.info("Creating JWT token for user: {}", username);
        // Fetch user details including roles and permissions
        User user = (User) userDetailsService.loadUserByUsername(username);
        // Log the roles and their permissions
        log.info("Roles and permissions:");
        roles.forEach(role -> log.info("Role: {}, Permissions: {}", role.getName(), user.getRoles()));
        // Extract roles and permissions from Role objects
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
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
        return token.trim();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token.trim())
                .getPayload();
        } catch (JwtException e) {
            log.error("Error parsing JWT token claims: {}", e.getMessage());
            throw new JwtAuthenticationException("Error parsing JWT token claims", e, HttpStatus.UNAUTHORIZED);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        log.info("Retrieved UserDetails for user: {}", userDetails.getUsername());
        log.info("Retrieved password for user: {}", userDetails.getPassword());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
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
            // Extract username after verification
            String username = getUsername(token);
            // Check if the provided user details match the decoded username
            if (user != null && !username.equals(user.getUsername())) {
                log.error("Token username doesn't match user details");
                return false;
            }
            // Check token expiration
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate != null && expirationDate.before(new Date());
    }
}


