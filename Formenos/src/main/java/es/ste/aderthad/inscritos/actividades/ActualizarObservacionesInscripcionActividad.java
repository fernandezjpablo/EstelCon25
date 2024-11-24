package es.ste.aderthad.inscritos.actividades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.InscripcionActividadBean;
import es.ste.aderthad.inscritos.data.PlanificacionBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class ActualizarObservacionesInscripcionActividad
 */
@WebServlet("/ActualizarObservacionesInscripcionActividad")
public class ActualizarObservacionesInscripcionActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ActualizarObservacionesInscripcionActividad() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idusuario=request.getParameter("idusuario");
		boolean bloqueo_datos=EntornoInscritos.getVariable("BLOQUEAR_DATOS_INSCRITOS").toUpperCase().equals("SI");
		JSONObject resultado=new JSONObject();
		resultado.put("respuesta", "ok");
		String idactividad=request.getParameter("idactividad");
		String observaciones=request.getParameter("observaciones");
		int estado=0;
		InscripcionActividadBean bean=new InscripcionActividadBean(idusuario,idactividad,estado,System.currentTimeMillis(),observaciones);
		boolean res=false;
		if (!bloqueo_datos)
		{
			res=SQLInscripcionesActividades.updateObservacionesInscripcion(bean);
		}
		if (!res) {
			if (bloqueo_datos)
			{
				resultado.put("respuesta", "error al actualizar observaciones, ya no se permiten modificaciones.");
				LoggerInscritos.GenerarEntradaLogMensaje("Error al actualizar observaciones de la inscrición a la actividad "+idactividad+". Bloqueadas por organización.", LoggerInscritos.getFileNameErrorLog());
			}
			else
			{
			resultado.put("respuesta", "error al actualizar observaciones");
			LoggerInscritos.GenerarEntradaLogMensaje("Error al actualizar observaciones de la inscrición a la actividad "+idactividad, LoggerInscritos.getFileNameErrorLog());
			}
			}
		else
		{
			LoggerInscritos.registrarActividadSession(request, "Observaciones de actividad "+idactividad+" actualizadas.");
		}
		response.getWriter().println(resultado.toString());
	}
}
