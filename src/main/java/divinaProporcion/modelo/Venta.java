package divinaProporcion.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Venta de botellas de un producto en una fecha.
 *
 * Guarda el id del producto (clave foránea); el nombre solo se rellena al listar.
 */
public class Venta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int idProducto;
	private String nombreProducto;
	private int cantidad;
	private LocalDate fecha;
	 
	 public Venta() {

	 }
	 public Venta(int id, int idProducto, String nombreProducto,int cantidad, LocalDate fecha) {

		this.id = id;
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.cantidad = cantidad;
		this.fecha = fecha;
	 }
	 public int getId() {
		 return id;
	 }
	 public void setId(int id) {
		 this.id = id;
	 }
	 public int  getIdProducto() {
		 return idProducto;
	 }
	 public void setIdProducto(int idProducto) {
		 this.idProducto = idProducto;
	 }
	 
	 public String  getNombreProducto() {
		 return nombreProducto;
	 }
	 public void setNombreProducto(String nombreProducto) {
		 this.nombreProducto = nombreProducto;
	 }
	 
	 
	 public int getCantidad() {
		 return cantidad;
	 }
	 public void setCantidad(int cantidad) {
		 this.cantidad = cantidad;
	 }
	 public LocalDate getFecha() {
		 return fecha;
	 }
	 public void setFecha(LocalDate fecha) {
		 this.fecha = fecha;
	 }
	 @Override
	 public String toString() {
		return String.format("Num.Venta: %d, Prod=%s, Cnt=%d, Fecha=%s", id, nombreProducto, cantidad, fecha);
	 }


}
