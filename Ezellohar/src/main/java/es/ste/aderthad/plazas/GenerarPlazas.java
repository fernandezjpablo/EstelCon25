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
 * Servlet implementation class GenerarPlazas
 */
@WebServlet("/admin/plazas/GenerarPlazas")
public class GenerarPlazas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GenerarPlazas() {
        // TODO Auto-generated constructor stub
    }



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String habitaciones=request.getParameter("habitaciones");
		String capacidad=request.getParameter("capacidad");
		String planta=request.getParameter("planta");
		String camas=request.getParameter("camas");
		String precioAdultos=request.getParameter("precioAdultos");
		String precioMenores=request.getParameter("precioMenores");
		boolean resultado=SQLHabitaciones.generarHabitaciones(Integer.valueOf(habitaciones), planta,Integer.valueOf(capacidad),Integer.valueOf(camas),Double.valueOf(precioAdultos),Double.valueOf(precioMenores));
		if (resultado)
			{
			Logger.registrarActividadSession(request, "Generando "+habitaciones+" habitaciones de "+capacidad+" plazas "+ camas +"camas. Precios: ("+precioAdultos+"/"+precioMenores+")");
			response.getWriter().println("Habitaciones generadas correctamente.");
			}
		else
		{
		response.getWriter().println("No se han podido generar las habitaciones.");
		}
	}

}
