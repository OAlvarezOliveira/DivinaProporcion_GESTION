package divinaProporcion.controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import divinaProporcion.dao.DAOVenta;
import divinaProporcion.modelo.Venta;

/**
 * Controlador de ventas: convierte los resultados y errores de {@link DAOVenta} en mensajes de texto.
 * Los errores de negocio (stock insuficiente, producto inexistente) y los de base de datos
 * se distinguen en el mensaje devuelto.
 */
public class ControladorVenta {

	/**
	 * Lista todas las ventas registradas.
	 *
	 * @return una línea por venta, {@code "sin resultados"} si no hay ninguna,
	 *         o un mensaje de error si falla la base de datos
	 */
	public static String listar() {

		String respuesta = null;

		List<Venta> lista;
		try {
			lista = DAOVenta.listarVenta();

			if (lista.isEmpty()) {
				respuesta = "sin resultados";

			} else {

				StringBuilder sb = new StringBuilder();

				for (Venta v : lista) {
					sb.append(v).append("\n");
				}

				respuesta = sb.toString();
			}

		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;

	}

	/**
	 * Registra una venta si hay stock suficiente.
	 *
	 * @param idProducto identificador del producto vendido
	 * @param cantidad botellas vendidas
	 * @param fecha fecha de la venta
	 * @return {@code "Venta registrada."}, el motivo del rechazo (producto inexistente o
	 *         stock insuficiente) o un mensaje de error de base de datos
	 */
	public static String registrar(int idProducto, int cantidad, LocalDate fecha) {
		String respuesta;
		Venta v = new Venta(0, idProducto, null, cantidad, fecha);

		try {

			DAOVenta.registrarVenta(v);
			respuesta = "Venta registrada.";

		} catch (IllegalArgumentException e) {
			respuesta = e.getMessage();

		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();

		}

		return respuesta;
	}

}
