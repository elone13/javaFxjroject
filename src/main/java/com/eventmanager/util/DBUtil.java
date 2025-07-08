package com.eventmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/eventdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // À adapter selon ton installation
    private static final String PASSWORD = ""; // À adapter selon ton installation

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
} 