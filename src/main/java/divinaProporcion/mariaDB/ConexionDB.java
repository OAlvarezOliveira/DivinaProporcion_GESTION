package divinaProporcion.mariaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class ConexionDB {

    private static String url;
    private static String user;
    private static String pass;
    private static Connection con = null; // Guardará la conexión activa

    // Bloque estático: Se ejecuta UNA SOLA VEZ cuando la aplicación arranca para cargar las credenciales
    static {
        try (InputStream input = ConexionDB.class.getResourceAsStream("properties.txt")) {
            if (input == null) {
                throw new RuntimeException("Error: No se pudo encontrar el archivo properties.txt");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                url = reader.readLine();
                user = reader.readLine();
                pass = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de configuración", e);
        }
    }

    public static Connection conectaDB() throws SQLException {
        // Si la conexión no existe o se ha cerrado por inactividad, la creamos
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión establecida con MariaDB.");
        }
        return con; // Devolvemos la conexión activa
    }
}


