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
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GenerarMensajeSaliente
 */
@WebServlet("/GenerarMensajeSaliente")
public class GenerarMensajeSaliente extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GenerarMensajeSaliente() {
        // TODO Auto-generated constructor stub
    }
    
    private static String getContactoUsuario(String usuario)
    {
    	String destinatarios="";
    	InscritoBean bean=SQLUsuarios.selectInscritoFull(usuario);
    	return bean.getNombre()+" "+bean.getApellido()+" ("+bean.getPseudonimo()+") "+bean.getEmail();
    }
    
    private static String getEmail(String usuario)
    {
    	String destinatarios="";
    	InscritoBean bean=SQLUsuarios.selectInscritoFull(usuario);
    	return bean.getEmail();
    }
    
    private static JSONArray getDestinatarios(JSONArray destinatarios)
    {
    	JSONArray destinos=new JSONArray();
    	InscritoBean bean;
    	for (int i=0;i<destinatarios.length();i++)
    	{
        	bean=SQLUsuarios.selectInscritoFull(destinatarios.getString(i));
        	destinos.put(bean.getEmail());
    	}
    	return destinos;
    }
    
    private static JSONArray getDestinatariosUsuario(JSONArray destinatarios)
    {
    	JSONArray destinos=new JSONArray();
    	for (int i=0;i<destinatarios.length();i++)
    	{
        	destinos.put(destinatarios.getJSONObject(i).getString("email"));
    	}
    	return destinos;
    }

	public static String generarMensajeArchivoMasivo(String tipoEnvio,String idactividad,String cuerpo,String usrFrom)
	{
		String asunto="";
		JSONObject actividad=SQLActividades.consultarActividad(idactividad);

		JSONArray emails=null;
		String from=getEmail(usrFrom);		
		String datosFrom=getContactoUsuario(usrFrom);
		String nombreActividad="";
		if (actividad.has("nombreActividad")) nombreActividad=actividad.getString("nombreActividad");
		if (tipoEnvio.equals("responsables"))
		{
			asunto="Consulta sobre la actividad "+nombreActividad;
			JSONArray destinatarios=new JSONArray();
			destinatarios.put(actividad.getString("responsables"));
			emails=getDestinatarios(destinatarios);
			
		}
		if (tipoEnvio.equals("inscritos"))
		{
			asunto="Comunicado sobre la actividad "+nombreActividad;
			emails=getDestinatariosUsuario(SQLActividades.listarInscritos(idactividad));
		}	
		else 		if (tipoEnvio.equals("espera"))
		{
			asunto="Comunicado sobre la actividad "+nombreActividad;
			emails=getDestinatariosUsuario(SQLActividades.listarInscritosEspera(idactividad));
		}	else 
			if (tipoEnvio.equals("ambos"))
			{
				asunto="Comunicado sobre la actividad "+nombreActividad;
				emails=getDestinatariosUsuario(SQLActividades.listarInscritosTotales(idactividad));
			}	

		JSONObject resultado=new JSONObject();
		
		/*Para hacer una comunicación masiva troceamos los destinatario en bloques de 50 y se envían en BCC*/
		int bloques=Math.round(emails.length() / 50)+1;
		String[] destinatarios=new String[bloques];
		int nbloque=0;
		if (emails.length()>0)
		{
		destinatarios[0]=emails.getString(0);
		for (int e=1;e<emails.length();e++)
		{
			if (e % 50 ==0)
			{
				nbloque++;
				destinatarios[nbloque]=emails.getString(e);
			}
			else
			{
				destinatarios[nbloque]+=","+emails.getString(e);
			}
		}
		for (int b=0;b<destinatarios.length;b++)
		{
			String newline = System.getProperty("line.separator");
		resultado.put("respuesta","ok");
		resultado.put("mensaje", "");
		resultado.put("mensajes","");
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
		
		sb.append("BCC:"+destinatarios[b]).append(newline);
		
		sb.append("SUBJECT:"+asunto).append(newline);
		sb.append("BODY:").append(newline);
		sb.append("Enviado por: "+datosFrom).append(newline);
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
			resultado.put("respuesta", "error");
			resultado.put("mensaje", ex.getLocalizedMessage());
		}
		}
		}
		else
		{
			//No hay destinatarios
			resultado.put("respuesta", "error");
			resultado.put("mensaje", "No hay usuarios a quienes enviar mensajes");
		}
		return resultado.toString();
	}
	
	
	protected void doPost(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tipoEnvio=request.getParameter("tipoEnvio");
		String idActividad=request.getParameter("idactividad");
		String cuerpoMensaje=request.getParameter("data");
		String from=request.getParameter("from");
		String resultado=generarMensajeArchivoMasivo(tipoEnvio,idActividad,cuerpoMensaje,from);
		LoggerInscritos.registrarActividadSession(request, "Mensaje masivo enviado: "+tipoEnvio + " "+idActividad);
		response.getWriter().println(resultado);
		/*
		 * tipoEnvio=responsables; tipoEnvio=inscritos
		 * */
		
		
	}

}
