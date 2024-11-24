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
import es.ste.aderthad.inscritos.sql.SQLEspacios;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class MostrarCalendario
 */
@WebServlet("/MostrarCalendario")
public class MostrarCalendario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MostrarCalendario() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private String formatoHora(long tiempo)
    {
    	if (tiempo % 15 ==0)
    	{
    	long tiempoTmp=tiempo/60;
    	if (tiempoTmp>23) tiempoTmp-=24;
    	String hora=("00"+String.valueOf(tiempoTmp));
    	hora=hora.substring(hora.length()-2,hora.length());
    	String minutos="00"+String.valueOf(tiempo%60);
    	minutos=minutos.substring(minutos.length()-2,minutos.length());
    	 	return hora+":"+minutos;
    	}
    	else
    	{
    		return "&nbsp;";
    	}
    }   
    
    private String formatearDescripciones()
    {
    	StringBuilder sb=new StringBuilder();
    	String idActividad="";
    	String descripcion="";
    	JSONArray lista=SQLActividades.listarActividades();
    	for (int a=0;a<lista.length();a++)
    	{
    		idActividad=lista.getJSONObject(a).getString("idActividad");
    		descripcion=lista.getJSONObject(a).getString("descripcion");
			if (!"".equals(descripcion.trim()))
			{
				sb.append("<div id=\"descripcion-"+idActividad+"\" class=\"descripcionActividad\" onClick=\"mostrarDescripcion('"+idActividad+"');\">"+descripcion+"</div>");
			}
    	}
    	return sb.toString();
    }
    
    private String corregirColor(String color)
    {

    	 
    	String resultado=color;
    	if ("white".equals(color)) resultado="ghostwhite";
    	if ("red".equals(color)) resultado="PowderBlue";
    	if ("fuchsia".equals(color)) resultado="Lavender";
    	if ("lime".equals(color)) resultado="Lightcyan";
    	if ("green".equals(color)) resultado="LavenderBlush";
    	if ("yellow".equals(color)) resultado="Linen";
    	if ("aqua".equals(color)) resultado="MintCream";
    	if ("aquamarine".equals(color)) resultado="MistyRose";
    	if ("beige".equals(color)) resultado="HoneyDew";
    	if ("burlywood".equals(color)) resultado="AliceBlue";
    	if ("bisque".equals(color)) resultado="LightBlue";
    	if ("darkseagreen".equals(color)) resultado="LightskyBlue";
    	if ("deepskyblue".equals(color)) resultado="LightSteelBlue";
    	if ("lavender".equals(color)) resultado="PeachPuff";    	
    	if ("plum".equals(color)) resultado="Plum";   
    	
    	return resultado;
    }
    
    private String formatearDia(JSONObject dia,JSONArray espacios,long horaInicio,long horaFin,int intervalo)
    {
    	StringBuilder sb=new StringBuilder();
    	StringBuilder sbTemp;
    	String idEspacio="";
    	JSONObject intervalosEspacio;
    	int contadorActividades=0;
    	String nombreAct;
    	JSONArray nombreAnt=new JSONArray();
    	String ponente="";
    	String aforo="";
    	String color="";
    	String descripcionStr="";
    	String descripcion="";
    	String idActividad="";
    	String divsActividades="";
    	String publico="";
    	//sb.append("<table border=6px><tr>");
    	sb.append("<div style=\"display:flex;flex-direction:row\">");
		sb.append("<table style=\"border: solid 1px\">");
		sb.append("<thead>");
		sb.append("<th style=\"width:70px;\">Hora</th>");
		for (int e=0;e<espacios.length();e++)
		{
			sb.append("<th>"+espacios.getJSONObject(e).getString("nombreEspacio")+"</th>");
			nombreAnt.put(e,"");
		}
		sb.append("</thead>");
		sb.append("<tbody>");
	

		for (long h=horaInicio;h<horaFin;h+=intervalo)
		{
			contadorActividades=0;
			nombreAct="";	
			sbTemp=new StringBuilder();
			sbTemp.append("<tr style=\"margin:0px\">");
			sbTemp.append("<td style=\"border:solid 1px\">"+formatoHora(h)+"</td>");
			for (int e=0;e<espacios.length();e++)
			{
				nombreAct="";		
			idEspacio=espacios.getJSONObject(e).getString("idEspacio");
			if (dia.has(idEspacio))
			{
				if (dia.getJSONObject(idEspacio).has("intervalos"))
				{
					intervalosEspacio=dia.getJSONObject(idEspacio).getJSONObject("intervalos");
				}
				else
				{
					intervalosEspacio=new JSONObject();
				}
			}
			else
			{
				intervalosEspacio=new JSONObject();
			}

			if (intervalosEspacio.has(String.valueOf(h)))
			{
				contadorActividades++;
				nombreAct=intervalosEspacio.getJSONObject(String.valueOf(h)).getString("nombreActividad");
				idActividad=intervalosEspacio.getJSONObject(String.valueOf(h)).getString("actividad");
				ponente=intervalosEspacio.getJSONObject(String.valueOf(h)).getString("ponente");
				color=intervalosEspacio.getJSONObject(String.valueOf(h)).getString("color");
				descripcionStr=intervalosEspacio.getJSONObject(String.valueOf(h)).getString("descripcion");
				descripcion="";
				if (!"".equals(descripcionStr.trim()))
				{
					descripcion="<a href=\"javascript:void(mostrarDescripcion('"+idActividad+"'));\">(+)</a>";
				}
				
				aforo=String.valueOf(intervalosEspacio.getJSONObject(String.valueOf(h)).getInt("aforo"));
				publico=String.valueOf(intervalosEspacio.getJSONObject(String.valueOf(h)).getString("publico"));
				sbTemp.append("<td style=\"border:solid 1px;background-color:"+corregirColor(color)+"\">");
				if (!nombreAnt.getString(e).equals(nombreAct))
				{
					//sb.append("<hr>");
					sbTemp.append(nombreAct);
					if (!"".equals(ponente) && !ponente.startsWith("("))
					{
						sbTemp.append("<br>con "+ponente);
					}
					if (!"0".equals(aforo))
					{
						sbTemp.append("<br>Plazas: "+aforo);
					}
					if ("menores".equals(publico.toLowerCase()))
					{
						sbTemp.append("<br>(Actividad Infantil)");
					}
					
					sbTemp.append(descripcion);

				}

				//sb.append("<td></td>");

				nombreAnt.put(e,nombreAct);
			}
			else
			{
				nombreAnt.put(e,"");
				nombreAct="";
				sbTemp.append("<td style=\"border:solid 1px\"></td>");
			}

		}
			sbTemp.append("</tr>");
			if (contadorActividades>0) sb.append(sbTemp.toString());
		}
		sb.append("</tbody>");			
		sb.append("</table>");
		sb.append("</div>");
		sb.append(divsActividades);
		return sb.toString();
    }
     
    
    private String pintarCabecera()
    {
    	StringBuilder sb=new StringBuilder();
    	sb.append("<!DOCTYPE html>");
    	sb.append("<html><head><meta charset=\"UTF-8\"><title>Bienvenido a la Mereth Aderthad</title><link rel=\"stylesheet\" type=\"text/css\" href=\"css/estilos.css\" />");
    	sb.append("    	<!-- Cookie Consent by TermsFeed https://www.TermsFeed.com -->"
    			+ "    	<script type=\"text/javascript\" src=\"//www.termsfeed.com/public/cookie-consent/4.1.0/cookie-consent.js\" charset=\"UTF-8\"></script>"
    			+ "    	<script type=\"text/javascript\" charset=\"UTF-8\">"
    			+ "    	document.addEventListener('DOMContentLoaded', function () {"
    			+ "    	cookieconsent.run({\"notice_banner_type\":\"simple\",\"consent_type\":\"express\",\"palette\":\"light\",\"language\":\"es\",\"page_load_consent_levels\":[\"strictly-necessary\"],\"notice_banner_reject_button_hide\":false,\"preferences_center_close_button_hide\":false,\"page_refresh_confirmation_buttons\":false});"
    			+ "    	});"
    			+ "    	</script><!-- Unnamed script -->"
    			+ "    	<!-- Google tag (gtag.js) -->"
    			+ "    	<script type=\"text/plain\" data-cookie-consent=\"tracking\" async src=\"https://www.googletagmanager.com/gtag/js?id=G-YVMT01CDJD\"></script>"
    			+ "    	<script type=\"text/plain\" data-cookie-consent=\"tracking\">"
    			+ "    	  window.dataLayer = window.dataLayer || [];"
    			+ "    	  function gtag(){dataLayer.push(arguments);}"
    			+ "    	  gtag('js', new Date());"
    			+ ""
    			+ "    	  gtag('config', 'G-YVMT01CDJD');"
    			+ "    	</script>"
    			+ "    	<!-- end of Unnamed script--><noscript>Free cookie consent management tool by <a href=\"https://www.termsfeed.com/\">TermsFeed</a></noscript>"
    			+ "    	<!-- End Cookie Consent by TermsFeed https://www.TermsFeed.com --><!-- Below is the link that users can use to open Preferences Center to change their preferences. Do not modify the ID parameter. Place it where appropriate, style it as needed. -->"
    			+ ""
    			+ "    	</head>"
    			+ "    	<BODY class=\"fondo\" style=\"background-color:cornsilk;overflow:scroll\"><script src=\"jquery/jquery-3.6.1.min.js\"></script>"
       			+"<div class=\"barramenu_calendario\" style=\"margin-left:0%\">"
    			+ "    	   <img class=\"telempe_calendario\" src=\"img/telempe.png\" alt=\"logo\"><p>Mereth Aldaron Enyali&euml; - Horario</p><img class=\"telempe_calendario\" src=\"img/laure.png\" alt=\"logo\">"
       			+"</div>"
    			+"<script type=\"text/javascript\">function mostrarDescripcion(id){"
       			+"$('#descripcion-'+id).toggle();"
    			+ "};</script>"+
       			"<script src=\"script/inscritos.js\"></script>"
   			+"<div class=\"contenido\" style=\"display:flex;flex-direction:column\">"

     			+ "    	");
    	
    	
    	


    	

    	


return sb.toString();


    	
    	
    }
    
    
    private String pintarPie()
    {
    	StringBuilder sb=new StringBuilder();
    	sb.append("    </div>"
    			+ ""
     			+ "    "
    			+ "</BODY></HTML>");
    	return sb.toString();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONArray calendario=SQLPlanificacion.selectCalendarioPorEspacios();
		JSONArray espacios=SQLEspacios.listarEspacios();
		StringBuilder sb=new StringBuilder();
		StringBuilder sbHead=new StringBuilder();
		JSONObject dia;
		String horaInicioStr=SQLPlanificacion.getHoraMin();
		if (horaInicioStr==null) horaInicioStr="9";
		long horaInicio=Long.valueOf(horaInicioStr);
		String horaFinStr=SQLPlanificacion.getHoraMax();
		if (horaFinStr==null) horaFinStr="22";
		long horaFin=Long.valueOf(horaFinStr);
		int intervalo=5;
		String display="block";
		for (int d=0;d<calendario.length();d++)
		{
			if (d>0) display="none";
			dia=calendario.getJSONObject(d);
			sbHead.append("<input type=\"button\" value=\""+dia.getString("fecha")+"\" onClick=\"toggleDia('dia-"+dia.getString("fecha")+"');\" class=\"box_button\"> ");
			sb.append("<div id=\"dia-"+dia.getString("fecha")+"\" class=\"planificaciondia\" style=\"display:"+display+"\">");
			sb.append("<h2>"+dia.getString("fecha")+"</h2>");
			sb.append(formatearDia(dia,espacios,horaInicio,horaFin,intervalo));

			sb.append("</div>");
		}
		response.setContentType("text/html;charset=ISO-8859-15");
		response.getWriter().println(pintarCabecera());
		response.getWriter().println("<br><br><div style=\"width:100%;display:flex;\">"+sbHead.toString()+"</div><br>");
		response.getWriter().println(sb.toString());
		response.getWriter().println(formatearDescripciones());
		response.getWriter().println(pintarPie());
	}


}
