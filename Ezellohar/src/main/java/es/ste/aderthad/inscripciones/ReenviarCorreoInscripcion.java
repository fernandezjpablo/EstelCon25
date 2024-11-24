package es.ste.aderthad.inscripciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.procesos.Procesos;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import es.ste.aderthad.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class ReenviarCorreoInscripcion
 */
@WebServlet("/ReenviarCorreoInscripcion")
public class ReenviarCorreoInscripcion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ReenviarCorreoInscripcion() {
        // TODO Auto-generated constructor stub
    }

    private static String generarMensajeRecuperacion(String idsusuario)
	{
		JSONObject resultado=new JSONObject();
		boolean respuesta=true;
		resultado.put("mensajes", new JSONArray());
		resultado.put("id_mensaje", new JSONArray());
		String[] inscritosArr=idsusuario.split(",");
		resultado.put("respuesta", "ok");
		resultado.put("mensaje", "Mensajes generados.");
		for (int j=0;j<inscritosArr.length;j++)
		{
			String idusuario=inscritosArr[j];
			InscritoBean inscrito=SQLInscritos.selectIdInscrito(idusuario);
		
		StringBuilder cuerpo=new StringBuilder();

			respuesta=respuesta && Procesos.generarMensajeArchivo(Procesos.componerMensajeInscrito(inscrito));

		}
		if (!respuesta)
		{
			resultado.put("respuesta", "error");
			resultado.put("mensaje", "No se pudieron generar uno o varios de los mensajes solicitados.");
		}
		return resultado.toString();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idsusuario=request.getParameter("inscritos");
		response.setContentType("text/plain");
		response.getWriter().println(generarMensajeRecuperacion(idsusuario));
		Logger.registrarActividadSession(request, "Solicitud de recuperación de correo de inscripción desde admin");
	}

}
