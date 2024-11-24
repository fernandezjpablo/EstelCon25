package es.ste.aderthad.espacios;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import es.ste.aderthad.sql.SQLEspacios;
/**
 * Servlet implementation class ConsultarEspacio
 */
@WebServlet("/admin/espacios/ConsultarEspacio")
public class ConsultarEspacio extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ConsultarEspacio() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject listado=SQLEspacios.consultarEspacio(request.getParameter("id"));
			response.getWriter().println(listado.toString());

	}

}
