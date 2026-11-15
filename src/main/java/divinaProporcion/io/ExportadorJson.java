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

public class ExportadorJson {
	
	
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
