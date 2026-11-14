package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Crea conexiones JDBC con la BD local de desarrollo.
 */
public class ConexionDB {

    private static String url ="jdbc:mariadb://127.0.0.1:3307/divinaProporcion";
    private static String user ="dp";
    private static String pass ="dp_dev";


	/**
	 * Abre una conexión nueva. Quien la reciba debe cerrarla (try-with-resources).
	 *
	 * @return conexión abierta
	 * @throws SQLException si no se puede conectar
	 */
    public static Connection conectaDB() throws SQLException {

        return DriverManager.getConnection(url, user, pass);
    }
}


