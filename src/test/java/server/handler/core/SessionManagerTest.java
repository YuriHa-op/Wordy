package server.handler.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    @Test
    void createSessionInvalidatesPreviousSessionForSameUser() {
        SessionManager sessionManager = new SessionManager();

        String first = sessionManager.createSession("player1", "PLAYER");
        String second = sessionManager.createSession("player1", "PLAYER");

        assertNotEquals(first, second);
        assertFalse(sessionManager.isValid(first));
        assertTrue(sessionManager.isValid(second));
        assertEquals("player1", sessionManager.getUsername(second));
    }

    @Test
    void invalidateSessionRemovesMappings() {
        SessionManager sessionManager = new SessionManager();

        String token = sessionManager.createSession("admin", "ADMIN");
        sessionManager.invalidateSession(token);

        assertFalse(sessionManager.isValid(token));
        assertNull(sessionManager.getUsername(token));
        assertNull(sessionManager.getTokenByUsername("admin"));
    }
}

