package es.ste.aderthad.actividades;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

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
import es.ste.aderthad.sql.SQLInscripcionesActividades;

/**
 * Servlet implementation class EstadoInscritos
 */
@WebServlet("/admin/actividades/CambiarEstadoInscritosActividad")
public class EstadoInscritos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EstadoInscritos() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accion=request.getParameter("accion");
		String elementos=request.getParameter("ids");
		String idinscrito;
		String idactividad;
		boolean result=true;
		JSONObject respuesta=new JSONObject();
		respuesta.put("resultado", "ok");
		
		elementos=elementos.replace("]","");
		elementos=elementos.replace("[","");
		elementos=elementos.replace("\"","");
			if (accion.equals("eliminar"))
			{
				result=SQLInscripcionesActividades.deleteInscripcionActividadMultiple(elementos);
			} else if (accion.equals("espera"))
			{
				result=SQLInscripcionesActividades.updateInscripcionActividadMultiple(elementos, 9);
			} else if (accion.equals("inscribir"))
			{
				result=SQLInscripcionesActividades.updateInscripcionActividadMultiple(elementos, 0);
			} 
			if (!result)
			{
				respuesta.put("resultado", "error");
			}
			Logger.registrarActividadSession(request, "Actualizando estado inscritos "+elementos+"("+accion+").");
			response.getWriter().println(respuesta.toString());
		}


}
