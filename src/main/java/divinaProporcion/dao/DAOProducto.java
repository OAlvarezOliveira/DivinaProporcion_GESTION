package divinaProporcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import divinaProporcion.modelo.Cosecha;
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

	public static List<Producto> listarProducto() throws SQLException{
		
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
	
	 private static Producto mapearProducto(ResultSet rs) throws SQLException {
		int idProducto = 0;
		String  nombreProducto = null ;
		int botellasProducto = 0 ;
		Cosecha cosecha = null ;
		
			 cosecha = DAOCosecha.mapearCosecha(rs);			 
			 idProducto = rs.getInt("idProducto");
			 nombreProducto = rs.getString("nombreProducto");
			 botellasProducto = rs.getInt("botellasProducto");
			

		return new Producto(idProducto, nombreProducto,cosecha,botellasProducto);
		
	}
	
	
}
