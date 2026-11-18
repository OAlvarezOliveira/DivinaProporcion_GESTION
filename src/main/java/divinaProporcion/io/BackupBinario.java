package divinaProporcion.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOBackup;
import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.dao.DAOParcela;
import divinaProporcion.dao.DAOProducto;
import divinaProporcion.dao.DAOVariedad;
import divinaProporcion.dao.DAOVenta;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Producto;
import divinaProporcion.modelo.Variedad;
import divinaProporcion.modelo.Venta;

public class BackupBinario {

	public static boolean guardar(String ruta) throws IOException, SQLException {

		List<Variedad> variedades = DAOVariedad.listarVariedad();
		List<Parcela> parcelas = DAOParcela.listarParcela();
		List<Cosecha> cosechas = DAOCosecha.listarCosecha();
		List<Producto> productos = DAOProducto.listarProducto();
		List<Venta> ventas = DAOVenta.listarVenta();

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
			oos.writeObject(variedades);
			oos.writeObject(parcelas);
			oos.writeObject(cosechas);
			oos.writeObject(productos);
			oos.writeObject(ventas);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean restaurar(String ruta) throws IOException, SQLException, ClassNotFoundException {

		List<Variedad> variedades;
		List<Parcela> parcelas;
		List<Cosecha> cosechas;
		List<Producto> productos;
		List<Venta> ventas;

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
			variedades = (List<Variedad>) ois.readObject();
			parcelas = (List<Parcela>) ois.readObject();
			cosechas = (List<Cosecha>) ois.readObject();
			productos = (List<Producto>) ois.readObject();
			ventas = (List<Venta>) ois.readObject();
			DAOBackup.restaurar(variedades, parcelas, cosechas, productos, ventas);
		}

		return true;

	}

}
