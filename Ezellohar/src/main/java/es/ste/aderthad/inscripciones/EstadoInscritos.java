package es.ste.aderthad.inscripciones;

import java.io.IOException;

import org.json.JSONArray;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HabilitarPlazas
 */
@WebServlet("/EstadoInscritos")
public class EstadoInscritos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EstadoInscritos() {
        // TODO Auto-generated constructor stub
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String listaInscritos=request.getParameter("inscritos");
		String estado=request.getParameter("estado");

		
		boolean resultado=true;
		if (estado.equals("pendiente"))
		{
			//Anulamos pagos y dejamos en estado pendiente
			String habitaciones = SQLInscritos.getHabitaciones(listaInscritos);
			SQLPagos.anularPagos(listaInscritos,8);
			if (!habitaciones.equals(""))
			{	
				SQLHabitaciones.revisarEstadoHabitaciones(habitaciones);
				SQLHabitaciones.revisarEstadoHabitacionesParciales(habitaciones);
			}
			SQLInscritos.estadoInscritos(listaInscritos, "0");
		}
			else if (estado.equals("eliminar"))
			{
				//Anulamos pagos y estado baja
				SQLPagos.anularPagos(listaInscritos,8);
				SQLInscritos.sacarHabitacion(listaInscritos);
				SQLInscritos.estadoInscritos(listaInscritos, "9");
				SQLInscritos.limpiarNif(listaInscritos);
			} else if (estado.equals("apartar"))
			{
				//Sacamos de la habitación pero mantenemos los pagos
				SQLInscritos.sacarHabitacion(listaInscritos);
			}	
			else 		if (estado.equals("recuperar"))
			{
				//Dejamos en estado pendiente y los pagos como estuvieran
				SQLInscritos.recuperarBajas(listaInscritos, "0");
			}
			


		Logger.registrarActividadSession(request, "Modificanco estado de inscritos: "+listaInscritos+" para "+estado+".");
		if (resultado)
		{
			response.getWriter().println("{\"resultado\":\"ok\"}");
		}
		else
		{
			response.getWriter().println("{\"resultado\":\"error\"}");
		}			
	}

}
