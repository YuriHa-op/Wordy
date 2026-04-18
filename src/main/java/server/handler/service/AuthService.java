package server.handler.service;

import server.db.UserDAO;
import server.dto.UserDTO;
import server.handler.core.SessionManager;

public class AuthService {

    public static class AuthResult {
        public final boolean success;
        public final String message;
        public final String token;

        public AuthResult(boolean success, String message, String token) {
            this.success = success;
            this.message = message;
            this.token = token;
        }
    }

    private final UserDAO userDAO;
    private final SessionManager sessionManager;

    public AuthService(UserDAO userDAO, SessionManager sessionManager) {
        this.userDAO = userDAO;
        this.sessionManager = sessionManager;
    }

    public AuthResult login(String username, String password, boolean adminOnly) {
        try {
            UserDTO user = userDAO.findByUsername(username);
            if (user == null || !user.getPassword().equals(password)) {
                return new AuthResult(false, "Invalid username or password", "");
            }
            if (adminOnly && !"ADMIN".equals(user.getRole())) {
                return new AuthResult(false, "Admin role required", "");
            }

            String oldToken = sessionManager.getTokenByUsername(username);
            if (oldToken != null) {
                sessionManager.invalidateSession(oldToken);
            }
            String token = sessionManager.createSession(username, user.getRole());
            userDAO.updateLoginStatus(username, true);
            return new AuthResult(true, "Login successful", token);
        } catch (Exception e) {
            return new AuthResult(false, "Login error: " + e.getMessage(), "");
        }
    }

    public AuthResult logout(String token) {
        if (!sessionManager.isValid(token)) {
            return new AuthResult(false, "Invalid session", "");
        }
        String username = sessionManager.getUsername(token);
        sessionManager.invalidateSession(token);
        try {
            userDAO.updateLoginStatus(username, false);
        } catch (Exception ignored) {
            // Session is still invalidated even if DB update fails.
        }
        return new AuthResult(true, "Logout successful", "");
    }

    public boolean isValidSession(String token) {
        return sessionManager.isValid(token);
    }

    public String getUsername(String token) {
        return sessionManager.getUsername(token);
    }

    public boolean isAdmin(String token) {
        return "ADMIN".equals(sessionManager.getRole(token));
    }
}

