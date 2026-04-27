-- ============================================
-- Create the database
-- ============================================
DROP DATABASE IF EXISTS wordy_game;
CREATE DATABASE wordy_game;
USE wordy_game;

-- ============================================
-- Table: users
-- ============================================
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('PLAYER', 'ADMIN') NOT NULL DEFAULT 'PLAYER',
                       is_logged_in BOOLEAN NOT NULL DEFAULT FALSE,
                       total_wins INT NOT NULL DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================
-- Table: longest_words
-- ============================================
CREATE TABLE longest_words (
                               word_id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               word VARCHAR(100) NOT NULL,
                               word_length INT NOT NULL,
                               submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================
-- Table: game_config
-- ============================================
CREATE TABLE game_config (
                             config_id INT AUTO_INCREMENT PRIMARY KEY,
                             config_key VARCHAR(50) NOT NULL UNIQUE,
                             config_value INT NOT NULL,
                             description VARCHAR(255),
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================
-- Table: games
-- ============================================
CREATE TABLE games (
                       game_id INT AUTO_INCREMENT PRIMARY KEY,
                       winner_id INT,
                       started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       ended_at TIMESTAMP NULL,
                       FOREIGN KEY (winner_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================================
-- Table: game_participants
-- ============================================
CREATE TABLE game_participants (
                                   game_id INT NOT NULL,
                                   user_id INT NOT NULL,
                                   rounds_won INT DEFAULT 0,
                                   PRIMARY KEY (game_id, user_id),
                                   FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE CASCADE,
                                   FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================
-- INSERT DEFAULT DATA
-- ============================================

-- Default configuration values
INSERT INTO game_config (config_key, config_value, description) VALUES
                                                                    ('waiting_time', 10, 'Waiting time for players to join a game (in seconds)'),
                                                                    ('round_duration', 30, 'Duration for a game round (in seconds)');

-- Default admin account
INSERT INTO users (username, password, role) VALUES
    ('admin', 'admin123', 'ADMIN');

-- Sample player accounts
INSERT INTO users (username, password, role) VALUES
                                                 ('player1', 'password1', 'PLAYER'),
                                                 ('player2', 'password2', 'PLAYER'),
                                                 ('player3', 'password3', 'PLAYER'),
                                                 ('player4', 'password4', 'PLAYER'),
                                                 ('player5', 'password5', 'PLAYER');

-- ============================================
-- INDEXES (FIXED: removed DESC)
-- ============================================
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_total_wins ON users(total_wins);
CREATE INDEX idx_word_length ON longest_words(word_length);

-- ============================================
-- USEFUL QUERIES (for reference)
-- ============================================

-- Top 5 players
-- SELECT username, total_wins
-- FROM users
-- WHERE role = 'PLAYER'
-- ORDER BY total_wins DESC
-- LIMIT 5;

-- Top 5 longest words
-- SELECT u.username, lw.word, lw.word_length
-- FROM longest_words lw
-- JOIN users u ON lw.user_id = u.user_id
-- ORDER BY lw.word_length DESC, lw.submitted_at ASC
-- LIMIT 5;

-- Get config
-- SELECT config_key, config_value FROM game_config;