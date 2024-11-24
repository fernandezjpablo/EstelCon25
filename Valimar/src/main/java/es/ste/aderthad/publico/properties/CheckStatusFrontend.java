package es.ste.aderthad.publico.properties;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.json.JSONObject;

import es.ste.aderthad.publico.log.LoggerPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckStatusFrontend
 */
@WebServlet("/CheckStatusFrontend")
public class CheckStatusFrontend extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CheckStatusFrontend() {

    }

    private long fechaCountdown()
    {
    	long resultado=0;
    	String fecha=EntornoPublic.getVariable("FECHA_CUENTA_ATRAS");
    	ZonedDateTime tiempo;
		tiempo=LocalDateTime.parse(fecha,DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).atZone(ZoneId.of("Europe/Madrid"));
		long time=tiempo.toInstant().toEpochMilli();
		
		long diffInMillies = Math.abs(time - System.currentTimeMillis());
		resultado=diffInMillies/1000;
    	
    	return resultado;
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject respuesta=new JSONObject();
		respuesta.put("inscripciones", EntornoPublic.getVariable("INSCRIPCIONES_ACTIVAS").toUpperCase().equals("SI"));
		respuesta.put("inscritos",  EntornoPublic.getVariable("ZONA_INSCRITOS_VISIBLE").toUpperCase().equals("SI"));
		respuesta.put("invitados",  EntornoPublic.getVariable("INVITADOS_VISIBLES").toUpperCase().equals("SI"));
		respuesta.put("punto_morado",  EntornoPublic.getVariable("PUNTO_MORADO_VISIBLE").toUpperCase().equals("SI"));
		respuesta.put("formulario",  EntornoPublic.getVariable("FORMULARIO").toUpperCase().equals("ACTIVO"));
		respuesta.put("cuenta", fechaCountdown());
		response.setContentType("application/json");
		response.getWriter().println(respuesta.toString());
	}

}
