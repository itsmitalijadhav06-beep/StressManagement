package com.stressApp;

import java.sql.*;

public class LoginHistoryDAO {

    // ✅ Log a successful login
    public void logLogin(int userId) throws SQLException {
        String sql = "INSERT INTO login_history (id, user_id, login_time) " +
                "VALUES (login_history_seq.NEXTVAL, ?, SYSTIMESTAMP)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            System.out.println("✅ Login recorded for User ID: " + userId);
        }
    }
}
