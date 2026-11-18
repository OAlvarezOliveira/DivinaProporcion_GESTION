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

/**
 * Copia de seguridad en fichero binario mediante serialización de objetos.
 *
 * El fichero contiene, por este orden, las listas de variedades, parcelas, cosechas,
 * productos y ventas. La lectura debe seguir el mismo orden que la escritura.
 */
public class BackupBinario {

	/**
	 * Guarda en un fichero binario el contenido actual de las cinco tablas.
	 *
	 * @param ruta ruta del fichero de destino
	 * @return {@code true} si se ha escrito correctamente
	 * @throws IOException si no se puede escribir el fichero
	 * @throws SQLException si falla la lectura de la base de datos
	 */
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

	/**
	 * Restaura la base de datos a partir de un fichero de backup.
	 * Lee las cinco listas completas antes de tocar la base de datos, de modo que un fichero
	 * dañado falla sin modificar nada.
	 *
	 * @param ruta ruta del fichero de backup
	 * @return {@code true} si la restauración se ha completado
	 * @throws IOException si el fichero no existe o está dañado
	 * @throws SQLException si falla la base de datos (se deshace la transacción)
	 * @throws ClassNotFoundException si el fichero contiene una clase desconocida
	 */
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
