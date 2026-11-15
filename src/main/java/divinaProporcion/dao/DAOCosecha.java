package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.Color;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Variedad;

/**
 * Acceso a datos de la tabla {@code cosecha} mediante JDBC.
 * <p>
 * Ofrece el CRUD completo (crear, listar, editar y borrar). Recibe y devuelve
 * objetos del modelo ({@link Cosecha}) y no interactúa con la consola: las
 * excepciones de JDBC se propagan para que las gestione el controlador. Todas
 * las consultas usan {@link PreparedStatement}, de modo que los valores viajan
 * separados del SQL y no hay inyección SQL.
 * </p>
 * <p>
 * Cada cosecha pertenece a una {@link Parcela}, que a su vez tiene plantada una
 * {@link Variedad}. Por eso la lectura une ({@code JOIN}) las tablas
 * {@code cosecha}, {@code parcela} y {@code variedad} y devuelve la cosecha con
 * su parcela y su variedad ya cargadas. Una parcela solo puede tener una
 * cosecha por año.
 * </p>
 * <p>
 * La graduación es opcional: puede ser {@code null} (por ejemplo, si no se
 * cosechó nada y no hay qué medir) y se guarda como {@code NULL} en la BD.
 * </p>
 */
public class DAOCosecha {

	private static final String SQL_CREAR_COSECHA = "INSERT INTO cosecha (anualidad,id_parcela,cantidad,graduacion) VALUES (?,?,?,?)";
	// Acaba en espacio a propósito: después se le pega "ORDER BY" o "WHERE"
	private static final String SQL_BASE_COSECHA ="SELECT" + "    c.id AS idCosecha,"
			+ "    c.anualidad AS anualidadCosecha," + "    c.cantidad AS cantidadCosecha,"
			+ "    c.graduacion AS graduacionCosecha," + "    p.id AS idParcela," + "    p.nombre AS nombreParcela,"
			+ "    p.extension AS extensionParcela," + "    p.ubicacion AS ubicacionParcela,"
			+ "    v.id AS idVariedad," + "    v.nombre AS nombreVariedad," + "    v.color AS colorVariedad"
			+ " FROM cosecha AS c" + " JOIN parcela AS p ON c.id_parcela = p.id"
			+ " JOIN variedad AS v ON p.id_variedad = v.id ";

	private static final String SQL_EDITAR_COSECHA = "UPDATE cosecha SET anualidad = ?, id_parcela = ?, cantidad = ? ,graduacion = ? WHERE id = ?";
	private static final String SQL_BORRAR_COSECHA = "DELETE FROM cosecha WHERE id = ?";

