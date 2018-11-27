package ua.nure.vraniuk.usermanagement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnectionFactory implements ConnectionFactory{
    public static final String DRIVER =
            "org.hsqldb.jdbcDriver";
    public static final String DBURL =
            "jdbc:hsqldb:file:db/usermanagement";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    @Override
    public Connection getConnection(String url, String user, String password ) throws DatabaseException {
        if (url.isEmpty())
            url = DBURL;
        if (user.isEmpty())
            user = USER;
        if (password.isEmpty())
            password = PASSWORD;
        Connection my_connection = null;
        try {
            Class.forName(DRIVER);
            my_connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        return my_connection;
    }
}
