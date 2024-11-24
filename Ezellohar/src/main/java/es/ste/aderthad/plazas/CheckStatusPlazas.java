package es.ste.aderthad.plazas;

import java.io.IOException;

import es.ste.aderthad.sql.SQLHabitaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckStatusPlazas
 */
@WebServlet("/admin/plazas/CheckStatusPlazas")
public class CheckStatusPlazas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CheckStatusPlazas() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb=new StringBuilder();
		//SQLHabitaciones.LimpiarBloqueos();
		sb.append("Plazas Totales:"+SQLHabitaciones.checkPlazasTotales()+"<br>");
		sb.append("Plazas disponibles para inscripción:"+SQLHabitaciones.checkPlazasEstado(1)+"<br>");
		sb.append("Plazas bloqueadas temporalmente (en proceso de inscripción):"+SQLHabitaciones.checkPlazasEstado(2)+"<br>");
		sb.append("Plazas disponibles para inscripción individual aleatoria:"+SQLHabitaciones.checkPlazasEstado(9)+"<br>");
		sb.append("Plazas preinscritas (pendiente de pago):"+SQLHabitaciones.checkPlazasEstado(3)+"<br>");
		sb.append("Plazas cerradas/pagadas: "+SQLHabitaciones.checkPlazasEstado(4)+"<br>");
		response.setContentType("text/html");
		response.setCharacterEncoding("ISO-8859-15");
		response.getWriter().println(sb.toString());
	}


}
