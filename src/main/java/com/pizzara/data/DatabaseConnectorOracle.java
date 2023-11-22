package com.pizzara.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectorOracle {
    private static final String URL = "jdbc:oracle:thin:@rs03-db-inf-min.ad.fh-bielefeld.de:1521:orcl";
    private static final String USERNAME = "pizzeria";
    private static final String PASSWORD = "$$123Pizzara123$$";

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