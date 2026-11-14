package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOVariedad;
import divinaProporcion.modelo.Color;
import divinaProporcion.modelo.Variedad;

/**
 * Controlador de variedades: conecta el menú con {@link divinaProporcion.dao.DAOVariedad}.
 * <p>
 * No imprime nada: devuelve siempre un texto para que lo muestre la vista. Las excepciones SQLException se capturan aquí y se convierten en un mensaje.
 * </p>
 */
public class ControladorVariedad {

	/**
	 * Lista todas las variedades.
	 *
	 * @return una variedad por línea, o "sin resultados"; si falla la BD, el error
	 */
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

	/**
	 * Crea una variedad.
	 *
	 * @param nombre nombre de la variedad
	 * @param color color de la uva
	 * @return "Variedad creada." o el mensaje de error
	 */
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
	
	/**
	 * Modifica el nombre y el color de una variedad.
	 *
	 * @param id id de la variedad a modificar
	 * @param nombre nuevo nombre
	 * @param color nuevo color
	 * @return confirmación, aviso si el id no existe, o el error
	 */
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
	
	/**
	 * Borra una variedad.
	 *
	 * @param id id de la variedad a borrar
	 * @return confirmación, aviso si el id no existe, o el error (p. ej. si tiene parcelas asociadas)
	 */
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
