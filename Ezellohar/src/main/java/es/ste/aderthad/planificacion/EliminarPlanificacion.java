package es.ste.aderthad.planificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.PlanificacionBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
/**
 * Servlet implementation class EliminarPlanificacion
 */
@WebServlet("/admin/planificacion/EliminarPlanificacion")
public class EliminarPlanificacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EliminarPlanificacion() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean liberar=true;
		boolean limpiar=true;
		JSONObject resultado=new JSONObject();
		resultado.put("resultado", "ok");
		resultado.put("mensaje", "ok");
		liberar=SQLPlanificacion.liberarPlanificacion();
		liberar = liberar && SQLActividades.desplanificarTodasActividades();
		Logger.registrarActividadSession(request, "Desplanificando todas las actividades.");
		if (!liberar) {
			resultado.put("resultado", "error");
			resultado.put("mensaje", "Error al liberar la planificación");
		}
		limpiar=SQLPlanificacion.limpiarPlanificacion();
		if (!limpiar) {
			resultado.put("resultado", "error");
			resultado.put("mensaje", "Error al limpiar la planificación");
		}
		
		
		response.getWriter().println(resultado.toString());
		
	}

}
