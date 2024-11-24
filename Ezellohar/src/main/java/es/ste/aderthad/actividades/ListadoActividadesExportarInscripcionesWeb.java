package es.ste.aderthad.actividades;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * Servlet implementation class ListadoActividadesExportarInscripciones
 */
@WebServlet("/admin/actividades/ListadoActividadesExportarInscripcionesWeb")
public class ListadoActividadesExportarInscripcionesWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListadoActividadesExportarInscripcionesWeb() {
        // TODO Auto-generated constructor stub
    }

    private String getInscritos(String idActividad)
    {
    	StringBuilder sb=new StringBuilder();
    	SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String idinscrito="";
    	JSONArray inscritos=SQLInscripcionesActividades.listarInscritos(idActividad);
    	if (inscritos.length()==0) 
    		{
    		sb.append("<tr><td></td><td></td><td></td><td></td><td>(Actualmente sin inscripciones)</td></tr>");
    		}
    	else {

    	for (int i=0;i<inscritos.length();i++)
    	{
    		idinscrito=inscritos.getJSONObject(i).getString("id");

    		sb.append("<tr><td></td><td></td><td></td><td></td><td>"+inscritos.getJSONObject(i).getString("nombre")+" "+inscritos.getJSONObject(i).getString("apellidos"));
    		if (!inscritos.getJSONObject(i).getString("pseudonimo").equals(""))
    		{
    			sb.append(" \""+inscritos.getJSONObject(i).getString("pseudonimo")+"\"");
    		}
    		if (inscritos.getJSONObject(i).getInt("estado")==9)
    		{
    			sb.append("<td>(lista de espera)</td>");
    		}
    		else
    		{
    			sb.append("<td>(inscrito)</td>");
    		}

    		sb.append("<td>"+inscritos.getJSONObject(i).getString("observaciones").replaceAll(";",".").replaceAll("\n", ".")+"</td>");
    		sb.append("</tr>");

    	}
  
    	}
    	return sb.toString();
    	
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado;
		listado=SQLActividades.listarActividades();
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		StringBuilder sb=new StringBuilder();
		sb.append("<style>table, th, td {"
				+ "  border: 1px solid black;"
				+ "}</style>");
		sb.append("<table style=\"border:solid 1px black\"><thead><th>Actividad</th><th>Tipo</th><th>Aforo</th><th>Público</th><th>Inscritos</th><th>Estado</th><th>Observaciones</th></thead>");
		sb.append("<tbody>");
		for (int l=0;l<listado.length();l++)
		{
			sb.append("<tr>");
			if (listado.getJSONObject(l).has("nombreActividad"))
			{
				sb.append("<td>"+listado.getJSONObject(l).getString("nombreActividad")+"</td>");
			}
			else
			{
				sb.append("<td>(Sin nombre)</td>");
			}

			if (listado.getJSONObject(l).has("tipo"))
			{
				sb.append("<td>"+listado.getJSONObject(l).getString("tipo")+"</td>");
			}
			else
			{
				sb.append("<td>(No especificado)</td>");
			}
			if (listado.getJSONObject(l).has("aforo"))
			{
				sb.append("<td>"+listado.getJSONObject(l).getInt("aforo")+"</td>");
			}
			else
			{
				sb.append("<td>(No especificado)</td>");
			}
			if (listado.getJSONObject(l).has("publico"))
			{
				sb.append("<td>"+listado.getJSONObject(l).getString("publico")+"</td>");
			}
			else
			{
				sb.append("<td>(No especificado)</td>");
			}
			sb.append("</tr>");
			sb.append(getInscritos(listado.getJSONObject(l).getString("idActividad")));
			sb.append("\n");
		}
		sb.append("</tbody>");
		sb.append("</table>");
		response.setCharacterEncoding("ISO-8859-15");
		response.getWriter().println(sb.toString());
	}

}
