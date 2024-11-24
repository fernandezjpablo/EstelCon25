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

/**
 * Servlet implementation class InscribirEsperaActividad
 */
@WebServlet("/InscribirEsperaActividad")
public class InscribirEsperaActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public InscribirEsperaActividad() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idusuario=request.getParameter("idusuario");
		JSONObject resultado=new JSONObject();
		resultado.put("respuesta", "ok");
		String idactividad=request.getParameter("idactividad");
		boolean pago=request.getParameter("pago").equals("true");
		int estado=9;
		//if (pago) estado=3; //Se ignora el parámetro pago, se gestionará por fuera de la plataforma
		InscripcionActividadBean bean=new InscripcionActividadBean(idusuario,idactividad,estado,System.currentTimeMillis());
		boolean res=SQLInscripcionesActividades.insertInscripcion(bean);
		if (!res) {
			resultado.put("respuesta", "error");
			LoggerInscritos.registrarActividadSession(request, "No se pudo inscribir en lista de espera de la actividad: "+idactividad);
		}
		else
		{
			LoggerInscritos.registrarActividadSession(request, "Inscripcion en lista de espera de actividad "+idactividad+".");
		}
		response.getWriter().println(resultado.toString());
	}

}
