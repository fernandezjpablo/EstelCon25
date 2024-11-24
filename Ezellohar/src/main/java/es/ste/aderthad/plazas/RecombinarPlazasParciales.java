package es.ste.aderthad.plazas;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.HabitacionParcialBean;
import es.ste.aderthad.sql.SQLHabitaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecombinarPlazasParciales
 */
@WebServlet("/admin/plazas/RecombinarPlazasParciales")
public class RecombinarPlazasParciales extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RecombinarPlazasParciales() {
        // TODO Auto-generated constructor stub
    }

private JSONObject clasificarHabitaciones(String listado)
{
	JSONObject resultado=new JSONObject();
	String[] lista=listado.split(",");
	String idraiz="";
	for (int i=0;i<lista.length;i++)
	{
		idraiz=lista[i].substring(0,lista[i].lastIndexOf("-"));
		if (!resultado.has(idraiz))
		{
			resultado.put(idraiz, new JSONArray());
		}
		resultado.getJSONArray(idraiz).put(lista[i]);
	}
	
	return resultado;
}

private boolean comprobar(JSONArray recibidas,JSONArray recuperadas)
{
	boolean resultadoEstados=recibidas.length()==recuperadas.length();
	if (resultadoEstados)
	{
	for (int i=0;i<recuperadas.length();i++)
	{
		resultadoEstados=resultadoEstados && (recuperadas.getJSONObject(i).getInt("estado")==0);
	}
	}
	return resultadoEstados;
}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String listaPlazas=request.getParameter("plazas");
		StringBuilder sb=new StringBuilder();
		String idraiz="";
		HabitacionParcialBean parcial;
		JSONObject resultObj=new JSONObject();
		resultObj.put("resultado","ok");
		JSONObject habitaciones=clasificarHabitaciones(listaPlazas);
		Iterator<String> idh=habitaciones.keys();
		String idhStr="";
		boolean resultado=true;
		while (idh.hasNext())
		{
			idhStr=idh.next();
			JSONArray plazas = SQLHabitaciones.selectHabitacionesParciales(idhStr);
			/*Hay que contrastar:
			 * 1-Que las plazas recibidas coinciden en cantidad con las que realmente tiene esa habitación
			 * 2-Que ninguna de ellas está en un estado distinto de "Desactivado"
			 * */
			if (comprobar(habitaciones.getJSONArray(idhStr),plazas))
			{
				resultado=SQLHabitaciones.eliminarHabitacionesParciales(idhStr);
				if (!resultado)
				{
					resultObj.put("resultado", "error");
					sb.append("No se pudieron eliminar las plazas parciales de "+idhStr+"\n");
				}
				else
				{
					sb.append("Eliminadas las plazas parciales de "+idhStr+"\n");					
				}
				resultado=SQLHabitaciones.estadoHabitaciones(idhStr, "0");
				if (!resultado)
				{
					resultObj.put("resultado", "error");
					sb.append("No se pudo recuperar el estado de "+idhStr+"\n");
				}
				else
				{
					sb.append(idhStr+" recuperada como habitación grupal\n");
				}

			}
			else
			{
				sb.append("Sólo se pueden recombinar plazas individuales si todas están desactivadas y si están todas seleccionadas.");
				resultObj.put("resultado", "error");
			}
		}
		
		if (!resultado) 
		{
			resultObj.put("resultado","error");
			sb.append("Error al cambiar el tipo de plaza (individuales a grupal)");
		}
		

		resultObj.put("mensaje", sb.toString());
		response.getWriter().print(resultObj.toString());

	}

}
