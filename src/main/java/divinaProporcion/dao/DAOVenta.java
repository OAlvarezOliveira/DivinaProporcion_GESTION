package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.LineaInforme;
import divinaProporcion.modelo.Venta;

/**
 * Acceso a datos de la tabla {@code venta}: listado, registro con control de stock
 * y consulta para el informe anual.
 */
public class DAOVenta {

	private static final String SQL_BASE_VENTA = """
			SELECT
			    v.id AS idVenta,
			    v.id_producto AS idProducto,
			    pr.nombre AS nombreProducto,
			    v.cantidad AS cantidadVenta,
			    v.fecha AS fechaVenta
			FROM venta v
			JOIN producto pr ON v.id_producto = pr.id
			""";

	private static final String SQL_INSERTAR_VENTA = """
			INSERT INTO venta (id_producto, cantidad, fecha) VALUES (?, ?, ?)
						""";

	private static final String SQL_INFORME_VENTAS = """
			SELECT YEAR(v.fecha) as anio,
			pr.nombre AS nombreProducto,
			SUM(v.cantidad) as cantidadTotal,
			COUNT(v.id ) as VentasTotales
			FROM venta v
						JOIN producto pr ON v.id_producto = pr.id
						GROUP BY YEAR(v.fecha), pr.id, pr.nombre
						ORDER BY anio, nombreProducto ,pr.id
						""";

	/**
	 * Lista todas las ventas con el nombre del producto, ordenadas por id y fecha.
	 *
	 * @return lista de ventas
	 * @throws SQLException si falla la consulta
	 */
	public static List<Venta> listarVenta() throws SQLException {

		List<Venta> lista = new ArrayList<>();

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BASE_VENTA + "ORDER BY v.id , v.fecha");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				lista.add(mapearVenta(rs));

			}

		}
		return lista;

	}

	/**
	 * Convierte la fila actual del {@code ResultSet} en una {@link Venta}.
	 *
	 * @param rs resultado posicionado en una fila válida
	 * @return la venta de esa fila
	 * @throws SQLException si falta alguna columna esperada
	 */
	private static Venta mapearVenta(ResultSet rs) throws SQLException {

		// public Venta(int id, int idProducto, String nombreProducto,int cantidad,
		// LocalDate fecha) {
		int id;
		int idProducto;
		String nombreProducto;
		int cantidad;
		LocalDate fecha = null;

		id = rs.getInt("IDVenta");
		idProducto = rs.getInt("idProducto");
		nombreProducto = rs.getString("nombreProducto");
		cantidad = rs.getInt("cantidadVenta");
		java.sql.Date sqlDate = rs.getDate("FechaVenta");
		if (sqlDate != null) {
			fecha = sqlDate.toLocalDate();
		}

		return new Venta(id, idProducto, nombreProducto, cantidad, fecha);

	}

	/**
	 * Registra una venta en una transacción: comprueba el stock y la inserta, o no hace nada.
	 * Si ocurre cualquier error se deshace con {@code rollback} y se relanza la excepción.
	 *
	 * @param venta venta a registrar (producto, cantidad y fecha)
	 * @throws IllegalArgumentException si el producto no existe o la cantidad supera el stock
	 * @throws SQLException si falla la base de datos
	 */
	public static void registrarVenta(Venta venta) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB()) {
			// transacción: comprobar el stock y registrar la venta se hacen juntos o no se
			// hace nada
			conexion.setAutoCommit(false);

			try {
				int stock = DAOProducto.calcularStock(conexion, venta.getIdProducto());

				if (stock == -1) {
					throw new IllegalArgumentException("Producto inexistente");
				} else if (venta.getCantidad() > stock) {

					throw new IllegalArgumentException("Stock insuficiente: quedan " + stock);

				} else {
					try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR_VENTA)) {

						ps.setInt(1, venta.getIdProducto());
						ps.setInt(2, venta.getCantidad());
						ps.setDate(3, java.sql.Date.valueOf(venta.getFecha()));
						ps.executeUpdate();
					}
					conexion.commit();
				}

			} catch (SQLException | RuntimeException e) {
				conexion.rollback();
				throw e;
			}

		}

	}

	/**
	 * Obtiene las ventas agrupadas por año y producto para el informe anual.
	 *
	 * @return una línea por año y producto, ordenadas por año y nombre
	 * @throws SQLException si falla la consulta
	 */
	public static List<LineaInforme> informeVentas() throws SQLException {

		List<LineaInforme> lista = new ArrayList<>();

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_INFORME_VENTAS);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				int anio = rs.getInt("anio");
				String nombreProducto = rs.getString("nombreProducto");
				int cantidadTotal = rs.getInt("cantidadTotal");
				int numVentas = rs.getInt("ventasTotales");

				lista.add(new LineaInforme(anio, nombreProducto, cantidadTotal, numVentas));

			}

		}
		return lista;

	}

}
