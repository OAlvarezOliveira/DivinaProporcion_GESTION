package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOProducto;
import divinaProporcion.modelo.Producto;

public class ControladorProducto {
	
	
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
