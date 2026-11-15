package divinaProporcion.modelo;

import java.io.Serializable;

public class Producto implements Serializable{
	
	private int id;
	private String nombre;
	private Cosecha cosecha; 
	private int botellaCantidad;
	
	
	
	
	public Producto() {

	}
	
	public Producto(int id, String nombre, Cosecha cosecha, int botellaCantidad) {
		this.id = id;
		this.nombre = nombre;
		this.cosecha = cosecha;
		this.botellaCantidad = botellaCantidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Cosecha getCosecha() {
		return cosecha;
	}

	public void setCosecha(Cosecha cosecha) {
		this.cosecha = cosecha;
	}

	public int getBotellaCantidad() {
		return botellaCantidad;
	}

	public void setBotellaCantidad(int botellaCantidad) {
		this.botellaCantidad = botellaCantidad;
	}

	@Override
	public String toString() {
		return String.format("Producto [id=%d, nombre=%s, cosecha=%s, botellaCantidad=%d]", id, nombre, cosecha,
				botellaCantidad);
	}

	 
	 
}
