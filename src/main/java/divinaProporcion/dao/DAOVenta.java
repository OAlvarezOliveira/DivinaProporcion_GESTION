package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Producto;
import divinaProporcion.modelo.Venta;

public class DAOVenta {

	private static final String SQL_BASE_VENTA = """
			SELECT
			    pr.id AS idPRODUCTO,
			    pr.nombre AS NombrePRODUCTO,
			    pr.id_cosecha AS idCOSECHA,
			    pr.botellas AS botellasPRODUCTO,
			    v.id AS IDVenta,
			    v.cantidad AS cantidadVenta,
			    v.fecha AS FechaVenta,
			    ch.id AS idCosecha,
			    ch.anualidad AS anualidadCosecha,
				ch.cantidad AS cantidadCosecha,
				ch.graduacion AS graduacionCosecha,
						        par.id AS idParcela,
					        par.nombre AS nombreParcela,
					        par.extension AS extensionParcela,
					        par.ubicacion AS ubicacionParcela,
					        		        var.id AS idVariedad,
					        var.nombre AS nombreVariedad,
					        var.color AS colorVariedad
			FROM venta v
			JOIN producto pr ON v.id_producto = pr.id
			JOIN cosecha ch ON ch.id = pr.id_cosecha
			JOIN parcela par ON ch.id_parcela = par.id
			JOIN variedad var ON par.id_variedad = var.id

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
		
		
		//public Venta(int id, Producto producto, int cantidad, LocalDate fecha)
		int id;
		Producto producto;
		int cantidad;
		LocalDate fecha = null;

		producto = DAOProducto.mapearProducto(rs);
		id = rs.getInt("IDVenta");
		cantidad = rs.getInt("cantidadVenta");
		java.sql.Date sqlDate = rs.getDate("FechaVenta");
		if (sqlDate != null) {
		    fecha = sqlDate.toLocalDate();
		}

		return new Venta( id, producto, cantidad, fecha);

	}

}
