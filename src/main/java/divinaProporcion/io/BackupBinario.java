package divinaProporcion.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.dao.DAOParcela;
import divinaProporcion.dao.DAOVariedad;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Variedad;

public class BackupBinario {
	


	 
	 public static boolean guardar(String ruta) throws IOException, SQLException {
		 
		 
		 List<Variedad> variedades = DAOVariedad.listarVariedad();
		 List<Parcela> parcelas = DAOParcela.listarParcela();
		 List<Cosecha> cosechas = DAOCosecha.listarCosecha();
		 
		 try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
			    oos.writeObject(variedades);
			    oos.writeObject(parcelas);
			    oos.writeObject(cosechas);
			}
		 
		    return true;
		}
}
