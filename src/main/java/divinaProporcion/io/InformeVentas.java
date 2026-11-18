package divinaProporcion.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOVenta;
import divinaProporcion.modelo.LineaInforme;

public class InformeVentas {
	

			
	public static boolean generar(String ruta) throws IOException, SQLException{
		
		int anioActual = 0;
		int botellasAnio = 0;
		int ventasAnio = 0;
		
		List<LineaInforme> lineas = DAOVenta.informeVentas();
		
		try (BufferedWriter br = new BufferedWriter( new FileWriter(ruta, StandardCharsets.UTF_8))) {
			

			   
			// título: se escribe una sola vez, antes de recorrer las líneas
			br.write("INFORME DE VENTAS ANUAL");
			br.newLine();

			for (LineaInforme l : lineas) {
				
				if (l.getAnio() != anioActual) {
					// el año ha cambiado: si no es el primero, se cierra el anterior con su total
					if (anioActual != 0) {
				        br.write(String.format("TOTAL %d: %d botellas, %d venta(s)", anioActual, botellasAnio, ventasAnio));
				        br.newLine();
				    }
				    anioActual = l.getAnio();
				    botellasAnio = 0;
				    ventasAnio = 0;
				    // encabezado del año que empieza
				    br.write(String.format("== %d ==", anioActual));
				    br.newLine();
				}
				// una línea por producto: %-28s = nombre alineado a la izquierda en 28 caracteres
				br.write(String.format("%d  %-28s %6d botellas  %2d venta(s)", l.getAnio(), l.getNombreProducto(), l.getCantidadTotal(), l.getNumVentas()));
				br.newLine();
				

				// se acumulan las cifras del año en curso para escribir su total al cerrarlo
				botellasAnio += l.getCantidadTotal();
				ventasAnio += l.getNumVentas();
			}
			
			br.write(String.format("TOTAL %d: %d botellas, %d venta(s)", anioActual, botellasAnio, ventasAnio));
			br.newLine();
		}
		return true;
		
	}
	

}
