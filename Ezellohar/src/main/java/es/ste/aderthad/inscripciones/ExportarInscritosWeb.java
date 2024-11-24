package es.ste.aderthad.inscripciones;

import java.io.IOException;
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
import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;

import org.json.JSONObject;

import es.ste.aderthad.sql.SQLInscritos;

/**
 * Servlet implementation class ExportarInscritos
 */
@WebServlet("/ExportarInscritosWeb")
public class ExportarInscritosWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ExportarInscritosWeb() {
        // TODO Auto-generated constructor stub
    }

    private String formatearListado(JSONObject inscritos)
    {
    	String habitacionAnt="";
    	HabitacionBean habitacionObj;
		StringBuilder sb=new StringBuilder();
		JSONArray alojados=new JSONArray(inscritos.getString("alojados"));
		JSONArray desalojados=new JSONArray(inscritos.getString("desalojados"));
		JSONArray listaespera=new JSONArray(inscritos.getString("espera"));
		JSONObject objInscrito;
		SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sb.append("Lista principal: Alojados");
		sb.append("<table border='1px'><thead>");
		sb.append("<th>HABITACION</th><th>APELLIDOS</th><th>NOMBRE</th><th>PSEUDÓNIMO</th><th>NIF</th><th>MENOR</th><th>CON BEBÉS</th><th>TELÉFONO</th><th>TELEGRAM</th><th>EMAIL</th><th>ALERGIAS</th><th>DIETA</th><th>TIPO HABITACIÓN</th><th>IMPORTE PLAZA</th><th>ESTADO PAGOS</th><th>FECHA INSCRIPCIÓN</th><th>OBSERVACIONES</th>");
		sb.append("</thead><tbody>");
		for (int a=0;a<alojados.length();a++)
		{
			sb.append("<tr>");
			objInscrito=alojados.getJSONObject(a);
			if (!habitacionAnt.equals(objInscrito.getString("habitacion")))
			{
				habitacionObj=SQLHabitaciones.selectHabitacion(objInscrito.getString("habitacion"));
				if (habitacionObj!=null)
				{
				sb.append("<td><b>"+habitacionObj.getIdentificador()+"</b> ("+objInscrito.getString("habitacion")+")</td>");
				}
				else
				{
				sb.append("<td><b>ERROR</b> ("+objInscrito.getString("habitacion")+")</td>");
				}
				}
			else
			{
				sb.append("<td></td>");
				}
			habitacionAnt=objInscrito.getString("habitacion");
			sb.append("<td>"+objInscrito.getString("apellidos")+"</td>");
			sb.append("<td>"+objInscrito.getString("nombre")+"</td>");
			sb.append("<td>"+objInscrito.getString("pseudonimo")+"</td>");
			sb.append("<td>"+objInscrito.getString("nif")+"</td>");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("<td>"+"Menor de 14 años;");
			}
			else
			{
				sb.append("<td>"+"Adulto"+"</td>");
			}
			if (objInscrito.has("con_bebes"))
			{
			if (!objInscrito.getString("con_bebes").isEmpty())
			{
				if (objInscrito.getString("con_bebes").equals("0"))
				{
					
					sb.append("<td>"+"Sin menores de 3 años"+"</td>");
				}
				else
				{
					
					sb.append("<td>"+"Acompañado de menores de 3 años ("+objInscrito.getString("con_bebes")+")"+"</td>");
				}
			}
			else
			{
				sb.append("<td>"+"Sin menores de 3 años"+"</td>");
			}
			}
			else
			{
				sb.append("<td>"+"Sin menores de 3 años"+"</td>");
			}		
			sb.append("<td>"+objInscrito.getString("telefono")+"</td>");
			if (!objInscrito.has("telegram")) objInscrito.put("telegram", "(no indicado)");
			sb.append("<td>"+objInscrito.getString("telegram")+"</td>");
			sb.append("<td>"+objInscrito.getString("email")+"</td>");
			if (objInscrito.getBoolean("alergias"))
			{
				sb.append("<td>"+"Alergias a:"+objInscrito.getString("alergiasTxt")+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin alergias notificadas"+"</td>");
			}
			if (objInscrito.getBoolean("alimentos"))
			{
				sb.append("<td>"+"Dieta: "+objInscrito.getString("alimentosTxt")+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin dieta específica notificada"+"</td>");
			}
			sb.append("<td>"+objInscrito.getString("tipoHabitacion")+"</td>");
			sb.append("<td>"+objInscrito.getString("importePlaza")+"</td>");
			sb.append("<td>"+objInscrito.getString("estadoPagos")+"</td>");

			sb.append("<td>"+df.format(new Date(objInscrito.getLong("fecha")))+"</td>");
			sb.append("<td>"+objInscrito.getString("observaciones")+"</td>");
			sb.append("</tr>");
		}
		
		sb.append("</tbody></table>");
		
		
		sb.append("Lista secundaria: Desalojados");
		sb.append("<table border='1px'><thead>");
		sb.append("<th>APELLIDOS</th><th>NOMBRE</th><th>PSEUDÓNIMO</th><th>NIF</th><th>MENOR</th><th>CON BEBÉS</th><th>TELÉFONO</th><th>TELEGRAM</th><th>EMAIL</th><th>ALERGIAS</th><th>DIETA</th><th>TIPO HABITACIÓN</th><th>IMPORTE PLAZA</th><th>HABITACIÓN</th><th>FECHA INSCRIPCIÓN</th><th>OBSERVACIONES</th>");
		sb.append("</thead><tbody>");

		for (int a=0;a<desalojados.length();a++)
		{
			sb.append("<tr>");
			objInscrito=desalojados.getJSONObject(a);
			sb.append("<td>"+objInscrito.getString("apellidos")+"</td>");
			sb.append("<td>"+objInscrito.getString("nombre")+"</td>");
			sb.append("<td>"+objInscrito.getString("pseudonimo")+"</td>");
			sb.append("<td>"+objInscrito.getString("nif")+"</td>");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("<td>"+"Menor de 14 años;");
			}
			else
			{
				sb.append("<td>"+"Adulto"+"</td>");
			}
			if (objInscrito.getInt("con_bebes")>0)
			{
				sb.append("<td>"+"Acompañado de menores de 3 años"+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin menores de 3 años"+"</td>");
			}
			sb.append("<td>"+objInscrito.getString("telefono")+"</td>");
			if (!objInscrito.has("telegram")) objInscrito.put("telegram", "(no indicado)");
			sb.append("<td>"+objInscrito.getString("telegram")+"</td>");
			sb.append("<td>"+objInscrito.getString("email")+"</td>");
			if (objInscrito.getBoolean("alergias"))
			{
				sb.append("<td>"+"Alergias a:"+objInscrito.getString("alergiasTxt")+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin alergias notificadas"+"</td>");
			}
			if (objInscrito.getBoolean("alimentos"))
			{
				sb.append("<td>"+"Dieta: "+objInscrito.getString("alimentosTxt")+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin dieta específica notificada"+"</td>");
			}
			sb.append("<td>"+objInscrito.getString("tipoHabitacion")+"</td>");
			sb.append("<td>"+objInscrito.getString("importePlaza")+"</td>");
			//sb.append("<td>"+objInscrito.getString("estadoPagos")+"</td>");
			sb.append("<td>"+objInscrito.getString("habitacion")+"</td>");
			sb.append("<td>"+df.format(new Date(objInscrito.getLong("fecha")))+"</td>");
			sb.append("<td>"+objInscrito.getString("observaciones")+"</td>");
			sb.append("</tr>");
		}
		
		sb.append("</tbody></table>");
		
		
		sb.append("Lista secundaria: En lista de espera");
		sb.append("<table border='1px'><thead>");
		sb.append("<th>APELLIDOS</th><th>NOMBRE</th><th>PSEUDÓNIMO</th><th>NIF</th><th>MENOR</th><th>CON BEBÉS</th><th>TELÉFONO</th><th>TELEGRAM</th><th>EMAIL</th><th>ALERGIAS</th><th>DIETA</th><th>TIPO HABITACIÓN</th><th>IMPORTE PLAZA</th><th>HABITACION</th><th>FECHA INSCRIPCI&oacute;N</th><th>OBSERVACIONES</th>");
		sb.append("</thead><tbody>");		
		for (int a=0;a<listaespera.length();a++)
		{
			sb.append("<tr>");
			objInscrito=listaespera.getJSONObject(a);
			sb.append("<td>"+objInscrito.getString("apellidos")+"</td>");
			sb.append("<td>"+objInscrito.getString("nombre")+"</td>");
			sb.append("<td>"+objInscrito.getString("pseudonimo")+"</td>");
			sb.append("<td>"+objInscrito.getString("nif")+"</td>");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("<td>"+"Menor de 14 años;");
			}
			else
			{
				sb.append("<td>"+"Adulto"+"</td>");
			}
			if (objInscrito.getBoolean("con_bebes"))
			{
				sb.append("<td>"+"Acompañado de menores de 3 años"+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin menores de 3 años"+"</td>");
			}
			sb.append("<td>"+objInscrito.getString("telefono")+"</td>");
			if (!objInscrito.has("telegram")) objInscrito.put("telegram", "(no indicado)");
			sb.append("<td>"+objInscrito.getString("telegram")+"</td>");
			sb.append("<td>"+objInscrito.getString("email")+"</td>");
			if (objInscrito.getBoolean("alergias"))
			{
				sb.append("<td>"+"Alergias a:"+objInscrito.getString("alergiasTxt")+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin alergias notificadas"+"</td>");
			}
			if (objInscrito.getBoolean("alimentos"))
			{
				sb.append("<td>"+"Dieta: "+objInscrito.getString("alimentosTxt")+"</td>");
			}
			else
			{
				sb.append("<td>"+"Sin dieta específica notificada"+"</td>");
			}
			sb.append("<td>"+objInscrito.getString("tipoHabitacion")+"</td>");
			sb.append("<td>"+objInscrito.getString("importePlaza")+"</td>");
			//sb.append("<td>"+objInscrito.getString("estadoPagos")+"</td>");
			sb.append("<td>"+objInscrito.getString("habitacion")+"</td>");
			sb.append("<td>"+df.format(new Date(objInscrito.getLong("fecha")))+"</td>");
			sb.append("<td>"+objInscrito.getString("observaciones")+"</td>");
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
		inscritos.put("desalojados",SQLInscritos.selectInscritosSinHabitacion().toString() );
		inscritos.put("espera",SQLInscritos.selectInscritosListaEspera().toString() );
		Logger.registrarActividadSession(request, "Exportando listado de inscritos a web.");
		response.setCharacterEncoding("ISO-8859-15");
		response.setContentType("text/html");
		response.getWriter().append(formatearListado(inscritos));
	}


}
