package divinaProporcion.vista;

import divinaProporcion.controlador.ControladorCosecha;
import divinaProporcion.controlador.ControladorParcela;
import divinaProporcion.controlador.ControladorProducto;
import divinaProporcion.controlador.ControladorVariedad;
import divinaProporcion.controlador.ControladorVenta;
import divinaProporcion.io.BackupBinario;
import divinaProporcion.io.ExportadorJson;
import divinaProporcion.io.ImportadorJson;
import divinaProporcion.io.ImportadorVendimia;
import divinaProporcion.io.InformeVentas;
import divinaProporcion.modelo.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
	private static final String RUTA_INFORME_VENTAS = "datos/informe_ventas.txt";

	/**
	 * Muestra el menú principal y atiende las opciones hasta que se elige salir.
	 */
	public static void menu() {
		int opcionMenu = -1;
		do {
			imprimirCabecera("SISTEMA DE GESTION");
			System.out.println("1. GESTION DE PARCELAS");
			System.out.println("2. GESTION DE COSECHAS");
			System.out.println("3. GESTION DE VARIEDADES");
			System.out.println("4. IMPORTAR VENDIMIA (CSV)");
			System.out.println("5. EXPORTAR COSECHAS (JSON)");
			System.out.println("6. IMPORTAR COSECHAS (JSON)");
			System.out.println("7. GENERAR INFORME DE VENTAS ANUAL");
			System.out.println("8. BACKUP Y RESTAURAR DATOS");
			System.out.println("9. GESTION DE VENTAS Y STOCK");
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
				gestionBackup();
				break;
			case 9:
				gestionVenta();
				break;
			case 10:
				System.out.println("EJECUCION FINALIZADA");
				break;
			default:
				System.out.println("Opción no válida. Inténtalo de nuevo.");
				break;
			}
		} while (opcionMenu != 10);
	}

	/** Cabecera común de todos los menús: título centrado en un ancho fijo. */
	private static void imprimirCabecera(String titulo) {
		final int ANCHO = 40;
		String texto = " " + titulo + " ";
		int izquierda = (ANCHO - texto.length()) / 2;
		int derecha = ANCHO - texto.length() - izquierda;
		System.out.println("=".repeat(ANCHO));
		System.out.println("BODEGAS DIVINA PROPORCION");
		System.out.println("=".repeat(izquierda) + texto + "=".repeat(derecha));
	}

	// ==========================================
	// SUBMENÚS DE GESTIÓN
	// ==========================================

	private static void gestionParcela()  {
		int opcionMenu = -1;
		do {
			imprimirCabecera("GESTION DE PARCELAS");
			System.out.println("1. CREAR PARCELA");
			System.out.println(
					"2. LISTAR PARCELAS (filtros: nombre/extension/variedad/ubicacion)");
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
			imprimirCabecera("GESTION DE COSECHAS");
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
			imprimirCabecera("GESTION DE VARIEDADES");
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

	private static void gestionVenta() {
		int opcionMenu = -1;
		do {
			imprimirCabecera("GESTION DE VENTAS");
			System.out.println("1. LISTAR PRODUCTOS CON STOCK");
			System.out.println("2. REGISTRAR VENTA");
			System.out.println("3. LISTAR VENTAS");
			System.out.println("4. VOLVER AL MENÚ PRINCIPAL");
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
				System.out.println(ControladorProducto.listarStock());
				break;
			case 2:
				try {
					System.out.print("Id del producto: ");
					int idProducto = Integer.parseInt(teclado.nextLine().trim());
					System.out.print("Cantidad de botellas: ");
					int cantidad = Integer.parseInt(teclado.nextLine().trim());
					if (cantidad <= 0) {
						System.out.println("La cantidad debe ser mayor que 0.");
						break;
					}
					System.out.print("Fecha (AAAA-MM-DD): ");
					LocalDate fecha = LocalDate.parse(teclado.nextLine().trim());
					System.out.println(ControladorVenta.registrar(idProducto, cantidad, fecha));
				} catch (NumberFormatException e) {
					System.out.println("Número no válido.");
				} catch (DateTimeParseException e) {
					System.out.println("Fecha no válida, usa AAAA-MM-DD.");
				}
				break;
			case 3:
				System.out.println(ControladorVenta.listar());
				break;
			case 4:
				System.out.println("VOLVIENDO AL MENÚ PRINCIPAL...");
				break;
			default:
				System.out.println("Opción no válida. Inténtalo de nuevo.");
				break;
			}
		} while (opcionMenu != 4);
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
			InformeVentas.generar(RUTA_INFORME_VENTAS);
			System.out.println("Informe generado en " + RUTA_INFORME_VENTAS);
		} catch (IOException e) {
			System.out.println("No se pudo escribir el informe: " + e.getMessage());

		} catch (SQLException e) {
			System.out.println("Error de BD: " + e.getMessage());

		}
	}
		

	private static void gestionBackup() {
		int opcionMenu = -1;
		do {
			imprimirCabecera("BACKUP DE DATOS");
			System.out.println("1. GENERAR BACKUP");
			System.out.println("2. RESTAURAR BACKUP");
			System.out.println("3. VOLVER AL MENÚ PRINCIPAL");
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
				backupDB();
				break;
			case 2:
				restaurarDB();
				break;
			case 3:
				System.out.println("VOLVIENDO AL MENÚ PRINCIPAL...");
				break;
			default:
				System.out.println("Opción no válida. Inténtalo de nuevo.");
				break;
			}
		} while (opcionMenu != 3);
	}

	private static void restaurarDB() {
		if (!new File(RUTA_BACKUP).exists()) {
			System.out.println("No existe el fichero de backup (" + RUTA_BACKUP + "). Genera uno primero.");
			return;
		}
		System.out.print("Esto borrará los datos actuales y los sustituirá por los del backup. ¿Continuar? (s/n): ");
		if (!teclado.nextLine().trim().equalsIgnoreCase("s")) {
			System.out.println("Restauración cancelada.");
			return;
		}
		try {
			if (BackupBinario.restaurar(RUTA_BACKUP)) {
				System.out.println("Backup restaurado.");
			} else {
				System.out.println("No se pudo restaurar el backup.");
			}
		} catch (IOException e) {
			System.out.println("No se pudo leer el backup: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("El backup no es compatible con esta versión: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error de BD, no se ha modificado nada: " + e.getMessage());
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
