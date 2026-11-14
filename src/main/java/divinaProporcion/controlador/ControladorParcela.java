package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOParcela;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Variedad;

/**
 * Controlador de parcelas: conecta el menú con {@link divinaProporcion.dao.DAOParcela}.
 * <p>
 * No imprime nada: devuelve siempre un texto para que lo muestre la vista. Las excepciones SQLException se capturan aquí y se convierten en un mensaje.
 * </p>
 */
public class ControladorParcela {
	
	
	/**
	 * Lista todas las parcelas.
	 *
	 * @return una parcela por línea, o "sin resultados"; si falla la BD, el error
	 */
	public static String listar() {
	    String respuesta;
	    try {
	        respuesta = formatear(DAOParcela.listarParcela());
	    } catch (SQLException e) {
	        respuesta = "Error de BD: " + e.getMessage();
	    }
	    return respuesta;
	}
	
	/**
	 * Lista las parcelas cuyo nombre contiene el texto indicado.
	 *
	 * @param nombre texto a buscar
	 * @return parcelas encontradas, "sin resultados" o el error
	 */
	public static String filtrarPorNombre(String nombre) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorNombre(nombre));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Lista las parcelas cuya ubicación contiene el texto indicado.
	 *
	 * @param ubicacion texto a buscar
	 * @return parcelas encontradas, "sin resultados" o el error
	 */
	public static String filtrarPorUbicacion(String ubicacion) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorUbicacion(ubicacion));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Lista las parcelas con la extensión indicada.
	 *
	 * @param extension extensión en hectáreas
	 * @return parcelas encontradas, "sin resultados" o el error
	 */
	public static String filtrarPorExtension(double extension) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorExtension(extension));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Lista las parcelas de una variedad.
	 *
	 * @param idVariedad id de la variedad
	 * @return parcelas encontradas, "sin resultados" o el error
	 */
	public static String filtrarPorVariedad(int idVariedad) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorVariedad(idVariedad));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Crea una parcela.
	 *
	 * @param nombre nombre de la parcela
	 * @param extension extensión
	 * @param idVariedad id de una variedad existente
	 * @param ubicacion ubicación (puede ser nula)
	 * @return "Parcela creada." o el mensaje de error
	 */
	public static String crear(String nombre, double extension, int idVariedad, String ubicacion) {
		String respuesta;
		try {
			// El id es 0 porque lo genera la BD; de la variedad solo importa su id
			Parcela p = new Parcela(0, nombre, extension, new Variedad(idVariedad, null, null), ubicacion);
			if (DAOParcela.crearParcela(p)) {
				respuesta = "Parcela creada.";
			} else {
				respuesta = "No se ha podido crear la parcela.";
			}
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Modifica una parcela.
	 *
	 * @param id id de la parcela a modificar
	 * @param nombre nuevo nombre
	 * @param extension nueva extensión
	 * @param idVariedad id de la nueva variedad
	 * @param ubicacion nueva ubicación
	 * @return confirmación, aviso si el id no existe, o el error
	 */
	public static String editar(int id, String nombre, double extension, int idVariedad, String ubicacion) {
		String respuesta;
		try {
			Parcela p = new Parcela(id, nombre, extension, new Variedad(idVariedad, null, null), ubicacion);
			if (DAOParcela.editarParcela(p)) {
				respuesta = "Parcela editada.";
			} else {
				respuesta = "No existe una parcela con ese id.";
			}
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Borra una parcela.
	 *
	 * @param id id de la parcela a borrar
	 * @return confirmación, aviso si el id no existe, o el error (p. ej. si tiene cosechas)
	 */
	public static String borrar(int id) {
		String respuesta;
		try {
			if (DAOParcela.borrarParcela(id)) {
				respuesta = "Parcela borrada.";
			} else {
				respuesta = "No existe una parcela con ese id.";
			}
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	private static String formatear(List<Parcela> lista) {
	    if (lista.isEmpty()) {
	        return "sin resultados";
	    }
	    StringBuilder sb = new StringBuilder();
	    for (Parcela p : lista) {
	        sb.append(p).append("\n");
	    }
	    return sb.toString();
	}

}
