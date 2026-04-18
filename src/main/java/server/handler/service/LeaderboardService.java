package server.handler.service;

import server.db.LongestWordDAO;
import server.db.UserDAO;
import server.dto.LongestWordDTO;
import server.dto.UserDTO;

import java.util.Collections;
import java.util.List;

public class LeaderboardService {

    private final UserDAO userDAO;
    private final LongestWordDAO longestWordDAO;

    public LeaderboardService(UserDAO userDAO, LongestWordDAO longestWordDAO) {
        this.userDAO = userDAO;
        this.longestWordDAO = longestWordDAO;
    }

    public List<UserDTO> getTopPlayers() {
        try {
            return userDAO.getTopPlayers(5);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<LongestWordDTO> getTopLongestWords() {
        try {
            return longestWordDAO.getTopLongestWords(5);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

