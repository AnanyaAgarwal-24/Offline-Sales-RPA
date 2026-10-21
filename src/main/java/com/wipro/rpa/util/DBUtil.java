package com.wipro.rpa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place for the JDBC connection.
 * Update DB_URL / DB_USER / DB_PASS to match your MySQL setup.
 */
public class DBUtil {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/rpa_sales_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found. Run Maven to download project dependencies.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
