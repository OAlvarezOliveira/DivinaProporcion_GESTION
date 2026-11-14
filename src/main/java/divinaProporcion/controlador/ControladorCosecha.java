package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;

/**
 * Controlador de cosechas: conecta el menú con {@link divinaProporcion.dao.DAOCosecha}.
 * <p>
 * No imprime nada: devuelve siempre un texto para que lo muestre la vista. Las excepciones SQLException se capturan aquí y se convierten en un mensaje.
 * </p>
 */
public class ControladorCosecha {

	/**
	 * Lista todas las cosechas, ordenadas por parcela y año.
	 *
	 * @return una cosecha por línea, o "sin resultados"; si falla la BD, el error
	 */
	public static String listar() {
		String respuesta;
		try {
			respuesta = formatear(DAOCosecha.listarCosecha());
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Crea una cosecha.
	 *
	 * @param anualidad año de la vendimia
	 * @param idParcela id de una parcela existente
	 * @param cantidad kilos recogidos
	 * @param graduacion grados, o {@code null} si no se midió
	 * @return "Cosecha creada." o el mensaje de error (p. ej. cosecha repetida en esa parcela y año)
	 */
	public static String crear(int anualidad, int idParcela, double cantidad, Double graduacion) {
		String respuesta;
		try {
			// El id es 0 porque lo genera la BD; de la parcela solo importa su id
			Cosecha c = new Cosecha(0, anualidad, new Parcela(idParcela, null, 0, null, null), cantidad, graduacion);
			if (DAOCosecha.crearCosecha(c)) {
				respuesta = "Cosecha creada.";
			} else {
				respuesta = "No se ha podido crear la cosecha.";
			}
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Modifica una cosecha.
	 *
	 * @param id id de la cosecha a modificar
	 * @param anualidad nuevo año
	 * @param idParcela id de la parcela
	 * @param cantidad nuevos kilos
	 * @param graduacion grados, o {@code null} si no se midió
	 * @return confirmación, aviso si el id no existe, o el error
	 */
	public static String editar(int id, int anualidad, int idParcela, double cantidad, Double graduacion) {
		String respuesta;
		try {
			Cosecha c = new Cosecha(id, anualidad, new Parcela(idParcela, null, 0, null, null), cantidad, graduacion);
			if (DAOCosecha.editarCosecha(c)) {
				respuesta = "Cosecha editada.";
			} else {
				respuesta = "No existe una cosecha con ese id.";
			}
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	/**
	 * Borra una cosecha.
	 *
	 * @param id id de la cosecha a borrar
	 * @return confirmación, aviso si el id no existe, o el error
	 */
	public static String borrar(int id) {
		String respuesta;
		try {
			if (DAOCosecha.borrarCosecha(id)) {
				respuesta = "Cosecha borrada.";
			} else {
				respuesta = "No existe una cosecha con ese id.";
			}
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	private static String formatear(List<Cosecha> lista) {
		if (lista.isEmpty()) {
			return "sin resultados";
		}
		StringBuilder sb = new StringBuilder();
		for (Cosecha c : lista) {
			sb.append(c).append("\n");
		}
		return sb.toString();
	}

}
