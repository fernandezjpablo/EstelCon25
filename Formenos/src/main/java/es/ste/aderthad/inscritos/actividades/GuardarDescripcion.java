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
import es.ste.aderthad.inscritos.sql.SQLActividades;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
/**
 * Servlet implementation class GuardarDescripcion
 */
@WebServlet("/GuardarDescripcion")
public class GuardarDescripcion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GuardarDescripcion() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("actividad");
		String duracion=request.getParameter("duracion");
		String descripcion=URLDecoder.decode(request.getParameter("descripcion"),"UTF-8");
		String observaciones=URLDecoder.decode(request.getParameter("observaciones"),"UTF-8");
		String requisitos=URLDecoder.decode(request.getParameter("requisitos"),"UTF-8");
	
		String resultado="ok";
		try
		{	
			SQLActividades.updateDuracion(id, duracion);
			SQLActividades.updateDescripcion(id,descripcion);
			SQLActividades.updateObservaciones(id,observaciones);
			SQLActividades.updateRequisitos(id,requisitos);
			LoggerInscritos.registrarActividadSession(request, "Información de actividad "+id+" actualizada.");
		}
		catch (Exception e)
		{
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
