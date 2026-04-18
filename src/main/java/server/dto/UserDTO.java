package server.dto;

public class UserDTO {
    private int userId;
    private String username;
    private String password;
    private String role;
    private boolean isLoggedIn;
    private int totalWins;

    public UserDTO() {}

    public UserDTO(int userId, String username, String password, String role, boolean isLoggedIn, int totalWins) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLoggedIn = isLoggedIn;
        this.totalWins = totalWins;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isLoggedIn() { return isLoggedIn; }
    public void setLoggedIn(boolean loggedIn) { isLoggedIn = loggedIn; }

    public int getTotalWins() { return totalWins; }
    public void setTotalWins(int totalWins) { this.totalWins = totalWins; }
}

