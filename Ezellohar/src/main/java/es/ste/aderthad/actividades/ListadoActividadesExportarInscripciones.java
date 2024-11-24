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
@WebServlet("/admin/actividades/ListadoActividadesExportarInscripciones")
public class ListadoActividadesExportarInscripciones extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListadoActividadesExportarInscripciones() {
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
    		sb.append(";;;;;(Actualmente sin inscripciones)");
    		}
    	else {

    	for (int i=0;i<inscritos.length();i++)
    	{
    		idinscrito=inscritos.getJSONObject(i).getString("id");

    		sb.append(";;;;;"+inscritos.getJSONObject(i).getString("nombre")+" "+inscritos.getJSONObject(i).getString("apellidos"));
    		if (!inscritos.getJSONObject(i).getString("pseudonimo").equals(""))
    		{
    			sb.append(" \""+inscritos.getJSONObject(i).getString("pseudonimo")+"\"");
    		}
    		if (inscritos.getJSONObject(i).getInt("estado")==9)
    		{
    			sb.append(" (lista de espera);");
    		}
    		else
    		{
    			sb.append(" (inscrito);");
    		}

    		sb.append("Observaciones: "+inscritos.getJSONObject(i).getString("observaciones").replaceAll(";",".").replaceAll("\n", "."));
    		sb.append("\n");

    	}
  
    	}
    	return sb.toString();
    	
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado;
		listado=SQLActividades.listarActividades();
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		StringBuilder sb=new StringBuilder();
		sb.append("Actividad;Tipo;Público;Aforo;Inscritos;\n");
		for (int l=0;l<listado.length();l++)
		{
			if ((listado.getJSONObject(l).getInt("estado")==1) || (listado.getJSONObject(l).getInt("estado")==5))
			{
				sb.append(listado.getJSONObject(l).getString("nombreActividad")+";");
			}
			else
			{
				sb.append(listado.getJSONObject(l).getString("nombreActividad")+"(Inscripciones desactivadas);");
				//sb.append("<tr><td>"+listado.getJSONObject(l).getString("nombreActividad")+"<br>(No necesita inscripción)</td></tr>");
			}
			if (listado.getJSONObject(l).has("tipo"))
			{
				sb.append(listado.getJSONObject(l).getString("tipo")+";");
			}
			else
			{
				sb.append("(No especificado);");
			}
			sb.append(listado.getJSONObject(l).getString("publico")+";");
			sb.append(listado.getJSONObject(l).getInt("aforo")+";\n");
			sb.append(getInscritos(listado.getJSONObject(l).getString("idActividad")));
			sb.append("\n");
		}
		response.setCharacterEncoding("ISO-8859-15");
		response.setHeader("Content-Disposition", "attachment; filename=\"inscritos-actividades-estelcon"+df.format(new Date())+".csv\"");
		response.getWriter().println(sb.toString());
	}

}
