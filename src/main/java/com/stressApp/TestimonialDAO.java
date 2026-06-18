package com.stressApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestimonialDAO {

    // ✅ Add new testimonial
    public void addTestimonial(int userId, String message) throws SQLException {
        String sql = "INSERT INTO testimonials (id, user_id, message, date_posted) " +
                "VALUES (testimonials_seq.NEXTVAL, ?, ?, SYSDATE)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.executeUpdate();
            System.out.println("✅ Testimonial added successfully for User ID: " + userId);
        }
    }

    // ✅ Fetch all testimonials
    public List<String> getTestimonials() throws SQLException {
        List<String> testimonials = new ArrayList<>();
        String sql = "SELECT message FROM testimonials ORDER BY date_posted DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                testimonials.add(rs.getString("message"));
            }
        }
        return testimonials;
    }
}
