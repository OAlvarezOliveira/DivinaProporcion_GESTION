package divinaProporcion.vista;

import divinaProporcion.controlador.ControladorCosecha;
import divinaProporcion.controlador.ControladorParcela;
import divinaProporcion.controlador.ControladorVariedad;
import divinaProporcion.dao.DAOProducto;
import divinaProporcion.dao.DAOVenta;
import divinaProporcion.io.BackupBinario;
import divinaProporcion.io.ExportadorJson;
import divinaProporcion.io.ImportadorJson;
import divinaProporcion.io.ImportadorVendimia;
import divinaProporcion.modelo.Color;
import divinaProporcion.modelo.Producto;
import divinaProporcion.modelo.Venta;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola. Es la única clase que lee del teclado y que imprime:
 * los controladores solo devuelven texto.
 */
public class Menu {

	private static Scanner teclado = new Scanner(System.in);
	private static final String RUTA_VENDIMIA = "datos/vendimia.csv";
	private static final String RUTA_EXPORTACION = "datos/cosechas_exportadas.json";
	private static final String RUTA_IMPORTACION = "datos/cosechas_importar.json";
	private static final String RUTA_BACKUP = "datos/backup.bin";

	/**
	 * Muestra el menú principal y atiende las opciones hasta que se elige salir.
	 */
	public static void menu() {
		int opcionMenu = -1;
		do {
			System.out.println("=========================");
			System.out.println("BODEGAS DIVINA PROPORCION");
			System.out.println("====SISTEMA DE GESTION====");
			System.out.println("1. GESTION PARCELAS");
			System.out.println("2. GESTION COSECHAS");
			System.out.println("3. GESTION VARIEDADES");
			System.out.println("4. IMPORTAR VENDIMIA (CSV)");
			System.out.println("5. EXPORTAR COSECHAS (JSON)");
			System.out.println("6. IMPORTAR COSECHAS (JSON)");
			System.out.println("7. GENERAR INFORME DE VENTAS ANUAL");
			System.out.println("8. GENERAR BACKUP DATOS");
			System.out.println("9. LISTAR STOCK DE PRODUCTOS");
			System.out.println("10. SALIR");
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
				importarCosechasJson();
				break;
			case 7:
				generarInformeVentas();
				break;
			case 8:
				backupDB();
				break;
			case 9:
				StockProductos();
				break;
			case 10:
				System.out.println("EJECUCION FINALIZADA");
				break;	
			default:
				System.out.println("Opción no válida. Inténtalo de nuevo.");
				break;
			}
		} while (opcionMenu != 9);
	}

	// ==========================================
	// SUBMENÚS DE GESTIÓN
	// ==========================================

	private static void gestionParcela()  {
		int opcionMenu = -1;
		do {
			System.out.println("=========================");
			System.out.println("BODEGAS DIVINA PROPORCION");
			System.out.println("====GESTION DE PARCELAS====");
			System.out.println("1. CREAR PARCELA");
			System.out.println(
					"2. LISTAR PARCELA : FITROS NOMBRE " + "(Filtros disponibles: Extension/Variedad/Ubicacion)");
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
				try {
					System.out.print("Nombre de la parcela: ");
					String nombre = teclado.nextLine();
					System.out.print("Extensión de la parcela: ");
					double extension = Double.parseDouble(teclado.nextLine().trim());
					System.out.print("Id de variedad: ");
					int idVariedad = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Ubicación de la parcela: ");
					String ubicacion = teclado.nextLine();
					System.out.println(ControladorParcela.crear(nombre, extension, idVariedad, ubicacion));
				} catch (NumberFormatException e) {
					System.out.println("Dato no válido.");
				}
				break;
			case 2:
				System.out.println(ControladorParcela.listar());
				break;
			case 3:
				try {
					System.out.print("Id de la parcela: ");
					int idParcela = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Nombre de la parcela: ");
					String nombre = teclado.nextLine();
					System.out.print("Extensión de la parcela: ");
					double extension = Double.parseDouble(teclado.nextLine().trim());
					System.out.print("Id de variedad: ");
					int idVariedad = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Ubicación de la parcela: ");
					String ubicacion = teclado.nextLine();
					System.out.println(ControladorParcela.editar(idParcela, nombre, extension, idVariedad, ubicacion));
				} catch (NumberFormatException e) {
					System.out.println("Dato no válido.");
				}
				break;
			case 4:
				try {
					System.out.print("Id: ");
					int id = Integer.parseInt(teclado.nextLine().trim());
					System.out.println(ControladorParcela.borrar(id));
				} catch (NumberFormatException e) {
					System.out.println("Id no válido.");
				}
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
				try {
					System.out.print("Anualidad: ");
					int anualidad = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Id de la parcela: ");
					int idParcela = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Cantidad (kg): ");
					double cantidad = Double.parseDouble(teclado.nextLine().trim());
					System.out.print("Graduación (vacío si no hay): ");
					String texto = teclado.nextLine().trim();
					// Opcional: si se deja vacío queda null, que la BD guarda como NULL
					Double graduacion = null;
					if (!texto.isEmpty()) {
						graduacion = Double.parseDouble(texto);
					}
					System.out.println(ControladorCosecha.crear(anualidad, idParcela, cantidad, graduacion));
				} catch (NumberFormatException e) {
					System.out.println("Dato no válido.");
				}
				break;
			case 2:
				System.out.println(ControladorCosecha.listar());
				break;
			case 3:
				try {
					System.out.print("Id de la cosecha: ");
					int idCosecha = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Anualidad de la cosecha: ");
					int anualidad = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Id de la parcela: ");
					int idParcela = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Cantidad (en Kg) de la cosecha: ");
					double cantidad = Double.parseDouble(teclado.nextLine().trim());
					System.out.print("Graduación (vacío si no hay): ");
					String texto = teclado.nextLine().trim();
					// Opcional: si se deja vacío queda null, que la BD guarda como NULL
					Double graduacion = null;
					if (!texto.isEmpty()) {
						graduacion = Double.parseDouble(texto);
					}
					System.out
							.println(ControladorCosecha.editar(idCosecha, anualidad, idParcela, cantidad, graduacion));
				} catch (NumberFormatException e) {
					System.out.println("Dato no válido.");
				}
				break;
			case 4:
				try {
					System.out.print("Id: ");
					int id = Integer.parseInt(teclado.nextLine().trim());
					System.out.println(ControladorCosecha.borrar(id));
				} catch (NumberFormatException e) {
					System.out.println("Id no válido.");
				}
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
				try {
					System.out.print("Nombre: ");
					String nombre = teclado.nextLine();
					System.out.print("Color (tinto/blanco/rosado): ");
					System.out.println(
							ControladorVariedad.crear(nombre, Color.valueOf(teclado.nextLine().trim().toUpperCase())));
				} catch (IllegalArgumentException e) {
					System.out.println("Color no válido.");
				}
				break;
			case 2:
				System.out.println(ControladorVariedad.listar());
				break;
			case 3:
				try {
					System.out.print("Id: ");
					int id = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Nombre: ");
					String nombre = teclado.nextLine();
					System.out.print("Color (tinto/blanco/rosado): ");

					System.out.println(ControladorVariedad.editar(id, nombre,
							Color.valueOf(teclado.nextLine().trim().toUpperCase())));
				} catch (IllegalArgumentException e) {
					System.out.println("Dato no válido.");
				}
				break;
			case 4:
				try {
					System.out.print("Id: ");
					int id = Integer.parseInt(teclado.nextLine().trim());
					System.out.println(ControladorVariedad.borrar(id));
				} catch (NumberFormatException e) {
					System.out.println("Id no válido.");
				}
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
		try {
			if (ImportadorVendimia.leer(RUTA_VENDIMIA)) {
				System.out.println("Importación completada.");
			} else {
				System.out.println("La importación se canceló.");
			}
		} catch (IOException e) {
			System.out.println("No se pudo leer el fichero: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error de BD: " + e.getMessage());
		}
	}

	private static void exportarProduccion() {
		try {
			if (ExportadorJson.exportar(RUTA_EXPORTACION)) {
				System.out.println("Exportación completada.");
			} else {
				System.out.println("La exportación no se realizó.");
			}
		} catch (IOException e) {
			System.out.println("No se pudo escribir en el fichero: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error de BD: " + e.getMessage());
		}
	}

	private static void importarCosechasJson() {
		try {
			if (ImportadorJson.leer(RUTA_IMPORTACION)) {
				System.out.println("Importación completada.");
			} else {
				System.out.println("La importación se canceló.");
			}
		} catch (IOException e) {
			System.out.println("No se pudo leer el fichero: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error de BD: " + e.getMessage());
		}
	}

	private static void generarInformeVentas() {
			
			try {
				System.out.println("Lista de Ventas");
				List<Venta> lista = DAOVenta.listarVenta();
				
				for (Venta v : lista) {
					
					System.out.println(v);
				}

			} catch (SQLException e) {
				System.out.println("Error de BD: " + e.getMessage());

			}
	}
	
	
	private static void StockProductos() {
		
		try {
			System.out.println("Productos con Stock");
			List<Producto> lista = DAOProducto.listarStockProducto();
			
			for (Producto v : lista) {
				
				System.out.println(v);
			}

		} catch (SQLException e) {
			System.out.println("Error de BD: " + e.getMessage());

		}
}

	private static void backupDB() {
		try {
			if (BackupBinario.guardar(RUTA_BACKUP)) {
				System.out.println("Backup generado.");
			} else {
				System.out.println("No se pudo generar el backup.");
			}
		} catch (IOException e) {
			System.out.println("No se pudo escribir el backup: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error de BD: " + e.getMessage());
		}
	}
}
