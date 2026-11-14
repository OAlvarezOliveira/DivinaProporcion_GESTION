package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOCosecha;
import divinaProporcion.modelo.Cosecha;
import divinaProporcion.modelo.Parcela;

public class ControladorCosecha {

	public static String listar() {
		String respuesta;
		try {
			respuesta = formatear(DAOCosecha.listarCosecha());
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

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
