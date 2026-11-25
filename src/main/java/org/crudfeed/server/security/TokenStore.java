package org.crudfeed.server.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {

    // Store userId and expiration timestamp
    private record Session(Long userId, Instant expiresAt) {}

    // Token lifetime = 24 hours
    private static final Duration TTL = Duration.ofHours(24);

    private final Map<String, Session> tokens = new ConcurrentHashMap<>();

    public String createToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new Session(userId, Instant.now().plus(TTL)));
        return token;
    }

    public Long getUserId(String token) {
        Session session = tokens.get(token);
        if (session == null) {
            return null;
        }
        // Remove from the map and treat as invalid if expired
        if (Instant.now().isAfter(session.expiresAt())) {
            tokens.remove(token);
            return null;
        }
        return session.userId();
    }

    public long requireUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            throw new IllegalArgumentException("Token is empty");
        }

        Long userId = getUserId(token);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        return userId;
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    // Optional: clean up expired tokens from memory every 10 minutes
    @Scheduled(fixedDelay = 600_000)
    public void cleanupExpired() {
        Instant now = Instant.now();
        tokens.entrySet().removeIf(entry -> now.isAfter(entry.getValue().expiresAt()));
    }
}