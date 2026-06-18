package com.stressApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurveyDAO {

    // ✅ Add a new survey record
    public void addSurvey(int userId, String stressLevel, String mood, int stressScore) throws SQLException {
        String sql = "INSERT INTO surveys (id, user_id, stress_level, mood, score, survey_date) " +
                "VALUES (surveys_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, stressLevel);
            stmt.setString(3, mood);
            stmt.setInt(4, stressScore);
            stmt.executeUpdate();

            System.out.println("✅ Survey data added successfully for User ID: " + userId);
        }
    }

    // ✅ Fetch stress history
    public List<String> getStressHistory(int userId) throws SQLException {
        List<String> history = new ArrayList<>();
        String sql = "SELECT stress_level, mood, score, TO_CHAR(survey_date, 'DD-MON-YYYY HH24:MI:SS') AS survey_date "
                +
                "FROM surveys WHERE user_id = ? ORDER BY survey_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String record = String.format(
                        "Score: %d | Level: %s | Mood: %s | Date: %s",
                        rs.getInt("score"),
                        rs.getString("stress_level"),
                        rs.getString("mood"),
                        rs.getString("survey_date"));
                history.add(record);
            }
        }
        return history;
    }
}
