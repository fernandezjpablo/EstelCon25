package es.ste.aderthad.publico.planificacion;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;


import es.ste.aderthad.publico.log.LoggerPublic;
import es.ste.aderthad.publico.properties.EntornoPublic;
import es.ste.aderthad.publico.sql.SQLPlanificacionPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ActividadesSala
 */
@WebServlet("/ActividadesSala")
public class ActividadesSala extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ActividadesSala() {
        // TODO Auto-generated constructor stub
    }

    
    private String pintarCabecera()
    {
    	StringBuilder sb=new StringBuilder();
    	sb.append("<div class=\"contenido_calendario\">");
    	
    	
    	


    	

    	


return sb.toString();


    	
    	
    }
    
    private String pintarPie()
    {
    	StringBuilder sb=new StringBuilder();
    	sb.append("    </div>");
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sala=request.getParameter("sala");
		response.getWriter().println("Actividades de la sala: "+sala);
		boolean planificacionVisible=EntornoPublic.getVariable("PLANIFICACION_VISIBLE").toUpperCase().equals("SI");
		
		
		
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
		
		if (planificacionVisible)
		{
		resultado.append("<table style=\"border:2px solid\" id=\"tabla_plani\"><thead><tr><th>Horas</th>");
		try {	
		fechaInicio=SQLPlanificacionPublic.getFechaMin();
		fechaFin=SQLPlanificacionPublic.getFechaMax();
		if (fechaInicio==null) fechaInicio="01-01-1900";
		if (fechaFin==null) fechaFin="01-01-1900";
		String horaInicioStr=SQLPlanificacionPublic.getHoraMin();
		if (horaInicioStr==null) horaInicioStr="9";
		horaInicio=Long.valueOf(horaInicioStr);
		String horaFinStr=SQLPlanificacionPublic.getHoraMax();
		if (horaFinStr==null) horaFinStr="22";
		horaFin=Long.valueOf(horaFinStr);
		intervalo=30;
		String espacio="";
		
		
	
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
			
				String bean=SQLPlanificacionPublic.select(fechasStr.get(d).replaceAll("/","-"),h,intervalo,sala);
				JSONArray actividades=new JSONArray(bean);
				String actividadAnt="";
				String actividadAct="";
				String color="";
				String backgroundcolor="white";
				for (int a=0;a<actividades.length();a++)
				{
					backgroundcolor=actividades.getJSONObject(a).getString("color");
					if (backgroundcolor.equals("")) backgroundcolor="white";
					color="black";
					actividadAct=actividades.getJSONObject(a).getString("nombre");
					espacio=actividades.getJSONObject(a).getString("espacio");
					if (!actividadAct.equals(actividadAnt))
					{
						resultado.append("<span style=\"color:"+color+";background-color:"+corregirColor(backgroundcolor)+"\">"+actividadAct+"<br>("+espacio+")</span><br>");
						actividadAnt=actividadAct;
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
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		resultado.append("</table>");
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
