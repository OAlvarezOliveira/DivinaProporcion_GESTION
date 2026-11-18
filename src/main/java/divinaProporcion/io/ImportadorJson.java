package divinaProporcion.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import divinaProporcion.dao.ConexionDB;
import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.dao.DAOParcela;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;

/**
 * Importa cosechas desde un fichero JSON.
 *
 * Valida cada cosecha (parcela, cantidad y graduación) y descarta las incorrectas con un aviso.
 * Las válidas se insertan en una única transacción: si falla la base de datos se deshace todo.
 * Al terminar muestra un resumen de leídas, insertadas y rechazadas.
 */
public class ImportadorJson {

	/**
	 * Lee el fichero JSON e inserta las cosechas válidas.
	 *
	 * @param ruta ruta del fichero JSON (UTF-8)
	 * @return {@code true} si se confirmó la transacción, {@code false} si se deshizo por un error de base de datos
	 * @throws IOException si no se puede leer el fichero
	 * @throws SQLException si falla la conexión con la base de datos
	 */
	public static boolean leer(String ruta) throws IOException, SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				BufferedReader br = new BufferedReader(new FileReader(ruta, StandardCharsets.UTF_8))) {

			conexion.setAutoCommit(false);

			Gson gson = new Gson();
			Type tipo = new TypeToken<List<CosechaJson>>() {
			}.getType();
			List<CosechaJson> lista = gson.fromJson(br, tipo);

			int parcelaVacia = 0;
			int cantidadInvalida = 0;
			int graduacionInvalida = 0;
			int parcelaInexistente = 0;

			int insertadas = 0;
			int noInsertadas = 0;

			int n = 0;
			try {
				for (CosechaJson c : lista) {
					n++;
					// comprobamos primero el null para evitar el NullPointerException
					if (c.getParcela() == null || c.getParcela().trim().isEmpty()) {
						System.out.printf("Cosecha %d con parcela vacía \n", n);
						parcelaVacia++;
						continue;
					}

					// damos por valida cosecha con cantidad 0.0 , tanto por ausencia en el JSon
					// como por pérdida real
					if (c.getCantidad() < 0) {
						System.out.printf("Cosecha %d con cantidad no válida \n", n);
						cantidadInvalida++;
						continue;
					}

					// graduacion solo se rechaza si no es null y es ≤ 0,orden importa para evitar
					// NullPointerException
					if (c.getGraduacion() != null && c.getGraduacion() <= 0) {
						System.out.printf("Cosecha %d con graduación no válida \n", n);
						graduacionInvalida++;
						continue;
					}

					// idParcela se usa en para crear parcela
					Integer idParcela = DAOParcela.buscarIdPorNombre(c.getParcela().trim());

					if (idParcela == null) {
						System.out.printf("Cosecha %d con parcela inexistente \n", n);
						parcelaInexistente++;
						continue;
					}

					Parcela parcela = new Parcela();
					parcela.setId(idParcela);
					try {
						DAOCosecha.crearCosecha(conexion,
								new Cosecha(0, c.getAnualidad(), parcela, c.getCantidad(), c.getGraduacion()));
						insertadas++;
					} catch (SQLIntegrityConstraintViolationException e) {
						noInsertadas++;
						System.out.printf("Cosecha %d no insertada (duplicada o dato no permitido) \n", n);
						continue;
					}
				} // fin for

				conexion.commit();

			} catch (SQLException e) {
				System.out.printf("Error en la BD: %s \n", e.getMessage());
				conexion.rollback();
				return false;
			}

			// Resumen del importador (solo se llega aquí si hubo commit)
			int rechazadas = parcelaVacia + cantidadInvalida + graduacionInvalida + parcelaInexistente;

			System.out.printf("\n========RESUMEN DEL IMPORTADOR JSON======\n");
			System.out.printf("Cosechas leídas:               %d\n", n);
			System.out.printf("Cosechas insertadas:           %d\n", insertadas);
			System.out.printf("No insertadas (BD):            %d\n", noInsertadas);
			System.out.printf("Rechazadas por datos:          %d\n", rechazadas);
			System.out.printf("  Parcela vacía:               %d\n", parcelaVacia);
			System.out.printf("  Parcela inexistente:         %d\n", parcelaInexistente);
			System.out.printf("  Cantidad no válida:          %d\n", cantidadInvalida);
			System.out.printf("  Graduación no válida:        %d\n", graduacionInvalida);
			System.out.printf("=========================================\n");

		}
		return true;
	}

}
