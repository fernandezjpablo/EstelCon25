package es.ste.aderthad.espacios;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONArray;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLEspacios;


/**
 * Servlet implementation class ListarEspacios
 */
@WebServlet("/admin/espacios/ListarEspacios")
public class ListarEspacios extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListarEspacios() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado;
			listado=SQLEspacios.listarEspacios();
			Logger.registrarActividadSession(request, "Listando espacios.");
			response.getWriter().println(listado.toString());

	}


}
