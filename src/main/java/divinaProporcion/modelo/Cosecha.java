package divinaProporcion.modelo;

import java.io.Serializable;

/**
 * Cosecha de una parcela en un año (tabla {@code cosecha}).
 * <p>
 * La graduación es {@link Double} (y no {@code double}) porque puede ser nula si no se midió.
 * </p>
 */

public class Cosecha implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private int anualidad;
	private Parcela parcela;
	private double cantidad;
	private Double graduacion;

	public Cosecha() {
	}

	public Cosecha(int id, int anualidad, Parcela parcela, double cantidad, Double graduacion) {
		this.id = id;
		this.anualidad = anualidad;
		this.parcela = parcela;
		this.cantidad = cantidad;
		this.graduacion = graduacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAnualidad() {
		return anualidad;
	}

	public void setAnualidad(int anualidad) {
		this.anualidad = anualidad;
	}

	public Parcela getParcela() {
		return parcela;
	}

	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getGraduacion() {
		return graduacion;
	}

	public void setGraduacion(Double graduacion) {
		this.graduacion = graduacion;
	}

	@Override
	public String toString() {
		return String.format("Cosecha [id=%d, anualidad=%d, parcela=%s, cantidad=%.2f, graduacion=%s]", id, anualidad,
				parcela, cantidad, graduacion);
	}

}