	/**
	 * Inserta una nueva cosecha. El {@code id} del objeto se ignora porque lo
	 * genera la base de datos (autonumérico). De la parcela solo se usa su
	 * {@code id}, que debe existir en la tabla {@code parcela}.
	 *
	 * @param c cosecha a guardar, con año, parcela y cantidad informados; la
	 *          graduación puede ser {@code null}
	 * @return {@code true} si se insertó la fila
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si la parcela
	 *                      no existe o si ya hay una cosecha de esa parcela en ese
	 *                      año
	 */
	public static boolean crearCosecha(Cosecha c) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_CREAR_COSECHA)) {

			ps.setInt(1, c.getAnualidad());
			ps.setInt(2, c.getParcela().getId());
			ps.setDouble(3, c.getCantidad());

			// graduacion es opcional: con null, setDouble falla, así que uso setNull
			if (c.getGraduacion() == null) {
				ps.setNull(4, Types.DECIMAL);
			} else {
				ps.setDouble(4, c.getGraduacion());
			}

			int filas = ps.executeUpdate();
			return filas > 0;
		}

	}
	
	//Para una importación con transacción, o entran todas o ninguna
	public static boolean crearCosecha(Connection conexion, Cosecha c) throws SQLException{
		
		
		try ( PreparedStatement ps = conexion.prepareStatement(SQL_CREAR_COSECHA)) {

			ps.setInt(1, c.getAnualidad());
			ps.setInt(2, c.getParcela().getId());
			ps.setDouble(3, c.getCantidad());

			// graduacion es opcional: con null, setDouble falla, así que uso setNull
			if (c.getGraduacion() == null) {
				ps.setNull(4, Types.DECIMAL);
			} else {
				ps.setDouble(4, c.getGraduacion());
			}

			int filas = ps.executeUpdate();
			return filas > 0;
		}
		
		
		
	}

	/**
	 * Obtiene todas las cosechas, cada una con su parcela y la variedad de esta,
	 * ordenadas por nombre de parcela y, dentro de cada parcela, por año.
	 *
	 * @return lista con todas las cosechas; vacía si la tabla no tiene filas
	 * @throws SQLException si falla el acceso a la BD
	 */
	public static List<Cosecha> listarCosecha() throws SQLException {

		List<Cosecha> lista = new ArrayList<>();
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BASE_COSECHA + "ORDER BY p.nombre, c.anualidad");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				lista.add(mapearCosecha(rs));
			}

		}

		return lista;

	}

	/**
	 * Convierte la fila actual del {@link ResultSet} en una {@link Cosecha} con su
	 * {@link Parcela} y su {@link Variedad}. Las columnas se leen por los alias de
	 * la consulta base ({@code SQL_BASE_COSECHA}), por lo que solo sirve para
	 * resultados que la usen. La graduación se devuelve como {@code null} si la
	 * columna es {@code NULL}. No avanza el cursor: de eso se encarga quien llama.
	 *
	 * @param rs resultado ya colocado en la fila que se quiere leer
	 * @return la cosecha construida con los datos de esa fila
	 * @throws SQLException si falla la lectura de alguna columna
	 */
	private static Cosecha mapearCosecha(ResultSet rs) throws SQLException {

		int idCosecha = rs.getInt("idCosecha");
		int anualidadCosecha = rs.getInt("anualidadCosecha");
		double cantidadCosecha = rs.getDouble("cantidadCosecha");

		// getDouble da 0.0 si es NULL; wasNull() solo mira la última lectura, va justo después
		double valor = rs.getDouble("graduacionCosecha");
		Double graduacionCosecha; // invoco a la clase que admite null en lugar del tipo de dato
		if (rs.wasNull()) {
			graduacionCosecha = null;
		} else {
			graduacionCosecha = valor;
		}

		int idParcela = rs.getInt("idParcela");
		String nombreParcela = rs.getString("nombreParcela");
		double extensionParcela = rs.getDouble("extensionParcela");
		String ubicacionParcela = rs.getString("ubicacionParcela");

		int idVariedad = rs.getInt("idVariedad");
		String nombreVariedad = rs.getString("nombreVariedad");
		// BD en minúsculas ('tinto'), enum en mayúsculas (TINTO)
		Color colorVariedad = Color.valueOf(rs.getString("colorVariedad").toUpperCase());

		return (new Cosecha(idCosecha, anualidadCosecha,
				(new Parcela(idParcela, nombreParcela, extensionParcela,
						new Variedad(idVariedad, nombreVariedad, colorVariedad), ubicacionParcela)),
				cantidadCosecha, graduacionCosecha));

	}

	/**
	 * Modifica año, parcela, cantidad y graduación de la cosecha cuyo {@code id}
	 * coincide con el del objeto recibido. De la parcela solo se usa su
	 * {@code id}.
	 *
	 * @param c cosecha con el {@code id} a modificar y los nuevos valores; la
	 *          graduación puede ser {@code null}
	 * @return {@code true} si se modificó una fila; {@code false} si no se
	 *         modificó ninguna, lo que implica que no existe ninguna cosecha con
	 *         ese id
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si la parcela
	 *                      no existe o si ya hay otra cosecha de esa parcela en ese
	 *                      año
	 */
	public static boolean editarCosecha(Cosecha c) throws SQLException {
		
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_EDITAR_COSECHA)) {
			
			ps.setInt(1, c.getAnualidad());
			ps.setInt(2, c.getParcela().getId());
			ps.setDouble(3, c.getCantidad());

			// graduacion es opcional: con null, setDouble falla, así que uso setNull
			if (c.getGraduacion() == null) {
			    ps.setNull(4, Types.DECIMAL);
			} else {
			    ps.setDouble(4, c.getGraduacion());
			}
			ps.setInt(5, c.getId());

			int filas = ps.executeUpdate();
			return filas > 0;
		}

	}

	/**
	 * Elimina la cosecha con el id indicado.
	 *
	 * @param id identificador de la cosecha a borrar
	 * @return {@code true} si se borró una fila; {@code false} si no se borró
	 *         ninguna, lo que implica que no existe ninguna cosecha con ese id
	 * @throws SQLException si falla el acceso a la BD, por ejemplo si la cosecha
	 *                      tiene productos asociados
	 */
	public static boolean borrarCosecha(int id) throws SQLException {
		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_COSECHA)) {

			ps.setInt(1, id);
			int filas = ps.executeUpdate();
			return filas > 0;
		}
	}

}
