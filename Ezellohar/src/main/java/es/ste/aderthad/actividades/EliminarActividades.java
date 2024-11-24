package es.ste.aderthad.actividades;

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

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;

/**
 * Servlet implementation class EliminarActividades
 */
@WebServlet("/admin/actividades/EliminarActividades")
public class EliminarActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EliminarActividades() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject resultado=new JSONObject();
		resultado.put("resultado", "ok");
		String ids=request.getParameter("ids");
		
		boolean respuesta=SQLActividades.eliminarActividades(ids);
		Logger.registrarActividadSession(request, "Eliminando actividades "+ids);
		if (!respuesta) resultado.put("resultado", "error");
		response.getWriter().println(resultado.toString());
	}

}
