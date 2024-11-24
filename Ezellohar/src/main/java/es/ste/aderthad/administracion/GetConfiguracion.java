package es.ste.aderthad.administracion;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetConfiguracion
 */
@WebServlet("/admin/sistema/GetConfiguracion")
public class GetConfiguracion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetConfiguracion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=ISO-8859-15");
		response.getWriter().print(Entorno.getEntorno());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
