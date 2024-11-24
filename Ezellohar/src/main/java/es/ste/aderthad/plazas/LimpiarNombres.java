package es.ste.aderthad.plazas;

import java.io.IOException;
import java.sql.SQLException;

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
 * Servlet implementation class LimpiarNombres
 */
@WebServlet("/admin/plazas/LimpiarNombres")
public class LimpiarNombres extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LimpiarNombres() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SQLHabitaciones.LimpiarNombres();
			response.getWriter().println("Nombres de habitaciones reiniciados");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			response.getWriter().println("Error al quitar los nombres: "+e.getLocalizedMessage());
		}
	}

}
