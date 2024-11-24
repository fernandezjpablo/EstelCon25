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
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLInscritos;

import org.json.JSONObject;

import es.ste.aderthad.sql.SQLInscritos;

/**
 * Servlet implementation class ExportarInscritos
 */
@WebServlet("/ExportarInscritos")
public class ExportarInscritos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ExportarInscritos() {
        // TODO Auto-generated constructor stub
    }

    private String parsearHtml(String contenido)
    {
    	String resultado=contenido;
    	contenido=contenido.replaceAll(";","");
    	contenido=contenido.replaceAll("&ntilde", "ñ");
    	contenido=contenido.replaceAll("&Ntilde", "Ñ");
    	contenido=contenido.replaceAll("&aacute", "á");
    	contenido=contenido.replaceAll("&oacute", "ó");
    	contenido=contenido.replaceAll("&iacute", "í");
    	contenido=contenido.replaceAll("&eacute", "é");
    	contenido=contenido.replaceAll("&uacute", "ú");
    	contenido=contenido.replaceAll("&Aacute", "Á");
    	contenido=contenido.replaceAll("&Oacute", "Ó");
    	contenido=contenido.replaceAll("&Iacute", "Í");
    	contenido=contenido.replaceAll("&Eacute", "É");
    	contenido=contenido.replaceAll("&Uacute", "Ú");
    	contenido=contenido.replaceAll("&iexcl", "¡");
    	contenido=contenido.replaceAll("<p>","");
    	contenido=contenido.replaceAll("</p>","");
    	contenido=contenido.replaceAll("\n"," ");
    	return contenido;
    }
    private String formatearListado(JSONObject inscritos)
    {
		StringBuilder sb=new StringBuilder();
		JSONArray alojados=new JSONArray(inscritos.getString("alojados"));
		JSONArray desalojados=new JSONArray(inscritos.getString("desalojados"));
		JSONArray listaespera=new JSONArray(inscritos.getString("espera"));
		JSONObject objInscrito;
		SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sb.append("APELLIDOS;NOMBRE;PSEUDONIMO;NIF;MENOR;CON BEBÉS;TELÉFONO;TELEGRAM;EMAIL;ALERGIAS;DIETA;TIPO HABITACIÓN;IMPORTE PLAZA;ESTADO PAGOS;HABITACIÓN;FECHA INSCRIPCIÓN;OBSERVACIONES;\n");
		for (int a=0;a<alojados.length();a++)
		{
			objInscrito=alojados.getJSONObject(a);
			sb.append(objInscrito.getString("apellidos")+";");
			sb.append(objInscrito.getString("nombre")+";");
			sb.append(objInscrito.getString("pseudonimo")+";");
			sb.append(objInscrito.getString("nif")+";");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("Menor de 14 años;");
			}
			else
			{
				sb.append("Adulto;");
			}
			if (objInscrito.has("con_bebes"))
			{
			if (!objInscrito.getString("con_bebes").isEmpty())
			{
				if (objInscrito.getString("con_bebes").equals("0"))
				{
					
					sb.append("Sin menores de 3 años;");
				}
				else
				{
					
					sb.append("Acompañado de menores de 3 años ("+objInscrito.getString("con_bebes")+");");
				}
			}
			else
			{
				sb.append("Sin menores de 3 años;");
			}
			}
			else
			{
				sb.append("Sin menores de 3 años;");
			}		
			sb.append(objInscrito.getString("telefono")+";");
			if (!objInscrito.has("telegram")) objInscrito.put("telegram", "(no indicado)");
			sb.append(objInscrito.getString("telegram")+";");
			sb.append(objInscrito.getString("email")+";");
			if (objInscrito.getBoolean("alergias"))
			{
				sb.append("Alergias a:"+objInscrito.getString("alergiasTxt")+";");
			}
			else
			{
				sb.append("Sin alergias notificadas;");
			}
			if (objInscrito.getBoolean("alimentos"))
			{
				sb.append("Dieta: "+objInscrito.getString("alimentosTxt")+";");
			}
			else
			{
				sb.append("Sin dieta específica notificada;");
			}
			sb.append(objInscrito.getString("tipoHabitacion")+";");
			sb.append(objInscrito.getString("importePlaza")+";");
			sb.append(objInscrito.getString("estadoPagos")+";");
			sb.append(objInscrito.getString("habitacion")+";");
			sb.append(df.format(new Date(objInscrito.getLong("fecha")))+";");
			sb.append(parsearHtml(objInscrito.getString("observaciones"))+";");
			sb.append("\n");
		}
		
		
		
		
		sb.append("DESALOJADOS\n");
		sb.append("APELLIDOS;NOMBRE;PSEUDONIMO;NIF;MENOR;CON BEBÉS;TELEFONO;TELEGRAM;EMAIL;ALERGIAS;DIETA;TIPO HABITACION;IMPORTE PLAZA;HABITACION;FECHA INSCRIPCIÓN;OBSERVACIONES;\n");
		for (int a=0;a<desalojados.length();a++)
		{
			objInscrito=desalojados.getJSONObject(a);
			sb.append(objInscrito.getString("apellidos")+";");
			sb.append(objInscrito.getString("nombre")+";");
			sb.append(objInscrito.getString("pseudonimo")+";");
			sb.append(objInscrito.getString("nif")+";");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("Menor de 14 años;");
			}
			else
			{
				sb.append("Adulto;");
			}
			if (objInscrito.has("con_bebes"))
			{
			if (!objInscrito.getString("con_bebes").isEmpty())
			{
				if (objInscrito.getString("con_bebes").equals("0"))
				{
					
					sb.append("Sin menores de 3 años;");
				}
				else
				{
					
					sb.append("Acompañado de menores de 3 años ("+objInscrito.getString("con_bebes")+");");
				}
			}
			else
			{
				sb.append("Sin menores de 3 años;");
			}
			}
			else
			{
				sb.append("Sin menores de 3 años;");
			}	
			sb.append(objInscrito.getString("telefono")+";");
			if (!objInscrito.has("telegram")) objInscrito.put("telegram", "(no indicado)");
			sb.append(objInscrito.getString("telegram")+";");
			sb.append(objInscrito.getString("email")+";");
			if (objInscrito.getBoolean("alergias"))
			{
				sb.append("Alergias a:"+objInscrito.getString("alergiasTxt")+";");
			}
			else
			{
				sb.append("Sin alergias notificadas;");
			}
			if (objInscrito.getBoolean("alimentos"))
			{
				sb.append("Dieta: "+objInscrito.getString("alimentosTxt")+";");
			}
			else
			{
				sb.append("Sin dieta específica notificada;");
			}
			sb.append(objInscrito.getString("tipoHabitacion")+";");
			sb.append(objInscrito.getString("importePlaza")+";");
			//sb.append(objInscrito.getString("estadoPagos")+";");
			sb.append(objInscrito.getString("habitacion")+";");
			sb.append(df.format(new Date(objInscrito.getLong("fecha")))+";");
			sb.append(parsearHtml(objInscrito.getString("observaciones"))+";");
			sb.append("\n");
		}
		
		
		sb.append("EN LISTA DE ESPERA\n");
		sb.append("APELLIDOS;NOMBRE;PSEUDONIMO;NIF;MENOR;CON BEBÉS;TELEFONO;TELEGRAM;EMAIL;ALERGIAS;DIETA;TIPO HABITACION;IMPORTE PLAZA;HABITACION;FECHA INSCRIPCIÓN;OBSERVACIONES;\n");
		for (int a=0;a<listaespera.length();a++)
		{
			objInscrito=listaespera.getJSONObject(a);
			sb.append(objInscrito.getString("apellidos")+";");
			sb.append(objInscrito.getString("nombre")+";");
			sb.append(objInscrito.getString("pseudonimo")+";");
			sb.append(objInscrito.getString("nif")+";");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("Menor de 14 años;");
			}
			else
			{
				sb.append("Adulto;");
			}
			if (objInscrito.has("con_bebes"))
			{
			if (!objInscrito.getString("con_bebes").isEmpty())
			{
				if (objInscrito.getString("con_bebes").equals("0"))
				{
					
					sb.append("Sin menores de 3 años;");
				}
				else
				{
					
					sb.append("Acompañado de menores de 3 años ("+objInscrito.getString("con_bebes")+");");
				}
			}
			else
			{
				sb.append("Sin menores de 3 años;");
			}
			}
			else
			{
				sb.append("Sin menores de 3 años;");
			}	
			sb.append(objInscrito.getString("telefono")+";");
			if (!objInscrito.has("telegram")) objInscrito.put("telegram", "(no indicado)");
			sb.append(objInscrito.getString("telegram")+";");
			sb.append(objInscrito.getString("email")+";");
			if (objInscrito.getBoolean("alergias"))
			{
				sb.append("Alergias a:"+objInscrito.getString("alergiasTxt")+";");
			}
			else
			{
				sb.append("Sin alergias notificadas;");
			}
			if (objInscrito.getBoolean("alimentos"))
			{
				sb.append("Dieta: "+objInscrito.getString("alimentosTxt")+";");
			}
			else
			{
				sb.append("Sin dieta específica notificada;");
			}
			sb.append(objInscrito.getString("tipoHabitacion")+";");
			sb.append(objInscrito.getString("importePlaza")+";");
			//sb.append(objInscrito.getString("estadoPagos")+";");
			sb.append(objInscrito.getString("habitacion")+";");
			sb.append(df.format(new Date(objInscrito.getLong("fecha")))+";");
			sb.append(parsearHtml(objInscrito.getString("observaciones"))+";");
			sb.append("\n");
		}
		return sb.toString();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject inscritos=new JSONObject();
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		inscritos.put("alojados",SQLInscritos.selectInscritos().toString() );
		inscritos.put("desalojados",SQLInscritos.selectInscritosSinHabitacion().toString() );
		inscritos.put("espera",SQLInscritos.selectInscritosListaEspera().toString() );
		Logger.registrarActividadSession(request, "Exportando listado de inscritos a excel.");
		response.setCharacterEncoding("ISO-8859-15");
		response.setHeader("Content-Disposition", "attachment; filename=\"inscripciones-estelcon"+df.format(new Date())+".csv\"");
		response.getWriter().append(formatearListado(inscritos));
	}


}
