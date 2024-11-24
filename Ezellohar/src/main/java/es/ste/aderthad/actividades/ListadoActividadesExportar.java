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

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;

/**
 * Servlet implementation class ListadoActividadesExportar
 */
@WebServlet("/admin/actividades/ListadoActividadesExportar")
public class ListadoActividadesExportar extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListadoActividadesExportar() {
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
    		sb.append("(Actualmente sin inscripciones)");
    		}
    	else {

    	for (int i=0;i<inscritos.length();i++)
    	{
    		idinscrito=inscritos.getJSONObject(i).getString("id");

    		sb.append(inscritos.getJSONObject(i).getString("nombre")+" "+inscritos.getJSONObject(i).getString("apellidos"));
    		if (!inscritos.getJSONObject(i).getString("pseudonimo").equals(""))
    		{
    			sb.append("\""+inscritos.getJSONObject(i).getString("pseudonimo")+"\" ");
    		}


    		if (inscritos.getJSONObject(i).getInt("estado")==9)
    		{
    			sb.append("(E)");
    		}
    		else
    		{
    			sb.append("(I)");
    		}
    		sb.append(";");
	



    	}

    	}
    	return sb.toString();
    	
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado;
		listado=SQLActividades.listarActividades();
		String idresponsable;
		InscritoBean bean;
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		StringBuilder sb=new StringBuilder();
		sb.append("Actividad;Tipo;Público;Responsable;Nombre;Teléfono;email;Telegram;Inscripciones;Duración;Aforo;Observaciones;Requisitos;Inscritos\n");
		for (int l=0;l<listado.length();l++)
		{
			idresponsable=listado.getJSONObject(l).getString("responsables");
			bean=null;

			sb.append(listado.getJSONObject(l).getString("nombreActividad")+";");
			if (listado.getJSONObject(l).has("tipo"))
			{
				sb.append(listado.getJSONObject(l).getString("tipo")+";");
			}
			else
			{
				sb.append("(No especificado);");
			}
			sb.append(listado.getJSONObject(l).getString("publico")+";");
			sb.append(listado.getJSONObject(l).getString("nombres_responsables")+";");
			if (!idresponsable.equals(""))
			{
				bean=SQLInscritos.selectIdInscrito(idresponsable);
				sb.append(bean.getNombre()+" "+bean.getApellido()+" ("+bean.getPseudonimo()+");"+bean.getTelefono()+";"+bean.getEmail()+";"+bean.getTelegram()+";");
			}
			else
			{
				sb.append("(no consta);(no consta);(no consta);(no consta);");
			}
			if ((listado.getJSONObject(l).getInt("estado")==1) || (listado.getJSONObject(l).getInt("estado")==5))
			{
				sb.append("Activas");
			}
			else
			{
				sb.append("Inactivas");
			}
			sb.append(";");
			sb.append(listado.getJSONObject(l).getString("duracion")+";");
			sb.append(listado.getJSONObject(l).getInt("aforo")+";");
			if (listado.getJSONObject(l).has("observaciones"))
			{
				sb.append(listado.getJSONObject(l).getString("observaciones")+";");
			}
			else
			{
				sb.append(";");
			}
			if (listado.getJSONObject(l).has("requisitos"))
			{
				sb.append(listado.getJSONObject(l).getString("requisitos")+";");
			}
			else
			{
				sb.append(";");
			}
			sb.append(getInscritos(listado.getJSONObject(l).getString("idActividad")));
			sb.append("\n");
		}
		response.setCharacterEncoding("ISO-8859-15");
		response.setHeader("Content-Disposition", "attachment; filename=\"actividades-estelcon"+df.format(new Date())+".csv\"");
		response.getWriter().println(sb.toString());
	}



}
