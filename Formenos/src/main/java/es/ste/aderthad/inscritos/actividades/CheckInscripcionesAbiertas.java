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
 * Servlet implementation class CheckInscripcionesAbiertas
 */
@WebServlet("/CheckInscripcionesAbiertas")
public class CheckInscripcionesAbiertas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CheckInscripcionesAbiertas() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    private String formatearLista(JSONArray lista)
    {
    	String listado="";
    	for (int i=0;i<lista.length();i++)
    	{
    		listado+=lista.getString(i)+";";
    	}
    	return listado;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject respuesta=new JSONObject();
		String idUsuario=request.getParameter("idusuario");
		String idActividad=request.getParameter("idactividad");
		String activas=EntornoInscritos.getVariable("ACTIVIDADES_INSCRIBIBLES");
		JSONArray inscritas=SQLInscripcionesActividades.selectActividades(idUsuario);
		JSONArray propias=SQLActividades.listarMisActividades(idUsuario);
		
		JSONObject planificacion=SQLPlanificacion.getIntervaloActividad(idActividad);
		if (planificacion.has("fecha"))
		{
			JSONObject coincidencias=SQLPlanificacion.getSimultaneas(planificacion);
		JSONArray conflictosInscritas=new JSONArray();
		JSONArray conflictosPropias=new JSONArray();
		for (int i=0;i<inscritas.length();i++)
		{
			if (coincidencias.has((inscritas.getJSONObject(i).getString("idActividad"))))
			{
				conflictosInscritas.put(inscritas.getJSONObject(i).getString("nombreActividad"));
			}
		}
		
		for (int i=0;i<propias.length();i++)
		{
			if (coincidencias.has((propias.getJSONObject(i).getString("idActividad"))))
			{
				conflictosPropias.put(propias.getJSONObject(i).getString("nombreActividad"));
			}
		}
		respuesta.put("inscritas", formatearLista(conflictosInscritas));
		respuesta.put("propias", formatearLista(conflictosPropias));
		}
		else
		{
			respuesta.put("inscritas", "");
			respuesta.put("propias", "");
		}
		respuesta.put("respuesta", activas.toLowerCase().equals("si"));

		response.getWriter().println(respuesta.toString());
	}


}
