package com.pizzara.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mariadb://pizzara.duckdns.org:666/pizzara";
    private static final String USERNAME = "pizzara";
    private static final String PASSWORD = "NB6TMmtKB1rapGoo870V";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}