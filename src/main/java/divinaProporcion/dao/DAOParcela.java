package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.Color;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Variedad;

/**
 * Acceso a datos de la tabla {@code parcela} mediante JDBC.
 * <p>
 * Ofrece el CRUD completo (crear, listar, editar y borrar) y cuatro filtros de
 * consulta (por nombre, extensión, ubicación y variedad). Recibe y devuelve
 * objetos del modelo ({@link Parcela}) y no interactúa con la consola: las
 * excepciones de JDBC se propagan para que las gestione el controlador. Todas
 * las consultas usan {@link PreparedStatement}, de modo que los valores viajan
 * separados del SQL y no hay inyección SQL.
 * </p>
 * <p>
 * Cada parcela tiene plantada una {@link Variedad}, por lo que las consultas de
 * lectura unen ({@code JOIN}) las tablas {@code parcela} y {@code variedad} y
 * devuelven la parcela con su variedad ya cargada.
 * </p>
 */
public class DAOParcela {

	// Acaba en espacio a propósito: después se le pega "ORDER BY" o "WHERE"
	private static final String SQL_BASE_PARCELA ="SELECT p.id AS idParcela, p.nombre AS nombreParcela, p.extension AS extensionParcela, p.ubicacion AS ubicacionParcela, "
			+ "v.id AS idVariedad, v.nombre AS nombreVariedad, v.color AS colorVariedad " + "FROM parcela AS p "
			+ "JOIN variedad AS v ON p.id_variedad = v.id ";

	private static final String SQL_FILTRAR_NOMBRE = SQL_BASE_PARCELA + "WHERE p.nombre LIKE ? ORDER BY p.nombre";
	private static final String SQL_FILTRAR_UBICACION = SQL_BASE_PARCELA + "WHERE p.ubicacion LIKE ? ORDER BY p.nombre";
	private static final String SQL_FILTRAR_EXTENSION = SQL_BASE_PARCELA + "WHERE p.extension >= ? ORDER BY p.extension";
	private static final String SQL_FILTRAR_VARIEDAD = SQL_BASE_PARCELA + "WHERE v.id = ? ORDER BY p.nombre";

	private static final String SQL_CREAR_PARCELA = "INSERT INTO parcela (nombre,extension,id_variedad,ubicacion) VALUES (?,?,?,?)";
	private static final String SQL_EDITAR_PARCELA = "UPDATE parcela SET nombre = ?, extension = ?, id_variedad = ?, ubicacion = ? WHERE id = ?";
	private static final String SQL_BORRAR_PARCELA = "DELETE FROM parcela WHERE id = ?";

