package com.pizzara.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDatabaseConnectorTest {

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

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();

            System.out.println("Erfolgreich mit der Datenbank verbunden: " + connection.getMetaData().getURL());

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
