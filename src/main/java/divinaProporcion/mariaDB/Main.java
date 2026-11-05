package divinaProporcion.mariaDB;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            // 1. Comprobamos de forma rápida que hay conectividad con MariaDB
            ConexionDB.conectaDB();

            // 2. Lanzamos el menú principal de la aplicación
            Menu.menu(); 
            
        } catch (SQLException e) {
            System.out.println("No se pudo iniciar la aplicación debido a un error de base de datos.");
            System.out.println("Detalles del error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


   