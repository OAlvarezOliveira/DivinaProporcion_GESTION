package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Producto;
import divinaProporcion.modelo.Variedad;
import divinaProporcion.modelo.Venta;

/**
 * Acceso a datos para restaurar la BD completa desde un backup
 *
 */
public class DAOBackup {
	
	private static final String SQL_INSERTAR_VARIEDAD = "INSERT INTO variedad (id, nombre, color) VALUES (?, ?, ?)";
	private static final String SQL_INSERTAR_PARCELA  = "INSERT INTO parcela (id, nombre, extension, id_variedad, ubicacion) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_INSERTAR_COSECHA  = "INSERT INTO cosecha (id, anualidad, id_parcela, cantidad, graduacion) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_INSERTAR_PRODUCTO = "INSERT INTO producto (id, nombre, id_cosecha, botellas) VALUES (?, ?, ?, ?)";
	private static final String SQL_INSERTAR_VENTA    = "INSERT INTO venta (id, id_producto, cantidad, fecha) VALUES (?, ?, ?, ?)";

	private static final String SQL_BORRAR_VENTAS    = "DELETE FROM venta";
	private static final String SQL_BORRAR_PRODUCTOS = "DELETE FROM producto";
	private static final String SQL_BORRAR_COSECHAS  = "DELETE FROM cosecha";
	private static final String SQL_BORRAR_PARCELAS  = "DELETE FROM parcela";
	private static final String SQL_BORRAR_VARIEDADES = "DELETE FROM variedad";
	
	
	/**
	 * Borra todo el contenido de las cinco tablas y lo reinserta , en una sola transacción en caso de fallo rollback
	 *
	 * Se borra de venta a variedad y se inserta al revés, por las claves foráneas
	 * Se conservan los ids originales para que las referencias sigan apuntando a lo mismo.
	 * 
	 * @param  variedades  lista de variedades que se insertan con su id original
	 * @param  parcelas    lista de parcelas a insertar (cada una referencia su variedad)
	 * @param  cosechas    lista de cosechas a insertar (cada una referencia su parcela)
	 * @param  productos   lista de productos a insertar (cada una referencia su cosecha)
	 * @param  ventas      lista de ventas a insertar (cada una referencia su producto)
	 * @throws SQLException si falla la conexión o alguna sentencia; en ese caso se hace rollback y la BD queda como estaba
	 */
	public static void restaurar(List<Variedad> variedades, List<Parcela> parcelas,
	        List<Cosecha> cosechas, List<Producto> productos, List<Venta> ventas) throws SQLException{
		
		
		try (Connection conexion = ConexionDB.conectaDB()) {

			conexion.setAutoCommit(false);
			
			try {
				// orden , no puedes borrar una fila a la que otra tabla todavía apunta.
					try (PreparedStatement ps = conexion.prepareStatement( SQL_BORRAR_VENTAS)) {

						ps.executeUpdate();
					}
					

					try (PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_PRODUCTOS)) {

						ps.executeUpdate();
					}
					

					try (PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_COSECHAS)) {

						ps.executeUpdate();
					}
					

					try (PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_PARCELAS)) {

						ps.executeUpdate();
					}
					

					try (PreparedStatement ps = conexion.prepareStatement(SQL_BORRAR_VARIEDADES)) {

						ps.executeUpdate();
					}

					// Inserción en orden inverso al borrado, con los ids originales
					try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR_VARIEDAD)) {
						for (Variedad v : variedades) {
							ps.setInt(1, v.getId());
							ps.setString(2, v.getNombre());
							ps.setString(3, v.getColor().name().toLowerCase());
							ps.executeUpdate();
						}
					}

					try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR_PARCELA)) {
						for (Parcela p : parcelas) {
							ps.setInt(1, p.getId());
							ps.setString(2, p.getNombre());
							ps.setDouble(3, p.getExtension());
							ps.setInt(4, p.getVariedad().getId());
							ps.setString(5, p.getUbicacion());
							ps.executeUpdate();
						}
					}

					try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR_COSECHA)) {
						for (Cosecha c : cosechas) {
							ps.setInt(1, c.getId());
							ps.setInt(2, c.getAnualidad());
							ps.setInt(3, c.getParcela().getId());
							ps.setDouble(4, c.getCantidad());
							// graduacion es opcional: con null, setDouble falla, así que uso setNull
							if (c.getGraduacion() == null) {
								ps.setNull(5, Types.DECIMAL);
							} else {
								ps.setDouble(5, c.getGraduacion());
							}
							ps.executeUpdate();
						}
					}

					try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR_PRODUCTO)) {
						for (Producto pr : productos) {
							ps.setInt(1, pr.getId());
							ps.setString(2, pr.getNombre());
							ps.setInt(3, pr.getIdCosecha());
							ps.setInt(4, pr.getBotellaCantidad());
							ps.executeUpdate();
						}
					}

					try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR_VENTA)) {
						for (Venta ve : ventas) {
							ps.setInt(1, ve.getId());
							ps.setInt(2, ve.getIdProducto());
							ps.setInt(3, ve.getCantidad());
							ps.setDate(4, Date.valueOf(ve.getFecha()));
							ps.executeUpdate();
						}
					}

					conexion.commit();


			} catch (SQLException | RuntimeException e) {
				conexion.rollback();
				throw e;
			}

		}
		
	}


}