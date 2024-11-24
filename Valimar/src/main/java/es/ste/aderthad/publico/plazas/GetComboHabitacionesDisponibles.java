package es.ste.aderthad.publico.plazas;

import java.io.IOException;

import org.json.JSONArray;

import es.ste.aderthad.publico.sql.SQLHabitacionesPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetComboHabitacionesDisponibles
 */
@WebServlet("/GetComboHabitacionesDisponibles")
public class GetComboHabitacionesDisponibles extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetComboHabitacionesDisponibles() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray lista=SQLHabitacionesPublic.selectComboHabitaciones();
		StringBuilder sb=new StringBuilder();
		
		if (lista.length()==0)
		{
			sb.append("En este momento no hay habitaciones disponibles");
		}
		else
		{
			sb.append("<SELECT id='comboHabitaciones'>");
			sb.append("<OPTION value=-1>(Seleccione la habitación para la inscripción)</OPTION>");
			for (int i=0;i<lista.length();i++)
			{
				sb.append("<OPTION value="+lista.getJSONObject(i).getInt("capacidad")+","+lista.getJSONObject(i).getInt("camas")+">"+lista.getJSONObject(i).getInt("capacidad")+" plazas con "+lista.getJSONObject(i).getInt("camas")+" camas (Habitaciones Disponibles: "+lista.getJSONObject(i).getInt("habitaciones")+")</OPTION>");
			}
			sb.append("</SELECT>");
			sb.append("<INPUT id='btnPaso2' TYPE=\"BUTTON\" value='Seleccionar' onClick='lanzarPaso2();'>");
		}
		response.getWriter().println(sb.toString());
	}


}
