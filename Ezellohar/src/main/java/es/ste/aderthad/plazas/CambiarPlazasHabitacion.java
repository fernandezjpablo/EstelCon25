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
 * Servlet implementation class CambiarPlazasHabitacion
 */
@WebServlet("/admin/plazas/CambiarPlazasHabitacion")
public class CambiarPlazasHabitacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CambiarPlazasHabitacion() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		String plazas=request.getParameter("plazas");
		String resultado="ok";
		try
		{
			SQLHabitaciones.CambiarPlazas(id, plazas);
			SQLHabitaciones.revisarEstadoHabitaciones(id);
			Logger.registrarActividadSession(request, "Cambiar plazas de habitación "+id+" a "+plazas+".");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
