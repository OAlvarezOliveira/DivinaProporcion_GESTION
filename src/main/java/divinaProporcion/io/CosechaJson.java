package divinaProporcion.io;

/**
 * Representación de una cosecha para el intercambio en JSON.
 * Identifica la parcela por su nombre en lugar de por su id, y admite graduación nula.
 */
public class CosechaJson {
	
	private String parcela;
	private int anualidad;
	private double cantidad;
	//usamos el objeto para que pueda ser null
	private Double graduacion;
	
	public CosechaJson() {

	}

	public CosechaJson(String parcela, int anualidad, double cantidad, Double graduacion) {

		this.parcela = parcela;
		this.anualidad = anualidad;
		this.cantidad = cantidad;
		this.graduacion = graduacion;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public int getAnualidad() {
		return anualidad;
	}

	public void setAnualidad(int anualidad) {
		this.anualidad = anualidad;
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
		return String.format("CosechaJson [parcela=%s, anualidad=%d, cantidad=%.2f, graduacion=%s]", parcela, anualidad,
				cantidad, graduacion);
	}
	
	
	

}
