package es.ste.aderthad.plazas;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONArray;

import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class VerOcupantes
 */
@WebServlet("/admin/plazas/VerOcupantes")
public class VerOcupantes extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public VerOcupantes() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idh=request.getParameter("idHabitacion");
		JSONArray ocupantes = SQLInscritos.ocupantesHabitacion(idh);
		StringBuilder sb=new StringBuilder();
		response.setContentType("text/plain;UTF-8");
		for (int i=0;i<ocupantes.length();i++)
		{
			sb.append(ocupantes.getString(i)+"<br>");
		}
		response.getWriter().println(sb.toString());
	}


}
