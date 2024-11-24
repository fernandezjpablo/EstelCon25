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

import es.ste.aderthad.inscritos.data.ActividadBean;
import es.ste.aderthad.inscritos.data.InscripcionActividadBean;
import es.ste.aderthad.inscritos.data.PlanificacionBean;
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
 * Servlet implementation class ObtenerInscritosActividad
 */
@WebServlet("/ObtenerInscritosActividadCsv")
public class ObtenerInscritosActividadCsv extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ObtenerInscritosActividadCsv() {
        // TODO Auto-generated constructor stub
    }
private String formatear(long fecha)
{
	SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	return df.format(fecha);
}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("idactividad");
		String observacionesStr="";
		JSONArray inscritos=SQLActividades.listarInscritosTotales(id);
		JSONObject actividad=SQLActividades.consultarActividad(id);
		response.setContentType("text/plain;charset=ISO-8859-15");
		response.setHeader("Content-Disposition", "attachment; filename=\"inscripciones-actividad-"+actividad.getString("nombreActividad")+".csv\"");
		response.getWriter().println("NOMBRE;APELLIDOS;PSEUDONIMO;MENOR;EMAIL;TELÉFONO;ESTADO;FECHA INSCRIPCIÓN;OBSERVACIONES");
		for (int i=0;i<inscritos.length();i++)
		{
			response.getWriter().print(inscritos.getJSONObject(i).getString("nombre")+";"+inscritos.getJSONObject(i).getString("apellidos")+";");
			response.getWriter().print(inscritos.getJSONObject(i).getString("pseudonimo")+";");
			if (inscritos.getJSONObject(i).getBoolean("menor"))
			{
				response.getWriter().print("Menor de edad;");
			}
			else
			{
				response.getWriter().print("Adulto o mayor de 14;");
			}
			observacionesStr="";
			if (inscritos.getJSONObject(i).has("observaciones"))
			{
				observacionesStr=inscritos.getJSONObject(i).getString("observaciones");
			}
			response.getWriter().print(inscritos.getJSONObject(i).getString("email")+";");
			response.getWriter().print(inscritos.getJSONObject(i).getString("telefono")+";");
			response.getWriter().println(inscritos.getJSONObject(i).getInt("estado")+";"+formatear(inscritos.getJSONObject(i).getLong("fecha"))+";"+observacionesStr);
		}
		
	}

}
