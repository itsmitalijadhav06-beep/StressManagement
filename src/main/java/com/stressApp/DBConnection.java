package com.stressApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String HOST = "localhost";
    private static final String PORT = "1521";
    private static final String SERVICE = "ORCLPDB";
    private static final String USER = "STRESSUSER";
    private static final String PASSWORD = "StressUser123";

    private static final String URL = "jdbc:oracle:thin:@//" + HOST + ":" + PORT + "/" + SERVICE;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(true); //
            System.out.println(" Database connected successfully!");
            return conn;

        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    " Oracle JDBC Driver not found. Please add ojdbc8.jar or ojdbc17.jar to WEB-INF/lib.", e);
        } catch (SQLException e) {
            throw new SQLException(" Database connection failed: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Connection test successful!");
            }
        } catch (SQLException e) {
            System.err.println(" Connection test failed: " + e.getMessage());
        }
    }
}
