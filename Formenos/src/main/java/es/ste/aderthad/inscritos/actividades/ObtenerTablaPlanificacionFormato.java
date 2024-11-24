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

import es.ste.aderthad.inscritos.data.PlanificacionBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class ObtenerTablaPlanificacion
 */
@WebServlet("/HorarioActividades")
public class ObtenerTablaPlanificacionFormato extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ObtenerTablaPlanificacionFormato() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    
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
    			+ "    	<script type=\"text/plain\" data-cookie-consent=\"tracking\" async src=\"https://www.googletagmanager.com/gtag/js?id=G-Z42QC1F5ZH\"></script>"
    			+ "    	<script type=\"text/plain\" data-cookie-consent=\"tracking\">"
    			+ "    	  window.dataLayer = window.dataLayer || [];"
    			+ "    	  function gtag(){dataLayer.push(arguments);}"
    			+ "    	  gtag('js', new Date());"
    			+ ""
    			+ "    	  gtag('config', 'G-Z42QC1F5ZH');"
    			+ "    	</script>"
    			+ "    	<!-- end of Unnamed script--><noscript>Free cookie consent management tool by <a href=\"https://www.termsfeed.com/\">TermsFeed</a></noscript>"
    			+ "    	<!-- End Cookie Consent by TermsFeed https://www.TermsFeed.com --><!-- Below is the link that users can use to open Preferences Center to change their preferences. Do not modify the ID parameter. Place it where appropriate, style it as needed. -->"
    			+ ""
    			+ "    	</head>"
    			+ "    	<BODY style=\"background-color:cornsilk;overflow:scroll\"><script src=\"jquery/jquery-3.6.1.min.js\"></script>"
       			+"<div class=\"barramenu_calendario\" style=\"margin-left:0%\">"
    			+ "    	   <img class=\"logo_calendario\" src=\"img/pajaro2.png\" alt=\"logo\"><p>Mereth Cormalleness&euml; - Horario</p><img class=\"dulin_calendario\" src=\"img/pajaro.png\" alt=\"logo\">"
       			+"</div>"
    			+"<script type=\"text/javascript\">function mostrarDescripcion(id){"
       			+"$('#descripcion-'+id).toggle();"
    			+ "};</script>"
   			+"<div class=\"contenido_calendario\">"

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
    
    private String formatoHora(long tiempo)
    {
    	long tiempoTmp=tiempo/60;
    	if (tiempoTmp>23) tiempoTmp-=24;
    	String hora=("00"+String.valueOf(tiempoTmp));
    	hora=hora.substring(hora.length()-2,hora.length());
    	String minutos="00"+String.valueOf(tiempo%60);
    	minutos=minutos.substring(minutos.length()-2,minutos.length());
    	 	return hora+":"+minutos;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean planificacionVisible=EntornoInscritos.getVariable("PLANIFICACION_VISIBLE").toUpperCase().equals("SI");
		
		StringBuilder resultado=new StringBuilder();
		String fechaInicio;
		ArrayList<String> fechasStr=new ArrayList<String>();
		String fechaFin;
		String espacios;
		Long horaInicio;
		Long horaFin;
		long intervalo;
		Date fechaIniDt;
		Date fechaFinDt;
		Date fechaIteracion;
		String fechaFormato="";
		SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
		String[] nombreEspaciosArr;
		String divsActividades="";
		
		if (planificacionVisible)
		{
		resultado.append("<table style=\"border:2px solid\" id=\"tabla_plani\"><thead><tr><th>Horas</th>");
		try {	
		fechaInicio=SQLPlanificacion.getFechaMin();
		fechaFin=SQLPlanificacion.getFechaMax();
		if (fechaInicio==null) fechaInicio="01-01-1900";
		if (fechaFin==null) fechaFin="01-01-1900";
		String horaInicioStr=SQLPlanificacion.getHoraMin();
		if (horaInicioStr==null) horaInicioStr="9";
		horaInicio=Long.valueOf(horaInicioStr);
		String horaFinStr=SQLPlanificacion.getHoraMax();
		if (horaFinStr==null) horaFinStr="22";
		horaFin=Long.valueOf(horaFinStr);
		intervalo=15;
		String espacio="";
		String ponente="";
		String aforo="";
		String descripcion="";
		String descripcionStr="";

	
			fechaIniDt=df.parse(fechaInicio);


			fechaFinDt=df.parse(fechaFin);

		Time intervaloTime=new Time(Long.valueOf(intervalo));
		Date unDia=new Date(24*60*60*1000);
		
		fechaIteracion=fechaIniDt;
		int numDias=0;
		int numColumnas=1;
		while (fechaIteracion.compareTo(fechaFinDt)<=0)
		{
			numDias++;
			fechasStr.add(df.format(fechaIteracion));
					resultado.append("<th>"+df.format(fechaIteracion)+"</th>");


			
			fechaIteracion=new Date(fechaIteracion.getTime()+unDia.getTime());
		}
		resultado.append("</tr></thead>");
	
	for (long h=horaInicio;h<horaFin-1;h+=intervalo)
	{
		resultado.append("<tr>");
		resultado.append("<td style=\"border:1px solid\">"+formatoHora(h)+" a "+formatoHora(h+intervalo)+"</td>");
		
		for (int d=0;d<numDias;d++)
		{

			resultado.append("<td style=\"border:1px solid\" id=\"celda-fecha-"+fechasStr.get(d).replaceAll("/","-")+"-tiempo-"+h+"-intervalo-"+intervalo+"\" >");
			
				String bean=SQLPlanificacion.select(fechasStr.get(d).replaceAll("/","-"),h,intervalo);
				JSONArray actividades=new JSONArray(bean);
				String actividadAnt="";
				String actividadAct="";
				String idActividad="";
				String color="";
				String backgroundcolor="white";
				for (int a=0;a<actividades.length();a++)
				{
					backgroundcolor=actividades.getJSONObject(a).getString("color");
					if (backgroundcolor.equals("")) backgroundcolor="white";
					color="black";
					actividadAct=actividades.getJSONObject(a).getString("nombre");
					idActividad=actividades.getJSONObject(a).getString("id");
					espacio=actividades.getJSONObject(a).getString("espacio");
					ponente=actividades.getJSONObject(a).getString("responsable");
					aforo=actividades.getJSONObject(a).getString("aforo");
					descripcionStr=actividades.getJSONObject(a).getString("descripcion");
					if ("0".equals(aforo)) {
						aforo="";
					}
					else
					{
						aforo="<br>Plazas: "+aforo;
					}
					if (ponente.startsWith("(") || "".equals(ponente))
					{
						ponente="";
					}
					else
					{
						ponente="<br>con "+ponente;
					}
					descripcion="";
					if (!"".equals(descripcionStr.trim()))
					{
						descripcion="<a href=\"javascript:void(mostrarDescripcion('"+idActividad+"'));\">(+)</a>";
					}
					if (!actividadAct.equals(actividadAnt))
					{
						if (!"".equals(actividadAnt)) resultado.append("<hr>");
						resultado.append("<span style=\"color:"+color+";background-color:"+backgroundcolor+"\">"+actividadAct+"<br>("+espacio+")"+ponente+aforo+"</span>"+descripcion+"<br>");
						actividadAnt=actividadAct;
						if (!"".equals(descripcionStr.trim()))
						{
							divsActividades+="<div id=\"descripcion-"+idActividad+"\" class=\"descripcionActividad\" onClick=\"mostrarDescripcion('"+idActividad+"');\">"+descripcionStr+"</div>";
						}
					}
				}
				if (!actividadAct.equals(actividadAnt))
				{
					resultado.append("<span style=\"color:"+color+";background-color:"+backgroundcolor+"\">"+actividadAct+"<br>("+espacio+")</span><br>");
					actividadAnt=actividadAct;
				}				
			resultado.append("</td>");

		}
		
		
		resultado.append("</tr>");
	}
		} catch (ParseException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		resultado.append("</table>");
		resultado.append(divsActividades);
		}
		else //Planificación oculta para los inscritos
		{
			resultado.append("Horario de actividades aún no disponible.");
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(pintarCabecera());
		response.getWriter().println(resultado.toString());
		response.getWriter().println(pintarPie());
	}


}