	/**
	 * Inserta una nueva parcela. El {@code id} del objeto se ignora porque lo
	 * genera la base de datos (autonumérico). De la variedad solo se usa su
	 * {@code id}, que debe existir en la tabla {@code variedad}.
	 *
	 * @param p parcela a guardar, con nombre, extensión y variedad informados; la
	 *          ubicación puede ser {@code null}
	 * @return {@code true} si se insertó la fila
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si ya existe una
	 *                      parcela con ese nombre o si la variedad no existe
	 */
	public static boolean crearParcela(Parcela p) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_CREAR_PARCELA)) {

			ps.setString(1, p.getNombre());
			ps.setDouble(2, p.getExtension());
			ps.setInt(3, p.getVariedad().getId());
			ps.setString(4, p.getUbicacion());
			int filas = ps.executeUpdate();
			return filas > 0;
		}

	}

	/**
	 * Busca las parcelas cuyo nombre contiene el texto indicado, sin distinguir
	 * mayúsculas de minúsculas. Un texto vacío devuelve todas las parcelas.
	 *
	 * @param nombre texto que debe aparecer en cualquier parte del nombre
	 * @return parcelas encontradas, ordenadas por nombre; lista vacía si ninguna
	 *         coincide
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Parcela> filtrarPorNombre(String nombre) throws SQLException {

		List<Parcela> lista = new ArrayList<>();
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_FILTRAR_NOMBRE)) {

			ps.setString(1, "%" + nombre + "%");
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					lista.add(mapearParcela(rs));

				}
			}

		}

		return lista;
	}

	/**
	 * Busca las parcelas cuya extensión es igual o mayor que el mínimo indicado.
	 *
	 * @param extension extensión mínima, en hectáreas
	 * @return parcelas encontradas, ordenadas de menor a mayor extensión; lista
	 *         vacía si ninguna alcanza el mínimo
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Parcela> filtrarPorExtension(double extension) throws SQLException {

		List<Parcela> lista = new ArrayList<>();
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_FILTRAR_EXTENSION)) {

			ps.setDouble(1, extension);
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					lista.add(mapearParcela(rs));

				}
			}

		}

		return lista;
	}

	/**
	 * Busca las parcelas cuya ubicación contiene el texto indicado, sin distinguir
	 * mayúsculas de minúsculas. Las parcelas sin ubicación ({@code NULL}) nunca
	 * coinciden, ni siquiera con un texto vacío.
	 *
	 * @param ubicacion texto que debe aparecer en cualquier parte de la ubicación
	 * @return parcelas encontradas, ordenadas por nombre; lista vacía si ninguna
	 *         coincide
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Parcela> filtrarPorUbicacion(String ubicacion) throws SQLException {

		List<Parcela> lista = new ArrayList<>();
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_FILTRAR_UBICACION)) {

			ps.setString(1, "%" + ubicacion + "%");
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					lista.add(mapearParcela(rs));

				}
			}

		}

		return lista;
	}

	/**
	 * Busca las parcelas que tienen plantada la variedad indicada.
	 *
	 * @param idVariedad identificador de la variedad
	 * @return parcelas encontradas, ordenadas por nombre; lista vacía si la
	 *         variedad no existe o no tiene parcelas
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Parcela> filtrarPorVariedad(int idVariedad) throws SQLException {

		List<Parcela> lista = new ArrayList<>();
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_FILTRAR_VARIEDAD)) {

			ps.setInt(1, idVariedad);
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					lista.add(mapearParcela(rs));

				}
			}

		}

		return lista;
	}

	/**
	 * Obtiene todas las parcelas, cada una con su variedad, ordenadas por nombre.
	 *
	 * @return lista con todas las parcelas; vacía si la tabla no tiene filas
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Parcela> listarParcela() throws SQLException {
		List<Parcela> lista = new ArrayList<>();
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BASE_PARCELA + "ORDER BY p.nombre");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				lista.add(mapearParcela(rs));
			}

		}

		return lista;
	}

	/**
	 * Convierte la fila actual del {@link ResultSet} en una {@link Parcela} con su
	 * {@link Variedad}. Las columnas se leen por los alias de la consulta base
	 * ({@code SQL_BASE_PARCELA}), por lo que solo sirve para resultados que la usen.
	 * No avanza el cursor: de eso se encarga quien llama.
	 *
	 * @param rs resultado ya colocado en la fila que se quiere leer
	 * @return la parcela construida con los datos de esa fila
	 * @throws SQLException si falla la lectura de alguna columna
	 */
	private static Parcela mapearParcela(ResultSet rs) throws SQLException {

		int idParcela = rs.getInt("idParcela");
		String nombreParcela = rs.getString("nombreParcela");
		double extensionParcela = rs.getDouble("extensionParcela");

		int idVariedad = rs.getInt("idVariedad");
		String nombreVariedad = rs.getString("nombreVariedad");
		// BD en minúsculas ('tinto'), enum en mayúsculas (TINTO)
		Color color = Color.valueOf(rs.getString("colorVariedad").toUpperCase());

		String ubicacionParcela = rs.getString("ubicacionParcela");

		return (new Parcela(idParcela, nombreParcela, extensionParcela, new Variedad(idVariedad, nombreVariedad, color),
				ubicacionParcela));
	}

	/**
	 * Modifica nombre, extensión, variedad y ubicación de la parcela cuyo
	 * {@code id} coincide con el del objeto recibido.
	 *
	 * @param p parcela con el {@code id} a modificar y los nuevos valores
	 * @return {@code true} si se modificó una fila; {@code false} si no se modificó
	 *         ninguna, lo que implica que no existe ninguna parcela con ese id
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si el nuevo
	 *                      nombre ya lo usa otra parcela o si la variedad no existe
	 */
	public static boolean editarParcela(Parcela p) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_EDITAR_PARCELA)) {

			ps.setString(1, p.getNombre());
			ps.setDouble(2, p.getExtension());
			ps.setInt(3, p.getVariedad().getId());
			ps.setString(4, p.getUbicacion());
			ps.setInt(5, p.getId());

			int filas = ps.executeUpdate();
			return filas > 0;
		}

	}

	/**
	 * Elimina la parcela con el id indicado.
	 *
	 * @param id identificador de la parcela a borrar
	 * @return {@code true} si se borró una fila; {@code false} si no se borró
	 *         ninguna, lo que implica que no existe ninguna parcela con ese id
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si la parcela
	 *                      tiene cosechas asociadas
	 */
	public static boolean borrarParcela(int id) throws SQLException {
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_PARCELA)) {

			ps.setInt(1, id);
			int filas = ps.executeUpdate();
			return filas > 0;
		}
	}

}
