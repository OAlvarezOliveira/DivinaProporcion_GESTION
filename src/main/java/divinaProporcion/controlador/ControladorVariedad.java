package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOVariedad;
import divinaProporcion.modelo.Color;
import divinaProporcion.modelo.Variedad;

public class ControladorVariedad {

	public static String listar() {

		String respuesta = null;

		List<Variedad> lista;
		try {
			lista = DAOVariedad.listarVariedad();

			if (lista.isEmpty()) {
				respuesta = "sin resultados";

			} else {

				StringBuilder sb = new StringBuilder();

				for (Variedad v : lista) {
					sb.append(v).append("\n");
				}

				respuesta = sb.toString();
			}

		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;

	}

	public static String crear(String nombre, Color color) {
		String respuesta = null;

		try {

			if (DAOVariedad.crearVariedad(new Variedad(0, nombre, color))) {
				respuesta = "Variedad creada.";

			} else {

				respuesta = "No se ha podido crear la variedad";
			}

		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();

		}

		return respuesta;
	}
	
	public static String editar(int id, String nombre, Color color) {
		String respuesta = null;

		try {

			if (DAOVariedad.editarVariedad(new Variedad(id, nombre, color))) {
				respuesta = "Variedad editada.";

			} else {

				respuesta = "No existe una variedad con ese id.";
			}

		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();

		}

		return respuesta;
	}
	
	public static String borrar(int id){
		String respuesta = null;

		try {

			if (DAOVariedad.borrarVariedad(id)) {
				respuesta = "Variedad borrada.";

			} else {

				respuesta = "No existe una variedad con ese id.";
			}

		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();

		}

		return respuesta;
	}
	
}
