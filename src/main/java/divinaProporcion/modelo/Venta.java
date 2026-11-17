package divinaProporcion.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Venta implements Serializable{
	
	private int id;
	private Producto producto;
	private int cantidad;
	private LocalDate fecha;
	 
	 public Venta() {

	 }
	 public Venta(int id, Producto producto, int cantidad, LocalDate fecha) {

		this.id = id;
		this.producto = producto;
		this.cantidad = cantidad;
		this.fecha = fecha;
	 }
	 public int getId() {
		 return id;
	 }
	 public void setId(int id) {
		 this.id = id;
	 }
	 public Producto getProducto() {
		 return producto;
	 }
	 public void setProducto(Producto producto) {
		 this.producto = producto;
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
		return String.format("Num.Venta: %d, producto=%s, cantidad=%d, fecha=%s]", id, producto.getNombre(), cantidad, fecha);
	 }


}
