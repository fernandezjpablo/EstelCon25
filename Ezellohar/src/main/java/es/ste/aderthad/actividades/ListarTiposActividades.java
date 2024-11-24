package es.ste.aderthad.actividades;

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

import es.ste.aderthad.sql.SQLActividades;
@WebServlet("/admin/actividades/ListarTiposActividades")
public class ListarTiposActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListarTiposActividades() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray tipos=SQLActividades.listarTiposActividades();
		
		response.getWriter().println(tipos.toString());
	}


}
