package server.db;

import server.dto.LongestWordDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LongestWordDAO {

    public List<LongestWordDTO> getTopLongestWords(int limit) throws Exception {
        List<LongestWordDTO> topWords = new ArrayList<>();
        String sql = "SELECT u.username, lw.word, lw.word_length " +
                     "FROM longest_words lw " +
                     "JOIN users u ON lw.user_id = u.user_id " +
                     "ORDER BY lw.word_length DESC, lw.submitted_at ASC " +
                     "LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topWords.add(new LongestWordDTO(
                            rs.getString("username"),
                            rs.getString("word"),
                            rs.getInt("word_length")
                    ));
                }
            }
        }
        return topWords;
    }

    public void insertLongestWord(String username, String word) throws Exception {
        // Need to get user_id first
        String queryUser = "SELECT user_id FROM users WHERE username = ?";
        int userId = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryUser)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                }
            }
        }

        if (userId != -1) {
            String insertSql = "INSERT INTO longest_words (user_id, word, word_length) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, word);
                stmt.setInt(3, word.length());
                stmt.executeUpdate();
            }
        }
    }
}

