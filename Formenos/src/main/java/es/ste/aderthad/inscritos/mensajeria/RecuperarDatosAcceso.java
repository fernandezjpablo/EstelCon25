package es.ste.aderthad.inscritos.mensajeria;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.data.PlanificacionBean;
import es.ste.aderthad.inscritos.data.UsuarioBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLActividades;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLInscritos;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecuperarDatosAcceso
 */
@WebServlet("/RecuperarDatosAcceso")
public class RecuperarDatosAcceso extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RecuperarDatosAcceso() {
        // TODO Auto-generated constructor stub
    }

    private static String generarMensajeRecuperacion(String email)
	{
		JSONObject resultado=new JSONObject();
		StringBuilder cuerpo=new StringBuilder();
		JSONArray recuperaciones=SQLUsuarios.selectDatosAcceso(email);
		if (recuperaciones.length()==0)
		{		
			resultado.put("respuesta","error");
			resultado.put("mensaje", "No se encontraron usuarios registrados con este email");
		}
		else
		{
			JSONObject item;
			cuerpo.append("Datos de acceso asociados a esta dirección de correo:<br>");
			for (int i=0;i<recuperaciones.length();i++)
			{
				item=recuperaciones.getJSONObject(i);
				cuerpo.append(item.getString("nombre")+" "+item.getString("apellidos")+"<br>");
				cuerpo.append("Usuario/Contraseña: "+item.getString("usuario")+"/"+item.getString("password")+"<br>");
			}
		String asunto="Recuperación de datos de acceso a la EstelCon 2024";


		String from=EntornoInscritos.getVariable("SMTP_FROM");		
			
	



			String newline = System.getProperty("line.separator");
		resultado.put("respuesta","ok");
		resultado.put("mensaje", "Correo generado, recibirás los datos de acceso en los próximos minutos.");
		String idBase=UUID.randomUUID().toString();
		resultado.put("id_mensaje", idBase);
		
		StringBuilder sb=new StringBuilder();
		String rutaOutbox=EntornoInscritos.getVariable("EMAIL_OUTBOX");
		resultado.put("ruta",rutaOutbox+idBase+".eml");
		File archivo_mensaje=new File(rutaOutbox+idBase+".eml");
		sb.append("FROM:"+from);
		sb.append(newline);
		sb.append("TO:").append(newline);
		sb.append("CC:").append(newline);
		
		sb.append("BCC:"+email).append(newline);
		
		sb.append("SUBJECT:"+asunto).append(newline);
		sb.append("BODY:").append(newline);
		sb.append(cuerpo).append(newline);
		try {
			if (!archivo_mensaje.exists()) {
				archivo_mensaje.createNewFile();
            }
			BufferedWriter bw = Files.newBufferedWriter(Paths.get(archivo_mensaje.getAbsolutePath()));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException ex) {
			LoggerInscritos.GenerarEntradaLogError(ex, LoggerInscritos.getFileNameErrorLog());
			resultado.put("respuesta", "excepcion");
			resultado.put("mensaje", ex.getLocalizedMessage());
		}

		}
		return resultado.toString();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email=request.getParameter("email");
		response.setContentType("application/json");
		response.getWriter().println(generarMensajeRecuperacion(email));
		LoggerInscritos.registrarActividadSession(request, "Solicitud de recuperación de datos de acceso");
	}

}
