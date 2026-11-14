package divinaProporcion.controlador;

import java.sql.SQLException;
import java.util.List;

import divinaProporcion.dao.DAOParcela;
import divinaProporcion.modelo.Parcela;
import divinaProporcion.modelo.Variedad;

public class ControladorParcela {
	
	
	public static String listar() {
	    String respuesta;
	    try {
	        respuesta = formatear(DAOParcela.listarParcela());
	    } catch (SQLException e) {
	        respuesta = "Error de BD: " + e.getMessage();
	    }
	    return respuesta;
	}
	
	public static String filtrarPorNombre(String nombre) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorNombre(nombre));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	public static String filtrarPorUbicacion(String ubicacion) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorUbicacion(ubicacion));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	public static String filtrarPorExtension(double extension) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorExtension(extension));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

	public static String filtrarPorVariedad(int idVariedad) {
		String respuesta;
		try {
			respuesta = formatear(DAOParcela.filtrarPorVariedad(idVariedad));
		} catch (SQLException e) {
			respuesta = "Error de BD: " + e.getMessage();
		}
		return respuesta;
	}

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
