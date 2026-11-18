package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOProducto;
import divinaProporcion.modelo.Producto;

/**
 * Controlador de productos: traduce el resultado del acceso a datos en texto para la vista.
 * Nunca imprime ni lee de consola; eso es responsabilidad de {@code Menu}.
 */
public class ControladorProducto {
	
	
	/**
	 * Lista los productos con su tirada y su stock actual.
	 *
	 * @return una línea por producto, {@code "sin resultados"} si no hay ninguno,
	 *         o un mensaje de error si falla la base de datos
	 */
	public static String listarStock() {
		
		String respuesta;
		
		try {
			List<Producto>	productos =	DAOProducto.listarStockProducto();
			
			if(productos.isEmpty()) {
				
				respuesta = "sin resultados";
			} else {
				
				StringBuilder sb = new StringBuilder();
				
				for (Producto p : productos) {
					sb.append(p).append("\n");
				}
				respuesta = sb.toString();
			}
			
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
			
		}
		
		return respuesta;
		
	}

}
