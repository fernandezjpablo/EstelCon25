package es.ste.aderthad.plazas;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONArray;

import es.ste.aderthad.sql.SQLHabitaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ListPlazasParciales
 */
@WebServlet("/admin/plazas/ListPlazasParcialesDisponibles")
public class ListPlazasParcialesDisponibles extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListPlazasParcialesDisponibles() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado;

			listado=SQLHabitaciones.selectHabitacionesParciales();
			response.getWriter().println(listado.toString());
	}


}
