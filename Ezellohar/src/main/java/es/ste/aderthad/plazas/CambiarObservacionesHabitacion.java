package es.ste.aderthad.plazas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CambiarObservacionesHabitacion
 */
@WebServlet("/admin/plazas/CambiarObservacionesHabitacion")
public class CambiarObservacionesHabitacion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CambiarObservacionesHabitacion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is=request.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String linea;
		StringBuilder sb=new StringBuilder();
		while ((linea=br.readLine())!=null)
		{
			sb.append(linea);
		}
		JSONObject objeto=new JSONObject(sb.toString());
		String id=objeto.getString("id");
		String observaciones=objeto.getString("contenido");
		String resultado="ok";
		try
		{
			SQLHabitaciones.CambiarObservaciones(id, observaciones);
			Logger.registrarActividadSession(request, "Cambiar observaciones de habitación "+id+".");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
