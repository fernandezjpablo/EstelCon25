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
@WebServlet("/ObtenerTablaPlanificacion")
public class ObtenerTablaPlanificacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ObtenerTablaPlanificacion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
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
			
				String bean=SQLPlanificacion.select(fechasStr.get(d).replaceAll("/","-"),h,intervalo);
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
						resultado.append("<span style=\"color:"+color+";background-color:"+backgroundcolor+"\">"+actividadAct+"<br>("+espacio+")</span><br>");
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
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		resultado.append("</table>");
		}
		else //Planificación oculta para los inscritos
		{
			resultado.append("Calendario de actividades aún no disponible.");
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(resultado.toString());
		
	}


}
