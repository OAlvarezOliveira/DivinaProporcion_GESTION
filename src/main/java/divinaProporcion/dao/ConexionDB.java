package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static String url ="jdbc:mariadb://127.0.0.1:3307/divinaProporcion";
    private static String user ="dp";
    private static String pass ="dp_dev";


    public static Connection conectaDB() throws SQLException {

        return DriverManager.getConnection(url, user, pass);
    }
}


