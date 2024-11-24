package es.ste.aderthad.plazas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AsignarNombres
 */
@WebServlet("/admin/plazas/AsignarNombres")
public class AsignarNombres extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public AsignarNombres() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String datos=request.getParameter("datos");
		JSONObject distribucion=new JSONObject(datos);
		JSONObject objetoDistribucion;
		JSONArray correspondencias=new JSONArray();
		JSONObject temp;
		String id;
		String nombre;
		String planta;
		Iterator<String> clave = distribucion.keys();
		while (clave.hasNext())
		{
			id=(String) clave.next();
			objetoDistribucion=distribucion.getJSONObject(id);
			nombre=objetoDistribucion.getString("ubicacion");
			planta=objetoDistribucion.getString("planta");
			temp=new JSONObject();
			temp.put("id", id);
			temp.put("nombre", nombre);
			temp.put("planta", planta);
			correspondencias.put(temp);
		}
		try {
			SQLHabitaciones.CambiarNombres(correspondencias);
			response.getWriter().println("Nombres de habitaciones actualizados");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().println("Error al actualizar los nombres: "+e.getLocalizedMessage());
		}
	}

}
