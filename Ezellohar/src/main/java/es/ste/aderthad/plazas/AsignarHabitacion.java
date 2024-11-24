package es.ste.aderthad.plazas;

import java.io.IOException;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AsignarHabitacion
 */
@WebServlet("/admin/plazas/AsignarHabitacion")
public class AsignarHabitacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public AsignarHabitacion() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("idinscrito");
		String habitacion=request.getParameter("idhabitacion");
		String tipo=request.getParameter("tipo");
		String resultado="ok";
		SQLInscritos.asignarHabitacion(id, habitacion);		
		Logger.registrarActividadSession(request, "Asignando habitación "+habitacion+" a "+id+".");
		InscritoBean inscrito=SQLInscritos.select(id);
		if (tipo.equals("1"))
		{
			//Es una plaza de una habitación parcial
			int estadoInscrito=inscrito.getEstado();
			if (estadoInscrito!=4) estadoInscrito=3;
			SQLHabitaciones.revisarEstadoHabitacionesParciales(habitacion);
			Logger.registrarActividadSession(request, "Revisando estado  "+habitacion+" parcial.");
		}
		else
		{
			//Es  una plaza en una habitación grupal
			//Asignamos y revisamos el estado por si el nuevo inscrito tiene pagos pendientes
			SQLHabitaciones.revisarEstadoHabitaciones(habitacion);
			Logger.registrarActividadSession(request, "Revisando habitación "+habitacion+" grupal.");
		}
		

		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}


}
