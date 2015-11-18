package com.fbapp;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 *
 * @author Yaara Shoham
 */
public abstract class DBHandler {

    protected Connection connection;
    private static String DbType = "mysql-heroku";
    //private static String DbType="mysql-local";

    public String DB_HOST = "localhost";
    public String DB_NAME = "fbapp";

    public String DRIVER = "com.mysql.jdbc.Driver";
    public String URL = "jdbc:mysql://" + DB_HOST + "/" + DB_NAME;

    //  Database credentials
    public String USERNAME = "root";
    public String PASSWORD = "1234";

    public DBHandler() {
        if (DbType.equals("mysql-heroku")) {
            this.DRIVER = "com.mysql.jdbc.Driver";
            this.DB_HOST = "us-cdbr-iron-east-03.cleardb.net";
            this.DB_NAME = "heroku_ac01b3fc78454e8";
            this.USERNAME = "baa4b397146d35";
            this.PASSWORD = "a053ee7d";
        }
    }

    public void open() throws Exception {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println("Error in closing connection: " + ex.getMessage());
            }
        }
    }
}
