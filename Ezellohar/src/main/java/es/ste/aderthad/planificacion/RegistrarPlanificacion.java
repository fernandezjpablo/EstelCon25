package es.ste.aderthad.planificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.PlanificacionBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
/**
 * Servlet implementation class RegistrarPlanificacion
 */
@WebServlet("/admin/planificacion/RegistrarPlanificacion")
public class RegistrarPlanificacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public RegistrarPlanificacion() {
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		ArrayList<PlanificacionBean> tareas=new ArrayList<PlanificacionBean>();
		JSONArray planificadasArr=new JSONArray();
		JSONArray desplanificadasArr=new JSONArray();
		StringBuilder objetoStr=new StringBuilder();
		String linea;
		String idActividad;
		JSONObject entradaObj=null;

		PlanificacionBean beanPlani;
		while ((linea=br.readLine())!=null)
		{
			objetoStr.append(linea);
		}
		JSONObject objeto=new JSONObject(objetoStr.toString());
		JSONObject entrada;
		JSONObject planificadas, desplanificadas;
		String entradaStr;
		planificadas=objeto.getJSONObject("planificadas");
		desplanificadas=objeto.getJSONObject("desplanificadas");
		
		
		
		
		Iterator<String> cursor;
		String id;
		
		
		/*Marcamos las desplanificadas*/
		
		cursor = desplanificadas.keys();
		
		
		while (cursor.hasNext())
		{
			id=cursor.next();
			entradaStr=desplanificadas.getString(id);
			entradaObj=new JSONObject(entradaStr);
			desplanificadasArr.put(entradaObj.getString("id").replace("opt-",""));
	    	int intervalo;
	    	int hora;
	    	String fecha;
	    	String idObjeto;
	    	String tiempo;
	    	String espacio;
	    	String idespacio;
	    	id=id.replace("fecha-","");
	    	fecha=id.split("-espacio-")[0];
	    	id=id.split("-espacio-")[1];
	    	espacio= id.split("-tiempo-")[0];
	    	id=id.split("-tiempo-")[1];
	    	hora=Integer.valueOf(id.split("-intervalo-")[0]);
	    	id=id.split("-intervalo-")[1];
	    	intervalo=Integer.valueOf(id);
	    	int h=hora;
    		while (h<=hora+intervalo)
    		{
    			beanPlani=new PlanificacionBean();
    			beanPlani.setActividad("");
    			beanPlani.setEspacio(espacio);
    			beanPlani.setIntervalo(h);
    			beanPlani.setColor("");
    			beanPlani.setNombreActividad("");
    			beanPlani.setFecha(fecha);
    			tareas.add(beanPlani);
    			h+=5;
    		}
			


		}
		
		 cursor= planificadas.keys();
		
		while (cursor.hasNext())
		{
			id=cursor.next();
			
			entradaStr=planificadas.getString(id);
			entrada=new JSONObject(entradaStr);
			idActividad=entrada.getString("id").replace("opt-", "");
			planificadasArr.put(idActividad);
	    	int intervalo;
	    	int hora;
	    	String fecha;
	    	String idObjeto;
	    	String tiempo;
	    	String espacio;
	    	String idespacio;
	    	id=id.replace("fecha-","");
	    	fecha=id.split("-espacio-")[0];
	    	id=id.split("-espacio-")[1];
	    	espacio= id.split("-tiempo-")[0];
	    	id=id.split("-tiempo-")[1];
	    	hora=Integer.valueOf(id.split("-intervalo-")[0]);
	    	id=id.split("-intervalo-")[1];
	    	intervalo=Integer.valueOf(id);
	    	int h=hora;
    		while (h<=hora+intervalo-1)
    		{
    			beanPlani=new PlanificacionBean();
    			beanPlani.setActividad(idActividad);
    			beanPlani.setEspacio(espacio);
    			beanPlani.setIntervalo(h);
    			beanPlani.setColor(entrada.getString("color"));
    			beanPlani.setFecha(fecha);
    			beanPlani.setNombreActividad(entrada.getString("actividad"));
    			tareas.add(beanPlani);
    			h+=5;
    		}
			

		}


		
		for (int t=0;t<tareas.size();t++)
		{
			if (SQLPlanificacion.existe(tareas.get(t).getIdPlanificacion()))
			{
				SQLPlanificacion.updateActividad(tareas.get(t));
			}
			else
			{
				SQLPlanificacion.insertActividad(tareas.get(t));
			}
		}
		SQLPlanificacion.limpiarPlanificacion();
//		SQLActividades.desplanificarActividades(desplanificadasArr.toString().replace("[", "").replace("]", "").replaceAll("\"",""));
//		SQLActividades.planificarActividades(planificadasArr.toString().replace("[", "").replace("]", "").replaceAll("\"",""));
		SQLActividades.revisionPlanificaciones();
		Logger.registrarActividadSession(request, "Registrando planificación.");
	}

}
