package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.Variedad;
import divinaProporcion.modelo.Color;

/**
 * Acceso a datos de la tabla {@code variedad} mediante JDBC.
 * <p>
 * Ofrece las cuatro operaciones del CRUD (crear, listar, editar y borrar).
 * Recibe y devuelve objetos del modelo ({@link Variedad}) y no interactúa con
 * la consola: las excepciones de JDBC se propagan para que las gestione el
 * controlador. Todas las consultas usan {@link PreparedStatement}, de modo que
 * los valores viajan separados del SQL y no hay inyección SQL.
 * </p>
 * <p>
 * En la BD el color se guarda en minúsculas ({@code 'tinto'}); en Java se usa
 * el enum {@link Color}, por lo que cada lectura y escritura convierte entre
 * ambas formas.
 * </p>
 */
public class DAOVariedad {

	private static final String SQL_LISTAR_VARIEDAD = "SELECT id,nombre,color FROM variedad ORDER BY nombre";
	private static final String SQL_CREAR_VARIEDAD = "INSERT INTO variedad (nombre,color) VALUES (?,?)";
	private static final String SQL_EDITAR_VARIEDAD = "UPDATE variedad SET nombre = ?, color = ? WHERE id = ?";
	private static final String SQL_BORRAR_VARIEDAD = "DELETE FROM variedad WHERE id = ?";

	/**
	 * Inserta una nueva variedad. El {@code id} del objeto se ignora porque lo
	 * genera la base de datos (autonumérico).
	 *
	 * @param v variedad a guardar, con nombre y color informados
	 * @return {@code true} si se insertó la fila
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si ya existe una
	 *                      variedad con ese nombre
	 */
	public static boolean crearVariedad(Variedad v) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_CREAR_VARIEDAD)) {

			ps.setString(1, v.getNombre());
			ps.setString(2, v.getColor().name().toLowerCase());
			int filas = ps.executeUpdate();
			return filas > 0;
		}
	}

	/**
	 * Obtiene todas las variedades de la tabla, ordenadas por nombre.
	 *
	 * @return lista con todas las variedades; vacía si la tabla no tiene filas
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Variedad> listarVariedad() throws SQLException {
		List<Variedad> lista = new ArrayList<>();

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_LISTAR_VARIEDAD);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				Color color = Color.valueOf(rs.getString("color").toUpperCase());

				lista.add(new Variedad(id, nombre, color));
			}

		}

		return lista;

	}

	/**
	 * Modifica el nombre y el color de la variedad cuyo {@code id} coincide con el
	 * del objeto recibido.
	 *
	 * @param v variedad con el {@code id} a modificar y los nuevos valores
	 * @return {@code true} si se modificó una fila; {@code false} si no se modificó ninguna,
	 * lo que implica que no existe ninguna variedad con ese id
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static boolean editarVariedad(Variedad v) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_EDITAR_VARIEDAD)) {

			ps.setString(1, v.getNombre());
			ps.setString(2, v.getColor().name().toLowerCase());
			ps.setInt(3, v.getId());
			int filas = ps.executeUpdate();
			return filas > 0;
		}
	}

	/**
	 * Elimina la variedad con el id indicado.
	 *
	 * @param id identificador de la variedad a borrar
	 * @return {@code true} si se borró una fila; {@code false} si no se borró ninguna,
	 * lo que implica que no existe ninguna variedad con ese id
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static boolean borrarVariedad(int id) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_VARIEDAD)) {

			ps.setInt(1, id);
			int filas = ps.executeUpdate();
			return filas > 0;
		}

	}

}
