package divinaProporcion.modelo;

import java.io.Serializable;

/**
 * Parcela de viñedo (tabla {@code parcela}). Pertenece a una {@link Variedad}.
 */
public class Parcela implements Serializable{

	private int id;
	private String nombre;
	private double extension;
	private Variedad variedad;
	private String ubicacion;

	public Parcela() {
	}

	public Parcela(int id, String nombre, double extension, Variedad variedad, String ubicacion) {

		this.id = id;
		this.nombre = nombre;
		this.extension = extension;
		this.variedad = variedad;
		this.ubicacion = ubicacion;
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

	public double getExtension() {
		return extension;
	}

	public void setExtension(double extension) {
		this.extension = extension;
	}

	public Variedad getVariedad() {
		return variedad;
	}

	public void setVariedad(Variedad variedad) {
		this.variedad = variedad;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	@Override
	public String toString() {
		return String.format("Parcela [id=%d, nombre=%s, extension=%.2f, variedad=%s, ubicacion=%s]", id, nombre,
				extension, variedad, ubicacion);
	}

}
