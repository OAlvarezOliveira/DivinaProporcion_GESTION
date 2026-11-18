package divinaProporcion.modelo;

/**
 * Línea del informe de ventas: botellas y número de ventas de un producto en un año.
 */
public class LineaInforme {
	
	private int anio;
	private String nombreProducto;
	private int cantidadTotal;
	private int numVentas;
	
	public LineaInforme(int anio, String nombreProducto, int cantidadTotal, int numVentas) {
		super();
		this.anio = anio;
		this.nombreProducto = nombreProducto;
		this.cantidadTotal = cantidadTotal;
		this.numVentas = numVentas;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public int getNumVentas() {
		return numVentas;
	}

	public void setNumVentas(int numVentas) {
		this.numVentas = numVentas;
	}
	
	

}
