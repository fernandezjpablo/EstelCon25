package es.ste.aderthad.plazas;

import java.io.IOException;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BloquearHabitacion
 */
@WebServlet("/admin/plazas/BloquearHabitacion")
public class BloquearHabitacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public BloquearHabitacion() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String capacidad=request.getParameter("capacidad");
		String camas=request.getParameter("camas");
		
		String idbloqueado=SQLHabitaciones.BloqueoHabitacion(capacidad,camas);
		//Logger.registrarActividadSession(request, "Bloqueando habitación "+idbloqueado+".");
		response.getWriter().println(idbloqueado);
		
	}

}
