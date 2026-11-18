package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.Producto;

public class DAOProducto {

	// Acaba en espacio a propósito: después se le pega "ORDER BY" o "WHERE"
	private static final String SQL_BASE_PRODUCTO = """
			SELECT
			    c.id AS idCosecha,
			    pr.id AS idProducto,
			    pr.nombre AS nombreProducto,
			    pr.botellas AS botellasProducto,
			    c.anualidad AS anualidadCosecha,
			    c.cantidad AS cantidadCosecha,
			    c.graduacion AS graduacionCosecha,
			    p.id AS idParcela,
			    p.nombre AS nombreParcela,
			    p.extension AS extensionParcela,
			    p.ubicacion AS ubicacionParcela,
			    v.id AS idVariedad,
			    v.nombre AS nombreVariedad,
			    v.color AS colorVariedad
			FROM producto AS pr
			JOIN cosecha AS c ON pr.id_cosecha = c.id
			JOIN parcela AS p ON c.id_parcela = p.id
			JOIN variedad AS v ON p.id_variedad = v.id
			""";

	private static final String SQL_STOCK = """
			SELECT
			    pr.id AS idProducto,
			    pr.nombre AS nombreProducto,
			    pr.id_cosecha AS idCosecha,
			    pr.botellas AS botellasProducto,
			    COALESCE(SUM(v.cantidad), 0) AS botellasVendidas,
			    (pr.botellas - COALESCE(SUM(v.cantidad), 0)) AS Stock

			FROM producto AS pr
			LEFT JOIN venta AS v ON pr.id = v.id_producto
			GROUP BY pr.id, pr.nombre, pr.botellas
											""";

	private static final String SQL_BOTELLAS_DISPONIBLES = """
			SELECT
			pr.botellas - COALESCE(SUM(v.cantidad), 0) AS stock
			FROM producto pr
			LEFT JOIN venta v ON v.id_producto = pr.id WHERE pr.id = ? GROUP BY pr.id
			   """;

	public static List<Producto> listarProducto() throws SQLException {

		List<Producto> lista = new ArrayList<>();

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BASE_PRODUCTO + "ORDER BY pr.nombre");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				lista.add(mapearProducto(rs));
			}

		}
		return lista;

	}

	public static List<Producto> listarStockProducto() throws SQLException {

		List<Producto> lista = new ArrayList<>();

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_STOCK + "ORDER BY pr.nombre");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Producto p = mapearProducto(rs);
				p.setStock(rs.getInt("stock"));
				lista.add(p);
			}

		}
		return lista;

	}

	static Producto mapearProducto(ResultSet rs) throws SQLException {
		int idProducto;
		String nombreProducto;
		int botellasProducto;
		int idCosecha;

		idProducto = rs.getInt("idProducto");
		nombreProducto = rs.getString("nombreProducto");
		idCosecha = rs.getInt("idCosecha");
		botellasProducto = rs.getInt("botellasProducto");

		return new Producto(idProducto, nombreProducto, idCosecha, botellasProducto);

	}

	static int calcularStock(int id) throws SQLException {

		try (Connection conexion = ConexionDB.conectaDB();
				PreparedStatement ps = conexion.prepareStatement(SQL_BOTELLAS_DISPONIBLES)) {
			int stock = -1;
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					stock = rs.getInt("stock");
				}
			}

			return stock;
		}

	}
	
	static int calcularStock  (Connection conexion, int id) throws SQLException {

		try (PreparedStatement ps = conexion.prepareStatement(SQL_BOTELLAS_DISPONIBLES)) {
			int stock = -1;
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					stock = rs.getInt("stock");
				}
			}

			return stock;
		}

	}

}
