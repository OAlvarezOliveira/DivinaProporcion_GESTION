package divinaProporcion.controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import divinaProporcion.dao.DAOVenta;
import divinaProporcion.modelo.Venta;

public class ControladorVenta {

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
