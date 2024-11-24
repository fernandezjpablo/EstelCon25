package es.ste.aderthad.inscripciones;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLCheckin;
import es.ste.aderthad.sql.SQLInscritos;

/**
 * Servlet implementation class ListInscritos
 */
@WebServlet("/ListInscritos")
public class ListInscritos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListInscritos() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject inscritos=new JSONObject();
		inscritos.put("alojados",SQLInscritos.selectInscritos().toString() );
		inscritos.put("desalojados",SQLInscritos.selectInscritosSinHabitacion().toString() );
		inscritos.put("espera",SQLInscritos.selectInscritosListaEspera().toString() );
		inscritos.put("checkin", SQLCheckin.listCheckin().toString());
		response.getWriter().append(inscritos.toString());
		Logger.registrarActividadSession(request, "Obteniendo listado de inscritos.");
	}


}
