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
 * Servlet implementation class HabilitarPlazas
 */
@WebServlet("/admin/plazas/EstadoPlazas")
public class EstadoPlazas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EstadoPlazas() {
        // TODO Auto-generated constructor stub
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String listaPlazas=request.getParameter("plazas");
		String estado=request.getParameter("estado");
		
		boolean resultado=true;
		if (estado.equals("1"))
		{
			resultado=resultado && SQLHabitaciones.habilitarHabitaciones(listaPlazas);
		}
		else if (estado.equals("0"))
		{
			resultado=resultado && SQLHabitaciones.deshabilitarHabitaciones(listaPlazas);
		}
		else
		{
			resultado=resultado && SQLHabitaciones.estadoHabitaciones(listaPlazas,estado);
		}
		if (resultado)
		{
			Logger.registrarActividadSession(request, "Modificar estado de las plazas "+listaPlazas+" a "+estado+".");
			response.getWriter().println("{\"resultado\":\"ok\"}");
		}
		else
		{
			response.getWriter().println("{\"resultado\":\"error\"}");
		}			
	}

}
