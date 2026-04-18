package server.handler.core;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private final Map<String, String> usernameToToken = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToRole = new ConcurrentHashMap<>();

    public synchronized String createSession(String username, String role) {
        String oldToken = usernameToToken.get(username);
        if (oldToken != null) {
            invalidateSession(oldToken);
        }

        String token = UUID.randomUUID().toString();
        usernameToToken.put(username, token);
        tokenToUsername.put(token, username);
        tokenToRole.put(token, role);
        return token;
    }

    public synchronized void invalidateSession(String token) {
        String username = tokenToUsername.remove(token);
        tokenToRole.remove(token);
        if (username != null) {
            usernameToToken.remove(username);
        }
    }

    public boolean isValid(String token) {
        return token != null && tokenToUsername.containsKey(token);
    }

    public String getUsername(String token) {
        return tokenToUsername.get(token);
    }

    public String getRole(String token) {
        return tokenToRole.get(token);
    }

    public String getTokenByUsername(String username) {
        return usernameToToken.get(username);
    }
}

