package es.ste.aderthad.mensajeria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.json.JSONObject;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLNoticias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class AccionesMensajes
 */
@WebServlet("/admin/mensajeria/AccionesMensajes")
public class AccionesMensajes extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public AccionesMensajes() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accion=request.getParameter("accion");
		String mensajes=request.getParameter("mensajes");
		JSONObject resultado=new JSONObject();
		resultado.put("resultado", "ok");
		if (accion.equals("recuperar"))
		{
			if (!ServiciosMensajeria.recuperarMensajes(mensajes))
			{
				resultado.put("resultado", "Error al recuperar mensajes");
			}
			Logger.registrarActividadSession(request, "Ejecutando "+accion+" en los mensajes "+mensajes+".");
		}
		else
		{
			if (accion.equals("eliminar"))
			{
				if (!ServiciosMensajeria.eliminarMensajes(mensajes))
				{
					resultado.put("resultado", "Error al eliminar mensajes");	
				}
				Logger.registrarActividadSession(request, "Ejecutando "+accion+" en los mensajes "+mensajes+".");
			}
			else
			{
				if (accion.equals("lanzar"))
				{
					ServiciosMensajeria.enviarMensajes();
				}
				Logger.registrarActividadSession(request, "Ejecutando "+accion+" en los mensajes.");
			}
		}
		
		response.getWriter().println(resultado.toString());
	}

}
