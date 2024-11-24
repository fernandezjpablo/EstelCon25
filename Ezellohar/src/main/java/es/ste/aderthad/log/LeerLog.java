package es.ste.aderthad.log;

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
import es.ste.aderthad.mensajeria.ServiciosMensajeria;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLNoticias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LeerLog
 */
@WebServlet("/LeerLog")
public class LeerLog extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LeerLog() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ruta=request.getParameter("ruta");
		response.setContentType("text/plain");
		response.getWriter().println(Logger.leerLog(ruta));
	}

}
