package divinaProporcion.modelo;

import java.io.Serializable;

/**
 * Variedad de uva (tabla {@code variedad}).
 */
public class Variedad implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private Color color;

	public Variedad() {

	}

	public Variedad(int id, String nombre, Color color) {
		this.id = id;
		this.nombre = nombre;
		this.color = color;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return String.format("Variedad [id=%d, nombre=%s, color=%s]", id, nombre, color);
	}

}
