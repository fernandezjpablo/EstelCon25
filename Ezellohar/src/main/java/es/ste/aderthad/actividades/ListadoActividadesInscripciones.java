package es.ste.aderthad.actividades;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.json.JSONArray;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLPagos;


/**
 * Servlet implementation class ListadoActividadesInscripciones
 */
@WebServlet("/admin/actividades/ListadoActividadesInscripciones")
public class ListadoActividadesInscripciones extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListadoActividadesInscripciones() {
        // TODO Auto-generated constructor stub
    }

    private String getInscritos(String idActividad,boolean pago_adicional)
    {
    	StringBuilder sb=new StringBuilder();
    	String estiloInscrito="";
    	SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String idinscrito="";
    	JSONArray inscritos=SQLInscripcionesActividades.listarInscritos(idActividad);
    	if (inscritos.length()==0) 
    		{
    		sb.append("(Actualmente sin inscripciones)");
    		}
    	else {
    		sb.append("<input type=\"button\" class=\"box_button_main\" value=\"Ver inscritos\" onClick=\"$('#inscritos-"+idActividad+"').toggle();\">");
    		sb.append("<div id=\"inscritos-"+idActividad+"\" style=\"display:none\">");
    		sb.append("<table><tbody>");
    	for (int i=0;i<inscritos.length();i++)
    	{
    		estiloInscrito="";
    		if (inscritos.getJSONObject(i).getInt("estado")==9)
    		{
    			estiloInscrito="background-color:darkgray";
    		}
    		idinscrito=inscritos.getJSONObject(i).getString("id");
    		sb.append("<tr><td style=\"border: 0px;"+estiloInscrito+"\">");
    		if (inscritos.getJSONObject(i).getInt("estado")==9)
    		{
    			sb.append("<input type=\"checkbox\" value=\""+idinscrito+";"+idActividad+"\" class=\"espera\">");
    		}
    		else
    		{
    			sb.append("<input type=\"checkbox\" value=\""+idinscrito+";"+idActividad+"\" class=\"inscrito\">");
    		}
    		sb.append(inscritos.getJSONObject(i).getString("nombre")+" "+inscritos.getJSONObject(i).getString("apellidos"));
    		if (!inscritos.getJSONObject(i).getString("pseudonimo").equals(""))
    		{
    			sb.append("\""+inscritos.getJSONObject(i).getString("pseudonimo")+"\" (");
    		}


    		if (inscritos.getJSONObject(i).getInt("estado")==9)
    		{
    			sb.append("En lista de espera - ");
    		}
    		else
    		{
    			sb.append("Inscrito - ");
    		}
    		sb.append(df.format(inscritos.getJSONObject(i).getLong("fecha"))+") (Actualizado el "+df.format(inscritos.getJSONObject(i).getLong("fechaUpdate"))+")");
    		sb.append("<br>");
    		if (pago_adicional)
    		{
    			sb.append("</tr><tr>");
    		//	if (inscritos.getJSONObject(i).getInt("estado")==0)
    			//{
    				sb.append("<td style=\"border:0px;\"><b>Pago actividad:</b><input type=\"text\" id=\"pago-"+idActividad+"-"+idinscrito+"\">");
    				sb.append("<input type=\"button\" value=\"Registrar pago\" onClick=\"registrarPagoActividad('"+idActividad+"','"+idinscrito+"');\"></td>");
    		//	}
 /*   			else
    			{
    				sb.append("<td style=\"border:0px;\"><b>Importe a anular:</b><input type=\"text\" id=\"pago-"+idActividad+"-"+idinscrito+"\" disabled>");
    				sb.append("<input type=\"button\" value=\"Anular pago\" onClick=\"anularPagoActividad('"+idActividad+"','"+idinscrito+"');\"></td>");
    			}*/
    			sb.append("</tr><tr><td style=\"border: 0px;\">");
    		}    		

    		sb.append("<b>Observaciones:</b><br>");
    		sb.append("<textarea id=\"obser-"+idinscrito+"-"+idActividad+"\">"+inscritos.getJSONObject(i).getString("observaciones")+"</textarea>");
    		sb.append("<br><input type=\"button\" value=\"Actualizar observaciones\" onClick=\"actualizarObservaciones('"+idinscrito+"','"+idActividad+"');\">");
    		sb.append("</td>");
    		sb.append("</tr>");
    		if (pago_adicional)
    		{
    			String movimientosStr=formatearMovimiento(SQLPagos.selectPagosUsuarioActividad(idinscrito, idActividad));
    			if (!movimientosStr.equals(""))
    			{
    				sb.append("<tr><td style=\"border: 0px;\">");
    				sb.append("<input type=\"button\" value=\"Ver pagos\" onClick=\"consultarPagosActividades('"+idActividad+"','"+idinscrito+"');\"><br>");
    				sb.append("<div id=\"pagos-"+idActividad+"-"+idinscrito+"\" style=\"display:none\">");
    				sb.append(movimientosStr);
    				sb.append("</div>");
    				sb.append("</td></tr>");
    			}
    		}
    	}
    	sb.append("</tbody></table>");
    	sb.append("</div>");
    	}
    	return sb.toString();
    	
    }

	private String formatearMovimiento(JSONArray selectPagosUsuarioActividad) {
		StringBuilder sb=new StringBuilder();
		String estadoStr="(Completado)";
		
		for (int i=0;i<selectPagosUsuarioActividad.length();i++)
		{
			estadoStr="(Completado)";
			if (selectPagosUsuarioActividad.getJSONObject(i).getInt("estado")==99) estadoStr="(Anulado)";
			sb.append("Importe:"+selectPagosUsuarioActividad.getJSONObject(i).getLong("importe")+" "+estadoStr+"<br>");
		}
		return sb.toString();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado;
		listado=SQLActividades.listarActividades();
		StringBuilder sb=new StringBuilder();
		sb.append("<table><tbody>");
		for (int l=0;l<listado.length();l++)
		{
			if ((listado.getJSONObject(l).getInt("estado")==1) || (listado.getJSONObject(l).getInt("estado")==5))
			{
				sb.append("<tr><td><b>Nombre de la actividad:</b> "+listado.getJSONObject(l).getString("nombreActividad")+"<br>"+getInscritos(listado.getJSONObject(l).getString("idActividad"),listado.getJSONObject(l).getBoolean("pagoAdicional"))+"</td></tr>");
			}
			else
			{
				sb.append("<tr><td><b>Nombre de la actividad:</b> "+listado.getJSONObject(l).getString("nombreActividad")+"(Inscripciones desactivadas)<br>"+getInscritos(listado.getJSONObject(l).getString("idActividad"),listado.getJSONObject(l).getBoolean("pagoAdicional"))+"</td></tr>");
				//sb.append("<tr><td>"+listado.getJSONObject(l).getString("nombreActividad")+"<br>(No necesita inscripción)</td></tr>");
			}
		}
		response.getWriter().println(sb.toString());
	}

}
