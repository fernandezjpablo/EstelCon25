package es.ste.aderthad.planificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.PlanificacionBean;
import es.ste.aderthad.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class ObtenerTablaPlanificacion
 */
@WebServlet("/admin/planificacion/ObtenerTablaPlanificacion")
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
		
		StringBuilder resultado=new StringBuilder();
		String fechaInicio;
		ArrayList<String> fechasStr=new ArrayList<String>();
		String fechaFin;
		String espacios;
		long horaInicio;
		long horaFin;
		long intervalo;
		Date fechaIniDt;
		Date fechaFinDt;
		Date fechaIteracion;
		String fechaFormato="";
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
		String[] espaciosArr;
		String[] nombreEspaciosArr;
		fechaInicio=request.getParameter("fechaInicio");
		fechaFin=request.getParameter("fechaFin");
		horaInicio=Long.valueOf(request.getParameter("horaInicio"))*60;
		horaFin=Long.valueOf(request.getParameter("horaFin"))*60;
		intervalo=Long.valueOf(request.getParameter("intervalo"));
		espaciosArr=request.getParameter("espacios").split(",");
		nombreEspaciosArr=request.getParameter("nomespacios").split(",");
		fechaIniDt=new Date(Long.valueOf(fechaInicio));
		fechaFinDt=new Date(Long.valueOf(fechaFin));
		Time intervaloTime=new Time(Long.valueOf(intervalo));
		Date unDia=new Date(24*60*60*1000);
		resultado.append("<table id=\"tabla_plani\"><thead><tr><th>Horas</th>");
		fechaIteracion=fechaIniDt;
		int numDias=0;
		int numColumnas=0;
		while (fechaIteracion.compareTo(fechaFinDt)<=0)
		{
			numDias++;
			fechasStr.add(df.format(fechaIteracion));
			for (int e=0;e<espaciosArr.length;e++)
			{
				if (e==0)
				{
					resultado.append("<th>"+df.format(fechaIteracion)+"</th>");
				}
				else
				{
					resultado.append("<th></th>");
				}
				numColumnas++;
			}

			
			fechaIteracion=new Date(fechaIteracion.getTime()+unDia.getTime());
		}
		resultado.append("</tr></thead>");
		resultado.append("<tr><td></td>");
	for (int d=0;d<numDias;d++)
	{
		for (int c=0;c<espaciosArr.length;c++)
		{
			resultado.append("<td>"+nombreEspaciosArr[c]+"</td>");
		}		
	}
	resultado.append("</tr>");
	
	for (long h=horaInicio;h<horaFin-1;h+=intervalo)
	{
		resultado.append("<tr>");
		resultado.append("<td>"+formatoHora(h)+" a "+formatoHora(h+intervalo)+"</td>");
		
		for (int d=0;d<numDias;d++)
		{
			for (int c=0;c<espaciosArr.length;c++)
			{
			String idBuscado=fechasStr.get(d).replaceAll("/","-")+";"+espaciosArr[c]+";"+h;
			resultado.append("<td id=\"celda-fecha-"+fechasStr.get(d).replaceAll("/","-")+"-espacio-"+espaciosArr[c]+"-tiempo-"+h+"-intervalo-"+intervalo+"\" >");
			if (!SQLPlanificacion.existe(idBuscado))
			{
					resultado.append("<input class=\"selplani\" type=\"checkbox\" id=\"chkplani-fecha-"+fechasStr.get(d).replaceAll("/","-")+"-espacio-"+espaciosArr[c]+"-tiempo-"+h+"-intervalo-"+intervalo+"\" onClick=\"addCasillaActividad('fecha-"+fechasStr.get(d).replaceAll("/","-")+"-espacio-"+espaciosArr[c]+"-tiempo-"+h+"-intervalo-"+intervalo+"');\">");
			}
			else
			{
				PlanificacionBean bean=SQLPlanificacion.select(idBuscado);
				if (!bean.getActividad().isEmpty())
				{
				resultado.append("<span onClick=\"removeCasillaActividad('fecha-"+fechasStr.get(d).replaceAll("/","-")+"-espacio-"+espaciosArr[c]+"-tiempo-"+h+"-intervalo-"+intervalo+"');\" style=\"background-color:"+bean.getColor()+"\">"+bean.getNombreActividad()+"</span>");
				}
				else
				{
					resultado.append("<input class=\"selplani\" type=\"checkbox\" id=\"chkplani-fecha-"+fechasStr.get(d).replaceAll("/","-")+"-espacio-"+espaciosArr[c]+"-tiempo-"+h+"-intervalo-"+intervalo+"\" onClick=\"addCasillaActividad('fecha-"+fechasStr.get(d).replaceAll("/","-")+"-espacio-"+espaciosArr[c]+"-tiempo-"+h+"-intervalo-"+intervalo+"');\">");
				}
			}
			resultado.append("</td>");
			}		
		}
		
		
		resultado.append("</tr>");
	}
	
		resultado.append("</table>");
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(resultado.toString());
		
	}


}
