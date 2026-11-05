package divinaProporcion.mariaDB;

import java.util.Scanner;

public class Menu {
	
    private static Scanner teclado = new Scanner(System.in);
	
    public static void menu() {
        int opcionMenu = -1;
        do {
            System.out.println("=========================");
            System.out.println("BODEGAS DIVINA PROPORCION");
            System.out.println("====SISTEMA DE GESTION====");
            System.out.println("1. GESTION PARCELAS");
            System.out.println("2. GESTION COSECHAS");
            System.out.println("3. GESTION VARIEDADES");
            System.out.println("4. IMPORTAR VENDIMIA (PRODUCTOS)");
            System.out.println("5. EXPORTAR PRODUCCION");
            System.out.println("6. GENERAR INFORME DE VENTAS ANUAL");
            System.out.println("7. GENERAR BACKUP DATOS"); 
            System.out.println("8. SALIR");                  
            System.out.print("Seleccione una opción: ");

            try {
                opcionMenu = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduce un número válido.");
                opcionMenu = -1; 
                continue;        
            }

            switch (opcionMenu) {
                case 1:
                    gestionParcela();
                    break;
                case 2:
                    gestionCosecha();
                    break;
                case 3:
                    gestionVariedad();
                    break;
                case 4:
                    importarVendimia();
                    break;
                case 5:
                    exportarProduccion();
                    break;
                case 6:
                    generarInformeVentas();
                    break;
                case 7:
                    backupDB();
                    break;
                case 8:
                    System.out.println("EJECUCION FINALIZADA");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        } while (opcionMenu != 8); 
    }

    // ==========================================
    // SUBMENÚS DE GESTIÓN
    // ==========================================

    private static void gestionParcela() {
        int opcionMenu = -1;
        do {
            System.out.println("=========================");
            System.out.println("BODEGAS DIVINA PROPORCION");
            System.out.println("====GESTION DE PARCELAS====");
            System.out.println("1. CREAR PARCELA");
            System.out.println("2. LISTAR PARCELA : FITROS NOMBRE "
                    + "(Filtros disponibles: Extension /Variedad/Ubicacion)");
            System.out.println("3. EDITAR PARCELA");
            System.out.println("4. BORRAR PARCELA");
            System.out.println("5. VOLVER AL MENÚ PRINCIPAL");                  
            System.out.print("Seleccione una opción: ");

            try {
                opcionMenu = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduce un número válido.");
                opcionMenu = -1; 
                continue;        
            }

            switch (opcionMenu) {
                case 1:
                    DAOParcela.crearParcela();
                    break;
                case 2:
                    DAOParcela.listarParcela();
                    break;
                case 3:
                    DAOParcela.editarParcela();
                    break;
                case 4:
                    DAOParcela.borrarParcela();
                    break;
                case 5:
                    System.out.println("VOLVIENDO AL MENÚ PRINCIPAL...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        } while (opcionMenu != 5); 
    }
    
    private static void gestionCosecha() {
        int opcionMenu = -1;
        do {
            System.out.println("=========================");
            System.out.println("BODEGAS DIVINA PROPORCION");
            System.out.println("====GESTION DE COSECHAS====");
            System.out.println("1. CREAR COSECHA");
            System.out.println("2. LISTAR COSECHAS");
            System.out.println("3. EDITAR COSECHA");
            System.out.println("4. BORRAR COSECHA");
            System.out.println("5. VOLVER AL MENÚ PRINCIPAL");
            System.out.print("Seleccione una opción: ");

            try {
                opcionMenu = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduce un número válido.");
                opcionMenu = -1;
                continue;
            }

            switch (opcionMenu) {
                case 1:
                    DAOCosecha.crearCosecha();
                    break;
                case 2:
                    DAOCosecha.listarCosecha();
                    break;
                case 3:
                    DAOCosecha.editarCosecha();
                    break;
                case 4:
                    DAOCosecha.borrarCosecha();
                    break;
                case 5:
                    System.out.println("VOLVIENDO AL MENÚ PRINCIPAL...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        } while (opcionMenu != 5);
    }

    private static void gestionVariedad() {
        int opcionMenu = -1;
        do {
            System.out.println("=========================");
            System.out.println("BODEGAS DIVINA PROPORCION");
            System.out.println("===GESTION DE VARIEDADES===");
            System.out.println("1. CREAR VARIEDAD");
            System.out.println("2. LISTAR VARIEDADES");
            System.out.println("3. EDITAR VARIEDAD");
            System.out.println("4. BORRAR VARIEDAD");
            System.out.println("5. VOLVER AL MENÚ PRINCIPAL");
            System.out.print("Seleccione una opción: ");

            try {
                opcionMenu = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduce un número válido.");
                opcionMenu = -1;
                continue;
            }

            switch (opcionMenu) {
                case 1:
                    DAOVariedad.crearVariedad();
                    break;
                case 2:
                    DAOVariedad.listarVariedad();
                    break;
                case 3:
                    DAOVariedad.editarVariedad();
                    break;
                case 4:
                    DAOVariedad.borrarVariedad();
                    break;
                case 5:
                    System.out.println("VOLVIENDO AL MENÚ PRINCIPAL...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        } while (opcionMenu != 5);
    }

    // ==========================================
    // PROCESOS AUXILIARES Y REPORTES
    // ==========================================

    private static void importarVendimia() {
        System.out.println("Importar Vendimia");
    }

    private static void exportarProduccion() {
        System.out.println("Exportar Productos");
    }

    private static void generarInformeVentas() {
        System.out.println("Informe de Ventas");
    }

    private static void backupDB() {
        System.out.println("backUp DB");
    }
}
