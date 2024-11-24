package es.ste.aderthad.inscripciones;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.CheckinBean;
import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLCheckin;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLUsuarios;

import org.json.JSONObject;

import es.ste.aderthad.sql.SQLInscritos;

/**
 * Servlet implementation class ExportarInscritos
 */
@WebServlet("/ExportarInscritosCheckin")
public class ExportarCheckinWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ExportarCheckinWeb() {
        // TODO Auto-generated constructor stub
    }

    
	private String cruzarActividades(String idInscrito)
	{
		StringBuilder sb=new StringBuilder();
		ActividadBean beanActividad;
		String observaciones;
		String listaActividades=Entorno.getVariable("ACTIVIDADES_CHECKIN");
		if (!listaActividades.equals(""))
		{
			String[] actividades = listaActividades.split(",");
			for (int a=0;a<actividades.length;a++)
			{
				beanActividad=SQLActividades.buscarActividad(actividades[a]);
				if (beanActividad!=null)
				{
					observaciones=SQLInscripcionesActividades.selectObservacionesInscripcion(beanActividad.getIdActividad(),idInscrito);
					if (observaciones !=null && !"".equals(observaciones))
					{
						sb.append(actividades[a]+":"+observaciones+";");
					}
				}
			}
		}
		return sb.toString();
	}
    private String formatearListado(JSONObject inscritos)
    {
    	SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd");
    	String habitacionAnt="";
    	String expedicion="";
		StringBuilder sb=new StringBuilder();
		JSONArray alojados=new JSONArray(inscritos.getString("alojados"));
		HabitacionBean habitacionObj=null;
		CheckinBean beanCheckin=null;
		JSONObject objInscrito;
		SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sb.append("<table border='1px'><thead>");
		sb.append("<th>Fila</th><th>PLANTA</th><th>HABITACION<br>(personas;camas)</th><th>APELLIDOS</th><th>NOMBRE</th><th>NIF</th><th>FECHA EXPEDICIÓN</th><th>FECHA NACIMIENTO</th><th>MENOR DE 14</th><th>DIETA</th><th>CUNA</th><th>ALERGIAS</th><th>Info. adicional</th>");
		sb.append("</thead><tbody>");
		for (int a=0;a<alojados.length();a++)
		{
			sb.append("<tr><td>"+a+"</td>");
			objInscrito=alojados.getJSONObject(a);
			beanCheckin=SQLCheckin.selectCheckin(SQLUsuarios.selectInscrito(objInscrito.getString("id")).getUsuario());
			if (!habitacionAnt.equals(objInscrito.getString("habitacion")))
			{
				habitacionObj=SQLHabitaciones.selectHabitacion(objInscrito.getString("habitacion"));
			
			}
			if (habitacionObj!=null) sb.append("<td>"+habitacionObj.getPlanta()+"</td><td>"+habitacionObj.getIdentificador()+"("+habitacionObj.getPlazas()+"p;"+habitacionObj.getCamas()+"c)</td>");
			habitacionAnt=objInscrito.getString("habitacion");
			sb.append("<td>"+objInscrito.getString("apellidos")+"</td>");
			sb.append("<td>"+objInscrito.getString("nombre")+"</td>");
			sb.append("<td>"+objInscrito.getString("nif")+"</td>");
			if (beanCheckin!=null)
			{
				expedicion=beanCheckin.getFechaExpedicion();
				if ("".equals(expedicion))
				{
					if (!"".equals(objInscrito.getString("nif")))
					{
					expedicion="<span style=\"color:purple\">Sin Fecha de Expedición</span>";
				}
				}
				else
				{
					try
					{
					if (df2.parse(expedicion).after(new Date()))
					{
							expedicion="<span style=\"color:orange\">"+expedicion+"</span>";
				}
					} catch (Exception e)
					{}
				}
				sb.append("<td>"+expedicion+"</td>");
				sb.append("<td>"+beanCheckin.getFechaNacimiento()+"</td>");
			}
			else
			{
				sb.append("<td style=\"color:red\">Sin Fecha de Expedición</td>");
				sb.append("<td style=\"color:red\">Sin Checkin</td>");
			}
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("<td>Menor de 14</td>");
			}
			else
			{
				sb.append("<td></td>");
			}
			if (objInscrito.has("alimentosTxt"))
			{
				sb.append("<td>"+objInscrito.getString("alimentosTxt")+"</td>");
			}
			else
			{
				sb.append("<td></td>");
			}
			
			if (objInscrito.has("con_bebes"))
			{
			if (!objInscrito.getString("con_bebes").isEmpty())
			{
				if (objInscrito.getString("con_bebes").equals("0"))
				{
					
					sb.append("<td></td>");
				}
				else
				{
					
					sb.append("<td>"+"Con cuna ("+objInscrito.getString("con_bebes")+")"+"</td>");
				}
			}
			else
			{
				sb.append("<td></td>");
			}
			}
			

			if (objInscrito.has("alergiasTxt"))
			{
				sb.append("<td>"+objInscrito.getString("alergiasTxt")+"</td>");
			}
			else
			{
				sb.append("<td></td>");
			}
			sb.append("<td>");
			sb.append(cruzarActividades(objInscrito.getString("id")));
			sb.append("</td>");
			sb.append("</tr>");
		}
		
		sb.append("</tbody></table>");
		

		

		return sb.toString();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject inscritos=new JSONObject();
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		inscritos.put("alojados",SQLInscritos.selectInscritosOrdenadosHabitacion().toString() );
		Logger.registrarActividadSession(request, "Exportando listado de inscritos para checkin.");
		response.setCharacterEncoding("ISO-8859-15");
		response.setContentType("text/html");
		response.getWriter().append(formatearListado(inscritos));
	}


}
