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

	public static List<Venta> listarVenta() throws SQLException {

		List<Venta> lista = new ArrayList<>();

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BASE_VENTA + "ORDER BY v.id , v.fecha");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				lista.add(mapearProducto(rs));

			}

		}
		return lista;

	}

	private static Venta mapearProducto(ResultSet rs) throws SQLException {

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
