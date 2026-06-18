package com.stressApp;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

public class StressServlet extends HttpServlet {
    private SurveyDAO surveyDAO = new SurveyDAO();
    private TestimonialDAO testimonialDAO = new TestimonialDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if ("survey".equalsIgnoreCase(action)) {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("userId") == null) {
                    response.sendRedirect("index.html");
                    return;
                }

                int userId = (int) session.getAttribute("userId");

                String[] responsesArr = new String[10];
                for (int i = 0; i < 10; i++) {
                    responsesArr[i] = request.getParameter("q" + (i + 1));
                }

                String mood = request.getParameter("mood");
                mood = (mood == null || mood.trim().isEmpty()) ? "Not specified" : mood;

                int agreeCount = 0;
                int highMarkers = 0;
                int[] stressIndicators = { 4, 5, 7, 10 };

                for (int i = 0; i < 10; i++) {
                    if ("Agree".equalsIgnoreCase(responsesArr[i])) {
                        agreeCount++;
                        for (int q : stressIndicators) {
                            if (i + 1 == q)
                                highMarkers++;
                        }
                    }
                }

                int stressScore = (int) Math.round((agreeCount / 10.0) * 10);

                String stressLevel;
                if (highMarkers >= 3 || agreeCount >= 8)
                    stressLevel = "high";
                else if (agreeCount >= 4)
                    stressLevel = "moderate";
                else
                    stressLevel = "low";

                // Save into database
                surveyDAO.addSurvey(userId, stressLevel, mood, stressScore);

                String encodedMood = URLEncoder.encode(mood, "UTF-8");
                String encodedLevel = URLEncoder.encode(stressLevel, "UTF-8");

                if ("high".equalsIgnoreCase(stressLevel)) {
                    response.sendRedirect(
                            "game.html?score=" + stressScore + "&level=" + encodedLevel + "&mood=" + encodedMood);
                } else {
                    response.sendRedirect("suggestions.html?score=" + stressScore + "&level=" + encodedLevel + "&mood="
                            + encodedMood);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3>Database Error: " + e.getMessage() + "</h3>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Unexpected Error: " + e.getMessage() + "</h3>");
        }
    }
}
