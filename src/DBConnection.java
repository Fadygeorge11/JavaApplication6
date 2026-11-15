package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=E_book;encrypt=true;trustServerCertificate=true";

            String user = System.getenv("EBOOK_DB_USER");
            String pass = System.getenv("EBOOK_DB_PASSWORD");

            if (user == null || pass == null) {
                throw new SQLException("Database credentials are not configured.");
            }

            return DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}