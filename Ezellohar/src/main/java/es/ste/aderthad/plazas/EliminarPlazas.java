package es.ste.aderthad.plazas;

import java.io.IOException;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EliminarPlazas
 */
@WebServlet("/admin/plazas/EliminarPlazas")
public class EliminarPlazas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EliminarPlazas() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Sólo se pueden eliminar  habitaciones Bloqueadas, estado 8
		String listaPlazas=request.getParameter("plazas");
		boolean resultado=SQLHabitaciones.eliminarHabitaciones(listaPlazas);
		resultado=resultado && SQLInscritos.limpiarHabitaciones(listaPlazas);
		if (resultado)
		{
			Logger.registrarActividadSession(request, "Eliminando plazas "+listaPlazas+".");
			response.getWriter().println("{\"resultado\":\"ok\"}");
		}
		else
		{
			response.getWriter().println("{\"resultado\":\"error\"}");
		}			
	}

}
