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

@WebServlet("/admin/actividades/EstadoActividades")
public class EstadoActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EstadoActividades() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject resultado=new JSONObject();
		resultado.put("resultado", "ok");
		
		String accion=request.getParameter("accion");
		String ids=request.getParameter("ids");
		
		boolean respuesta;
		if ("desactivar".equals(accion))
		{
			respuesta=SQLActividades.desactivarActividades(ids);	
		}
		else if ("aceptar".equals(accion))
		{
			respuesta=SQLActividades.aceptarActividades(ids);	
		}
		else
		{
			respuesta=SQLActividades.activarActividades(ids);	
		}
		if (!respuesta) resultado.put("resultado", "error");
		Logger.registrarActividadSession(request, "Actualizando estado actividades a "+accion+"("+ids+").");
		response.getWriter().println(resultado.toString());
	}

}
