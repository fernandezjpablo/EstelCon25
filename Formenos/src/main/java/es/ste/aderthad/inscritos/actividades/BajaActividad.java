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
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class InscribirActividad
 */
@WebServlet("/BajaActividad")
public class BajaActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public BajaActividad() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idusuario=request.getParameter("idusuario");
		JSONObject resultado=new JSONObject();
		resultado.put("respuesta", "ok");
		String idactividad=request.getParameter("idactividad");
		int estado=0;
		InscripcionActividadBean bean=new InscripcionActividadBean(idusuario,idactividad,estado,System.currentTimeMillis());
		boolean res=SQLInscripcionesActividades.eliminarInscripcion(bean);
		if (res) {
			res=RevisarAforoActividad.revisarAforo(idactividad);
			if (!res) LoggerInscritos.GenerarEntradaLogMensaje("Error al revisar el aforo de "+idactividad, LoggerInscritos.getFileNameErrorLog());
		}
		else
			{
			resultado.put("respuesta", "error al dar la baja de la actividad");
			LoggerInscritos.GenerarEntradaLogMensaje("Error al dar la baja de la actividad "+idactividad, LoggerInscritos.getFileNameErrorLog());
			}
		LoggerInscritos.registrarActividadSession(request, "Baja de actividad "+idactividad+".");
		response.getWriter().println(resultado.toString());
	}

}
