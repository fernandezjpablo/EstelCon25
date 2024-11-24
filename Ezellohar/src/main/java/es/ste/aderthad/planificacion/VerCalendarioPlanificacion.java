package es.ste.aderthad.planificacion;

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


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLPlanificacion;

/**
 * Servlet implementation class VerCalendarioPlanificacion
 */
@WebServlet("/admin/planificacion/VerCalendarioPlanificacion")
public class VerCalendarioPlanificacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public VerCalendarioPlanificacion() {
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
		fechaInicio=SQLPlanificacion.getFechaMin();
		fechaFin=SQLPlanificacion.getFechaMax();
		horaInicio=Long.valueOf(SQLPlanificacion.getHoraMin());
		horaFin=Long.valueOf(SQLPlanificacion.getHoraMax());
		intervalo=15;
		
		if (fechaInicio!=null)
		{
		try {
			fechaIniDt=df.parse(fechaInicio);


			fechaFinDt=df.parse(fechaFin);

		Time intervaloTime=new Time(Long.valueOf(intervalo));
		Date unDia=new Date(24*60*60*1000);
		resultado.append("<table style=\"border:2px solid\" id=\"tabla_plani\"><thead><tr><th>Horas</th>");
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
				String espacio="";
				String color="";
				String idActividad="";
				String aforo="";
				int aforoInt=0;
				String ponente="";
				JSONObject actividadBean=null;
				String backgroundcolor="white";
				for (int a=0;a<actividades.length();a++)
				{
					backgroundcolor=actividades.getJSONObject(a).getString("color");
					if (backgroundcolor.equals("")) backgroundcolor="white";
					color="black";
					actividadAct=actividades.getJSONObject(a).getString("nombre");
					idActividad=actividades.getJSONObject(a).getString("id");
					actividadBean=SQLActividades.consultarActividad(idActividad);
					if (actividadBean.has("nombres_responsables"))
					{
						ponente=actividadBean.getString("nombres_responsables");
						if (!"".equals(ponente))
						{
							ponente=" con "+ponente;
						}
					}
					if (actividadBean.has("aforo"))
					{
						aforoInt=actividadBean.getInt("aforo");
						aforo=String.valueOf(aforoInt);
						if (aforoInt>0)
						{
							aforo="<br>Plazas: "+aforo;
						}
						else
						{
							aforo="";
						}
					}
					if (actividadBean.has("nombreActividad"))
					{
						actividadAct=actividadBean.getString("nombreActividad");
					}
					else
					{
						actividadAct="(Sin nombre de actividad)";
					}
					
					espacio=actividades.getJSONObject(a).getString("espacio");
					if (!actividadAct.equals(actividadAnt))
					{
						resultado.append("<span style=\"color:"+color+";background-color:"+backgroundcolor+"\">"+actividadAct+ponente+aforo+"<br>("+espacio+")</span><br>");
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
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		}
		else
		{	
			resultado.append("<table style=\"border:2px solid\" id=\"tabla_plani\"><thead><tr><th>Horas</th>");
			resultado.append("<tr><td>Aún no hay actividades planificadas</td></tr>");
		}
		resultado.append("</table>");
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(resultado.toString());
		
	}


}
