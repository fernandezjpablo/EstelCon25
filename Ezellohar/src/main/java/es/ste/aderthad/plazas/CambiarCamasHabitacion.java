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
 * Servlet implementation class CambiarCamasHabitacion
 */
@WebServlet("/admin/plazas/CambiarCamasHabitacion")
public class CambiarCamasHabitacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CambiarCamasHabitacion() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		String camas=request.getParameter("camas");
		String resultado="ok";
		try
		{
			SQLHabitaciones.CambiarCamas(id, camas);
			Logger.registrarActividadSession(request, "Modificar número de camas: ("+camas+") "+id+".");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
