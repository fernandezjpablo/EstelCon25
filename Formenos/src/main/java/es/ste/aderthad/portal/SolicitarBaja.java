package es.ste.aderthad.portal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.HabitacionBean;
import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.data.MensajeBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class GetPerfil
 */
@WebServlet("/SolicitarBaja")
public class SolicitarBaja extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SolicitarBaja() {
        // TODO Auto-generated constructor stub
    }
    
    private static boolean generarMensajeArchivo(MensajeBean mensaje)
	{
		boolean resultado=true;
		String newline = System.getProperty("line.separator");

		String idBase=UUID.randomUUID().toString();
		
		StringBuilder sb=new StringBuilder();
		String rutaOutbox=EntornoInscritos.getVariable("EMAIL_OUTBOX");
		File archivo_mensaje=new File(rutaOutbox+idBase+".eml");
		sb.append("FROM:"+mensaje.getFrom()).append(newline);
		sb.append("TO:"+mensaje.getTo()).append(newline);
		sb.append("CC:"+mensaje.getCopyto()).append(newline);
		sb.append("BCC:"+mensaje.getBlindcopyto()).append(newline);
		sb.append("SUBJECT:"+mensaje.getSubject()).append(newline);
		sb.append("BODY:").append(newline);
		sb.append(mensaje.getBody()).append(newline);
		try {
			if (!archivo_mensaje.exists()) {
				archivo_mensaje.createNewFile();
            }
			BufferedWriter bw = Files.newBufferedWriter(Paths.get(archivo_mensaje.getAbsolutePath()));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
			resultado=false;
		}
		
		return resultado;
	}
    
    
	public static String generarCuerpo(InscritoBean inscrito) {

		StringBuilder cuerpoMensaje=new StringBuilder();
		
		cuerpoMensaje.append("Solicitud de baja de inscripción de la Mereth Aderthad recibida:,<br>");
		cuerpoMensaje.append("Datos de la baja:,<br>");
		cuerpoMensaje.append("Nombre:"+inscrito.getNombre()+"<br>");
		cuerpoMensaje.append("Apellidos:"+inscrito.getApellido()+"<br>");
		cuerpoMensaje.append("Nif:"+inscrito.getNif()+"<br>");
		cuerpoMensaje.append("Id de la Habitación:"+inscrito.getHabitacion()+"<br>");
		cuerpoMensaje.append("Ésto es un correo de notificación de que hemos recibido la petición. No es una tramitación automática de la baja.<br>");
		cuerpoMensaje.append("Cuando ésta sea efectiva se enviará un nuevo correo indicándolo.<br>");
		cuerpoMensaje.append("Un saludo<br>La Organización de la EstelCon");
		return cuerpoMensaje.toString();
	}
    
	
    
	private static MensajeBean componerMensajeBaja(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Solicitud de Baja de Inscripción en la XXVIII Mereth Aderthad");
		msg.setBlindcopyto(bean.getEmail());
		msg.setTo(EntornoInscritos.getVariable("SMTP_FROM"));
		msg.setCopyto("");
		msg.setFrom(EntornoInscritos.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo(bean));
		return msg;
	}
	

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject resultado=new JSONObject();
		HttpSession sesion=request.getSession();
		String usuario=(String) sesion.getAttribute("usuario");
		String idsesion=(String) sesion.getAttribute("idSesion");
		boolean bloqueo_datos=EntornoInscritos.getVariable("BLOQUEAR_DATOS_INSCRITOS").toUpperCase().equals("SI");
    	Cookie loginCookie = null;
    	Cookie sesionCookie=null;
    	Cookie[] cookies = request.getCookies();
    	if(cookies != null){
    	for(Cookie cookie : cookies){
    		if(cookie.getName().equals("usuario")){
    			loginCookie = cookie;
    			usuario=loginCookie.getValue();
    		}
    		if(cookie.getName().equals("JSESSIONID")){
    			sesionCookie = cookie;
    			idsesion=sesionCookie.getValue();
    		}
    	}
    	}
		
		if (usuario==null || idsesion==null)
		{
			resultado.put("respuesta", "caducada");
		}
		else
		{

			InscritoBean inscrito=SQLUsuarios.selectUsuario(usuario);

			boolean respuesta=false;
					
			if (!bloqueo_datos) {
				//Generar mensaje de baja
				respuesta=generarMensajeArchivo(componerMensajeBaja(inscrito));
			}
			if (respuesta)
				{
				resultado.put("respuesta", "ok");
				LoggerInscritos.registrarActividadSession(request, "Solicitud de baja generada");
				}
			else
			{
				if (bloqueo_datos)
				{
					resultado.put("respuesta", "error: la organización ha bloqueado la modificación de perfiles");
					LoggerInscritos.registrarActividadSession(request, "Error al actualizar el perfil");
				}
				else
					{
					resultado.put("respuesta", "error");
					LoggerInscritos.registrarActividadSession(request, "Error al actualizar el perfil");
					}
				
			}
		}
    	response.getWriter().println(resultado.toString());
	}

}
