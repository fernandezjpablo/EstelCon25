package es.ste.aderthad.pagos;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONArray;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLPagos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InsertPlazas
 */
@WebServlet("/admin/pagos/ListPagosUsuario")
public class ListPagosUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListPagosUsuario() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idinscrito=request.getParameter("idinscrito");
		if (idinscrito==null) idinscrito="";
		JSONArray listado=new JSONArray();
		if (!idinscrito.equals(""))
		{
			listado=SQLPagos.selectPagosUsuario(idinscrito);
			Logger.registrarActividadSession(request, "Obteniendo pagos de "+idinscrito+".");
		}

			response.getWriter().println(listado.toString());

	}



}
