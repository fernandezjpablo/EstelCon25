package es.ste.aderthad.mensajeria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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

import es.ste.aderthad.sql.SQLNoticias;

/**
 * Servlet implementation class GuardarPlantilla
 */
@WebServlet("/admin/mensajeria/GuardarPlantilla")
public class GuardarPlantilla extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GuardarPlantilla() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tipo=request.getParameter("tipo");
		//String datos=URLDecoder.decode(request.getParameter("data"),Charset.forName("UTF-8"));
		String datos=request.getParameter("data").replaceAll("%2B","+");
		String resultado="ok";
		try
		{
			resultado=ServiciosMensajeria.actualizarPlantilla(tipo,datos);
			Logger.registrarActividadSession(request, "Actualizando plantilla "+tipo+".");
			if (resultado.equals("")) resultado="ok";
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
