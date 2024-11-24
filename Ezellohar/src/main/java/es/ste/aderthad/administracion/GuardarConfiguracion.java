package es.ste.aderthad.administracion;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.mensajeria.ServiciosMensajeria;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetConfiguracion
 */
@WebServlet("/admin/sistema/GuardarConfiguracion")
public class GuardarConfiguracion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GuardarConfiguracion() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String datos=request.getParameter("data");
		String resultado="ok";
		try
		{
			resultado=Entorno.guardarEntorno(datos);
			if (resultado.equals("")) resultado="ok";
			Logger.registrarActividadSession(request, "Actualizando configuración.");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
