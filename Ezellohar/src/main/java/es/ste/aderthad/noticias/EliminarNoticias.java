package es.ste.aderthad.noticias;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLNoticias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EliminarNoticias
 */
@WebServlet("/admin/noticias/EliminarNoticias")
public class EliminarNoticias extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EliminarNoticias() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String listaNoticias=request.getParameter("noticias");
		boolean resultado=SQLNoticias.eliminarNoticias(listaNoticias);
		Logger.registrarActividadSession(request, "Eliminando noticias "+listaNoticias+".");
		if (resultado)
		{
			response.getWriter().println("{\"resultado\":\"ok\"}");
		}
		else
		{
			response.getWriter().println("{\"resultado\":\"error\"}");
		}			
	}
}
