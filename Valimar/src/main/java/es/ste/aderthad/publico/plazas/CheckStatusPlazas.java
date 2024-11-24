package es.ste.aderthad.publico.plazas;

import java.io.IOException;

import org.json.JSONObject;

import es.ste.aderthad.publico.properties.EntornoPublic;
import es.ste.aderthad.publico.sql.SQLHabitacionesPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckStatusPlazas
 */
@WebServlet("/CheckStatusPlazas")
public class CheckStatusPlazas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CheckStatusPlazas() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb=new StringBuilder();
		JSONObject respuesta=new JSONObject();
		//SQLHabitacionesPublic.LimpiarBloqueos();
		//SQLHabitacionesPublic.LimpiarBloqueosParciales();
		sb.append("Plazas Totales:"+SQLHabitacionesPublic.checkPlazasTotales()+"<br>");
		sb.append("Plazas Totales Disponibles:"+SQLHabitacionesPublic.checkPlazasTotalesDisponibles()+"<br>");
		sb.append("Plazas disponibles para inscripción de habitaciones:"+SQLHabitacionesPublic.checkPlazasEstado(1)+"<br>");
		sb.append("Plazas disponibles para inscripción individual en habitación compartida:"+SQLHabitacionesPublic.checkPlazasIndividualesEstado(1)+"<br>");
		sb.append("Plazas bloqueadas temporalmente (proceso de inscripción de habitaciones):"+SQLHabitacionesPublic.checkPlazasEstado(2)+"<br>");
		sb.append("Plazas bloqueadas temporalmente (proceso de inscripción individual en habitación compartida):"+SQLHabitacionesPublic.checkPlazasIndividualesEstado(2)+"<br>");
		sb.append("Plazas reservadas (pagadas y pendientes de pago):"+SQLHabitacionesPublic.checkPlazasReservadasTotales()+"<br>");
//		sb.append("Reservas cerradas/pagadas: "+SQLHabitacionesPublic.checkPlazasPagadas()+"<br>");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		respuesta.put("html", sb.toString());
		respuesta.put("listaEspera", EntornoPublic.getVariable("LISTA_ESPERA").toUpperCase().startsWith("ACTIVA"));
		response.getWriter().println(respuesta.toString());
	}


}
