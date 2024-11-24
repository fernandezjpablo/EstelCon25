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

import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.data.PlanificacionBean;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLActividades;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class ObtenerActividadesUsuario
 */
@WebServlet("/ObtenerActividadesUsuario")
public class ObtenerActividadesUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ObtenerActividadesUsuario() {
        // TODO Auto-generated constructor stub
    }


    private JSONArray cruzarActividades(JSONArray inscribibles,JSONArray inscritas,JSONArray propias)
    {
    	JSONObject claves=new JSONObject();
    	JSONArray resultado=new JSONArray();
 /*   	
    	
    	for (int i=0;i<propias.length();i++)
    	{
    		claves.put(propias.getJSONObject(i).getString("idActividad"), false);
    	}
   */ 	
    	for (int i=0;i<inscritas.length();i++)
    	{
    		claves.put(inscritas.getJSONObject(i).getString("idActividad"), false);
    	}
    	
    	String idactividad;
    	for (int j=0;j<inscribibles.length();j++)
    	{
    		idactividad=inscribibles.getJSONObject(j).getString("idActividad");
    		if (!claves.has(idactividad))
    		{
    			resultado.put(inscribibles.getJSONObject(j));
    		}
    	}
    	
    	return resultado;
    	
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idUsuario=request.getParameter("idusuario");
		
		InscritoBean inscrito=SQLUsuarios.selectInscritoFull(idUsuario);
		
		JSONArray inscritas=SQLInscripcionesActividades.selectActividades(idUsuario);
		JSONArray inscribibles=SQLActividades.listarActividadesInscribibles(inscrito.isMenor());
		
		
		
		JSONArray propias=SQLActividades.listarMisActividades(idUsuario);
		
		inscribibles=cruzarActividades(inscribibles,inscritas,propias);
		
		JSONArray flotantes=SQLActividades.listarActividadesFlotantesInscribibles(inscrito.isMenor());
		flotantes=cruzarActividades(flotantes,inscritas,propias);
		
		
		JSONObject respuesta=new JSONObject();
		if ("SI".equals(EntornoInscritos.getVariable("ACTIVIDADES_INSCRIBIBLES")))
		{
			respuesta.put("inscribibles", inscribibles);
		}
		else
		{
			respuesta.put("inscribibles", new JSONArray());
		}
		respuesta.put("propias",propias);
		respuesta.put("inscritas", inscritas);
		respuesta.put("flotantes", flotantes);
		respuesta.put("propuestas", false);
		String propuestas=EntornoInscritos.getVariable("PROPUESTAS");
		if ("activadas".equals(propuestas.toLowerCase()))
		{
			respuesta.put("propuestas", true);
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(respuesta.toString());
	}

}
