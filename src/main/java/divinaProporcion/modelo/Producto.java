package divinaProporcion.modelo;

import java.io.Serializable;

/**
 * Producto embotellado a partir de una cosecha.
 *
 * Guarda el id de la cosecha (clave foránea) en lugar del objeto. El stock no se almacena en la base
 * de datos: se calcula como la tirada menos las botellas vendidas.
 */
public class Producto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private int idCosecha;
	private int botellaCantidad;
	private int stock;
	
	
	
	public Producto() {

	}
	
	public Producto(int id, String nombre, int idCosecha, int botellaCantidad) {
		this.id = id;
		this.nombre = nombre;
		this.idCosecha = idCosecha;
		this.botellaCantidad = botellaCantidad;
		this.stock = botellaCantidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getIdCosecha() {
		return idCosecha;
	}

	public void setIdCosecha(int idCosecha) {
		this.idCosecha = idCosecha;
	}

	public int getBotellaCantidad() {
		return botellaCantidad;
	}

	public void setBotellaCantidad(int botellaCantidad) {
		this.botellaCantidad = botellaCantidad;
	}

	@Override
	public String toString() {
		return String.format("%d, %s, Tirada/stock: %d/%d" , id, nombre, botellaCantidad,stock);
	}

	 
	 
}
