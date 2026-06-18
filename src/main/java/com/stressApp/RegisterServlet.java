package com.stressApp;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        try (Connection conn = DBConnection.getConnection()) {

            if ("register".equalsIgnoreCase(action)) {
                // Check if username already exists
                PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT COUNT(*) FROM users WHERE LOWER(username)=LOWER(?)");
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();

                if (rs.getInt(1) > 0) {
                    // Username exists → redirect back with error
                    response.sendRedirect("index.html?regerror=exists");
                    return;
                }

                // Insert new user
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO users (id, username, password, email) VALUES (users_seq.NEXTVAL, ?, ?, ?)");
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, email);
                insertStmt.executeUpdate();

                // Registration success → redirect to login page
                response.sendRedirect("index.html?regsuccess=true");

            } else if ("login".equalsIgnoreCase(action)) {
                // Verify credentials
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id FROM users WHERE LOWER(username)=LOWER(?) AND password=?");
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");

                    // Create session
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("userId", userId);

                    // Record login history
                    PreparedStatement logStmt = conn.prepareStatement(
                            "INSERT INTO login_history (id, username, login_time) VALUES (login_history_seq.NEXTVAL, ?, SYSDATE)");
                    logStmt.setString(1, username);
                    logStmt.executeUpdate();

                    // ✅ Redirect to home page
                    response.sendRedirect("home.html");
                    return;
                } else {
                    // Invalid credentials → redirect back with error
                    response.sendRedirect("index.html?loginerror=invalid");
                }

            } else {
                response.sendRedirect("index.html?error=invalidaction");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("index.html?dberror=true");
        }
    }
}
