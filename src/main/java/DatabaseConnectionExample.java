import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionExample {

    // JDBC-URL, Benutzername und Passwort der MariaDB-Datenbank
    private static final String URL = "jdbc:mariadb://pizzara.duckdns.org:666/pizzara";
    private static final String USERNAME = "pizzara";
    private static final String PASSWORD = "NB6TMmtKB1rapGoo870V";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            // SQL-Abfrage, um Daten aus der Tabelle "ingredient" abzurufen
            String sql = "SELECT * FROM ingredient";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Daten aus dem ResultSet lesen und in der Konsole ausgeben
                    while (resultSet.next()) {
                        int id = resultSet.getInt("ingredient_id");
                        String name = resultSet.getString("name");
                        String typ = resultSet.getString("typ");

                        System.out.println("ID: " + id + ", Name: " + name + ", typ: " + typ);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}