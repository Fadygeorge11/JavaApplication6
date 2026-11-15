package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=E_book;encrypt=true;trustServerCertificate=true";
            String user = "project";
            String pass = "1234";

            return DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
