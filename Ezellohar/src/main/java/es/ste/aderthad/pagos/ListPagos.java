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
@WebServlet("/admin/pagos/ListPagos")
public class ListPagos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListPagos() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orden=request.getParameter("filtro");
		if (orden==null) orden="";
		JSONArray listado;
		if (!orden.equals(""))
		{
			orden=URLDecoder.decode(orden,Charset.defaultCharset());
			listado=SQLPagos.selectPagos(orden);
			Logger.registrarActividadSession(request, "Obteniendo pagos de los usuarios.");
		}
		else
		{
			listado=SQLPagos.selectPagos();
		}

			response.getWriter().println(listado.toString());

	}



}
