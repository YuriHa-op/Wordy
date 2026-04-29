package server.handler.service;

import server.db.UserDAO;
import server.dto.UserDTO;
import server.handler.core.SessionManager;

public class AuthService {

    public static class AuthResult {
        public final boolean success;
        public final String message;
        public final String token;
        public final String role;

        public AuthResult(boolean success, String message, String token, String role) {
            this.success = success;
            this.message = message;
            this.token = token;
            this.role = role;
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
                return new AuthResult(false, "Invalid username or password", "", "");
            }
            if (adminOnly && !"ADMIN".equals(user.getRole())) {
                return new AuthResult(false, "Admin role required", "", user.getRole());
            }

            String oldToken = sessionManager.getTokenByUsername(username);
            if (oldToken != null) {
                sessionManager.invalidateSession(oldToken);
            }
            String token = sessionManager.createSession(username, user.getRole());
            userDAO.updateLoginStatus(username, true);
            String message = oldToken == null
                    ? "Login successful"
                    : "Login successful. Previous session disconnected.";
            return new AuthResult(true, message, token, user.getRole());
        } catch (Exception e) {
            String error = e.getMessage();
            if (error != null && error.contains("Communications link failure")) {
                return new AuthResult(false, "Database connection failed, server is offline.", "", "");
            }
            if (error != null && error.length() > 40) {
                error = error.substring(0, 37) + "...";
            }
            return new AuthResult(false, "Login error: " + error, "", "");
        }
    }

    public AuthResult logout(String token) {
        if (!sessionManager.isValid(token)) {
            return new AuthResult(false, "Invalid session", "", "");
        }
        String username = sessionManager.getUsername(token);
        sessionManager.invalidateSession(token);
        try {
            userDAO.updateLoginStatus(username, false);
        } catch (Exception ignored) {
            // Session is still invalidated even if DB update fails.
        }
        return new AuthResult(true, "Logout successful", "", "");
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

