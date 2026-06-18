package com.stressApp;

import java.sql.*;

public class UserDAO {

    // ✅ Register a new user
    public void addUser(String username, String password, String email) throws SQLException {
        String sql = "INSERT INTO users (id, username, password, email) VALUES (users_seq.NEXTVAL, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            System.out.println("✅ User registered successfully: " + username);
        }
    }

    // ✅ Validate user login
    public boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // ✅ Get user ID for login/survey relation
    public int getUserId(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt("id");
        }
        return -1;
    }
}
