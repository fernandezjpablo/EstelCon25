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
 * Servlet implementation class CambiarPrecioAdultoHabitacion
 */
@WebServlet("/admin/plazas/CambiarPrecioAdultoHabitacion")
public class CambiarPrecioAdultoHabitacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CambiarPrecioAdultoHabitacion() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		String precio=request.getParameter("precioA");
		String resultado="ok";
		try
		{
			SQLHabitaciones.CambiarPrecioAdulto(id, precio);
			Logger.registrarActividadSession(request, "Cambiar precio adulto de habitación "+id+" a "+precio+".");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
