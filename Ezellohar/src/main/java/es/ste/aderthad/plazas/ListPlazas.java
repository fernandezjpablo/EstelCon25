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
 * Servlet implementation class InsertPlazas
 */
@WebServlet("/admin/plazas/ListPlazas")
public class ListPlazas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListPlazas() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orden=request.getParameter("orden");
		JSONArray listado;
		if (!orden.equals(""))
		{
			orden=URLDecoder.decode(orden,Charset.defaultCharset());
			listado=SQLHabitaciones.selectHabitaciones(orden);
		}
		else
		{
			listado=SQLHabitaciones.selectHabitacionesDistribuidas();
		}

			response.getWriter().println(listado.toString());

	}



}
