
-- Table to store both players and administrators
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Recommended to store hashed passwords
    role ENUM('PLAYER', 'ADMIN') DEFAULT 'PLAYER',
    wins INT DEFAULT 0,
    is_logged_in BOOLEAN DEFAULT FALSE
);

-- Tabel configuration by the administrator
CREATE TABLE IF NOT EXISTS game_config (
    id INT PRIMARY KEY DEFAULT 1,
    wait_time_seconds INT DEFAULT 10,
    round_duration_seconds INT DEFAULT 30
);

-- Table longest words formed
CREATE TABLE IF NOT EXISTS longest_words (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    word VARCHAR(50) NOT NULL,
    length INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ignore on duplicate
INSERT IGNORE INTO game_config (id, wait_time_seconds, round_duration_seconds) VALUES (1, 10, 30);

