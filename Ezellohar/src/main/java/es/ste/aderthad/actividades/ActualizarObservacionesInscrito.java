package es.ste.aderthad.actividades;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class ActualizarObservacionesInscrito
 */
@WebServlet("/admin/actividades/ActualizarObservacionesInscrito")
public class ActualizarObservacionesInscrito extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ActualizarObservacionesInscrito() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idusuario=request.getParameter("idInscrito");
		JSONObject resultado=new JSONObject();
		resultado.put("respuesta", "ok");
		String idactividad=request.getParameter("idActividad");
		String observaciones=request.getParameter("observaciones");

		boolean res=false;

			res=SQLInscripcionesActividades.updateObservacionesInscripcionActividad(idusuario,idactividad,observaciones);

		if (!res) {

			resultado.put("respuesta", "error al actualizar observaciones");
			Logger.GenerarEntradaLogMensaje("Error al actualizar observaciones de la inscrición a la actividad "+idactividad, Logger.getFileNameErrorLog());

			}
		else
		{
			Logger.registrarActividadSession(request, "Observaciones de actividad "+idactividad+" actualizadas.");
		}
		response.getWriter().println(resultado.toString());
	}

}
