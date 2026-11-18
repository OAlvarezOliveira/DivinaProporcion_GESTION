package divinaProporcion.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.modelo.Cosecha;

/**
 * Exporta las cosechas de la base de datos a un fichero JSON.
 */
public class ExportadorJson {
	
	
	/**
	 * Escribe todas las cosechas en un fichero JSON con formato legible (UTF-8).
	 *
	 * @param ruta ruta del fichero de destino
	 * @return {@code true} si se ha escrito correctamente
	 * @throws IOException si no se puede escribir el fichero
	 * @throws SQLException si falla la lectura de la base de datos
	 */
	public static boolean exportar(String ruta) throws IOException, SQLException{
		
		List<Cosecha> lista =  DAOCosecha.listarCosecha();
		
		List<CosechaJson> listaCosechas =  new ArrayList<>();
		
		for (Cosecha c : lista ) {
			
			listaCosechas.add(new CosechaJson( c.getParcela().getNombre(),c.getAnualidad(),c.getCantidad(),c.getGraduacion()));
		}
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listaCosechas);
		try (FileWriter fw = new FileWriter(ruta, StandardCharsets.UTF_8)) {
			
			fw.write(json);
			return true;
		}
		
	}

}
