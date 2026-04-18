package server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ConfigDAO {

    public Map<String, Integer> getAllConfigs() throws Exception {
        Map<String, Integer> configs = new HashMap<>();
        String sql = "SELECT config_key, config_value FROM game_config";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                configs.put(rs.getString("config_key"), rs.getInt("config_value"));
            }
        }
        return configs;
    }

    public int getConfigValue(String key, int defaultValue) {
        String sql = "SELECT config_value FROM game_config WHERE config_key = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("config_value");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public boolean updateConfig(String key, int value) throws Exception {
        String sql = "UPDATE game_config SET config_value = ? WHERE config_key = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, value);
            stmt.setString(2, key);
            return stmt.executeUpdate() > 0;
        }
    }
}

