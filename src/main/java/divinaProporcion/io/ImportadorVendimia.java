package divinaProporcion.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import divinaProporcion.dao.ConexionDB;
import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.dao.DAOParcela;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;

/**
 * Importa la vendimia desde un fichero CSV separado por punto y coma.
 *
 * Formato por línea: {@code parcela;anualidad;kilos;graduacion}, con cabecera. La graduación puede
 * ir vacía. Las líneas incorrectas se descartan con un aviso y las válidas se insertan en una única
 * transacción. Al terminar muestra un resumen por tipo de error.
 */
public class ImportadorVendimia {

	/**
	 * Lee el CSV e inserta las cosechas válidas.
	 *
	 * @param ruta ruta del fichero CSV (UTF-8)
	 * @return {@code true} si se confirmó la transacción, {@code false} si se deshizo por un error de base de datos
	 * @throws IOException si no se puede leer el fichero
	 * @throws SQLException si falla la conexión con la base de datos
	 */
	public static boolean leer(String ruta) throws IOException, SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
					BufferedReader br = new BufferedReader(new FileReader(ruta, StandardCharsets.UTF_8))) {
			
			//desactivamos el autocommit para poder escribir todos los registros de una vez y en caso de fallo rollback
			conexion.setAutoCommit(false);

			// Contadores error
			int lineaIncompleta = 0;
			int anualidadErronea = 0;
			int pesajeErroneo = 0;
			int sinGraduacion = 0;
			int errorGraduacion = 0;
			int parcelaVacia = 0;
			int errorParcela =0;
			int lineasDuplicadas=0;
			int insertadasCorrectas=0;


			String linea;
			br.readLine();
			int n = 1;

			try {
				while ((linea = br.readLine()) != null) {
				
					// nace de nuevo en cada línea y no puede heredar el id de la anterior
					Integer idParcela = null;
					int anualidad;
					double kilosUva;
					// inicializamos a null para que si la linea no tiene valor se pueda registrar igualmente en la BD
					Double graduacion = null;
					// incrementamos la linea que estaba en posicion de cabecera
					n++;
					// usamos el -1 en el split para que graduación pueda ser un campo null válido, si no se leería como una linea mal formada de 3 elementos
					String[] elementos = linea.split(";", -1);

					if (elementos.length != 4) {

						System.out.printf("Línea %d incompleta: %s \n", n, linea);
						lineaIncompleta++;
						continue;
					}

					if (elementos[0].trim().isEmpty()) {

						System.out.printf("Línea %d parcela vacía: %s \n", n, linea);
						parcelaVacia++;
						continue;

					}

					try {
						anualidad = Integer.parseInt(elementos[1].trim());

					} catch (NumberFormatException e) {
						System.out.printf("Línea %d año no válido: %s \n", n, linea);
						anualidadErronea++;
						continue;
					}

					try {
						kilosUva = Double.parseDouble(elementos[2].trim());

					} catch (NumberFormatException e) {
						System.out.printf("Línea %d kilos de cosecha no válidos: %s \n", n, linea);
						pesajeErroneo++;
						continue;
					}

					if (kilosUva < 0) {

						System.out.printf("Línea %d kilos de cosecha no válidos: %s \n", n, linea);
						pesajeErroneo++;
						continue;
					}

					try {

						if (!(elementos[3].trim()).isEmpty()) {

							graduacion = Double.parseDouble(elementos[3].trim());

							if (graduacion <= 0) {
								System.out.printf("Línea %d graduación no válida: %s \n", n, linea);
								errorGraduacion++;
								continue;
							}

						} else {
							sinGraduacion++;
						}

					} catch (NumberFormatException e) {
						System.out.printf("Línea %d graduación no válida: %s \n", n, linea);
						errorGraduacion++;
						continue;
					}
				
					//se guarda el valor para emplearlo despues para insertar la cosecha
					idParcela = DAOParcela.buscarIdPorNombre(elementos[0].trim());
					if (idParcela == null ) {
	
						System.out.printf("Línea %d parcela inexistente: %s \n", n, linea);
						errorParcela++;
						continue;
					
					}
					Parcela parcela = new Parcela();
					parcela.setId(idParcela);          
				
					//idCosecha puede valer 0 por que al ser autoincremental la BD lo regenera.
					try {
						DAOCosecha.crearCosecha(conexion,(new Cosecha(0, anualidad, parcela, kilosUva, graduacion)));
					
					// dato duplicado o que viola una restricción de la tabla: se informa y se sigue con la siguiente línea
					} catch (SQLIntegrityConstraintViolationException e) {
						System.out.printf("Línea %d no insertada (duplicada o dato no permitido): %s \n  Motivo: %s \n", n, linea,e.getMessage());
						lineasDuplicadas++;
						continue;
					}
					insertadasCorrectas++;

				}//fin while 
			
				conexion.commit();
			} catch (SQLException e) {
				// fallo de BD o de conexión: se deshace  lo insertado
				conexion.rollback();
				System.out.printf("Error en la BD : %s \n",e.getMessage());
				return false;
			}
			
			//Resumen del importador
			int rechazadasPorFormato = lineaIncompleta + parcelaVacia + anualidadErronea + pesajeErroneo
					+ errorGraduacion + errorParcela;

			System.out.printf("\n========RESUMEN DEL IMPORTADOR===========\n");
			System.out.printf("Líneas de datos leídas:        %d\n", n - 1);
			System.out.printf("Cosechas insertadas:           %d\n", insertadasCorrectas);
			System.out.printf("  Con graduación vacía (info): %d\n", sinGraduacion);
			System.out.printf("No insertadas (BD):            %d\n", lineasDuplicadas);
			System.out.printf("Rechazadas por formato/datos:  %d\n", rechazadasPorFormato);
			System.out.printf("  Líneas incompletas:          %d\n", lineaIncompleta);
			System.out.printf("  Parcela vacía:               %d\n", parcelaVacia);
			System.out.printf("  Parcela inexistente:         %d\n", errorParcela);
			System.out.printf("  Año no válido:               %d\n", anualidadErronea);
			System.out.printf("  Kilos no válidos:            %d\n", pesajeErroneo);
			System.out.printf("  Graduación no válida:        %d\n", errorGraduacion);
			System.out.printf("=========================================\n");

		}

		return true;

	}

}
