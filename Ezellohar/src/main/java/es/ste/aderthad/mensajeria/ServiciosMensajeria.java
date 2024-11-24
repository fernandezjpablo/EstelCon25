package es.ste.aderthad.mensajeria;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.mail.smtp.SMTPTransport;

import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLUsuarios;

public class ServiciosMensajeria {
	

	private static boolean sendMessage(MensajeBean mensaje)
	{
		boolean exito=true;
		if (mensaje!=null)
		{
		Properties prop=System.getProperties();
		prop.put("mail.smtp.host", Entorno.getVariable("SMTP_SERVER"));
		prop.put("mail.smtp.auth","true");
		prop.put("mail.smtp.port", Entorno.getVariable("SMTP_PORT"));
		prop.put("mail.smtp.starttls.enable", Entorno.getVariable("SMTP_ENABLE_TLS"));
		Session session=Session.getInstance(prop,null);
		MimeMessage msg = new MimeMessage(session);

		Multipart multipart=new MimeMultipart("related");
		BodyPart htmlPart=new MimeBodyPart();
		String[] fragmentos = procesarFragmentos(mensaje.getBody());
        try {
    		msg.setHeader("Content-Type","text/html;Charset=UTF_8");		
			// from
           if ((mensaje.getFrom()!=null) && (!mensaje.getFrom().equals("")))  msg.setFrom(new InternetAddress(mensaje.getFrom()));

			// to 
           if (mensaje.getTo()!=null) msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mensaje.getTo().replaceAll(";", ","), false));

			// cc
           if (mensaje.getCopyto()!=null) msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(mensaje.getCopyto().replaceAll(";", ","), false));

			// bcc
           if (mensaje.getBlindcopyto()!=null) msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(mensaje.getBlindcopyto().replaceAll(";", ","), false));
           
			// subject
           if (mensaje.getSubject()!=null) 
        	   msg.setSubject(mensaje.getSubject(),"UTF-8");
           else
        	   msg.setSubject("(Sin asunto)");
			
			// content 
           if (fragmentos.length==1)
           {
        	   msg.setContent(mensaje.getBody(),"text/html;Charset=UTF-8");
           }
           else
           {
        	   htmlPart.setContent(fragmentos[0],"text/html");
        	   multipart.addBodyPart(htmlPart);
        	   if (fragmentos.length>1)
        	   {
        		   for (int f=1;f<fragmentos.length;f++)
        		   {
        			   PreencodedMimeBodyPart img=new PreencodedMimeBodyPart("base64");
        			   img.setHeader("Content-ID","<"+fragmentos[f].split(":")[0]+">");
        			   img.setDisposition(MimeBodyPart.INLINE);
        			   img.setContent(fragmentos[f].split(":")[2], fragmentos[f].split(":")[1]);
        			   multipart.addBodyPart(img);
        		   }
        	   }
        	   msg.setContent(multipart);
        	   
           }
            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            t.connect(Entorno.getVariable("SMTP_SERVER"), Entorno.getVariable("SMTP_USER"), Entorno.getVariable("SMTP_PASSWD"));
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            t.close();

            
            

        } catch (MessagingException e) {
        	Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
            exito=false;
        }
		}
		else
		{
			exito=false;
		}
        return exito;
	}
	
	public static String formatearMensaje(String ruta)
	{
		boolean exito=true;
		StringBuilder sb=new StringBuilder();
		MensajeBean mensaje=GenerarArchivoMensaje.generarBeanArchivo(ruta);
		if (mensaje!=null)
		{
			sb.append("<b>Desde:</b>"+mensaje.getFrom()+"<br>");
			sb.append("<b>Para:</b>"+mensaje.getTo()+"<br>");
			sb.append("<b>Copia:</b>"+mensaje.getCopyto()+"<br>");
			sb.append("<b>Copia Oculta:</b>"+mensaje.getBlindcopyto()+"<br>");
			sb.append("<b>Asunto:</b>"+mensaje.getSubject()+"<br>");
			sb.append(mensaje.getBody());
		}
		else
		{
			sb.append("El mensaje está vacío");
		}
        return sb.toString();
	}
	
	private static boolean moverArchivo(String origen,String destino)
	{
		boolean resultado=true;
		try {
			Path temp=Files.move(Paths.get(origen),Paths.get(destino));
			if (temp==null) resultado=false;
		} catch (IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			resultado=false;
		}
		return resultado;
	}
	
	private static boolean eliminarMensaje(String archivo)
	{
		boolean resultado=true;
		try {
			Files.delete(Paths.get(archivo));
		} catch (IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=false;
		}
		return resultado;
	}
	
	public static boolean recuperarMensajes(String mensajes)
	{
		boolean resultado=true;
		String[] archivos=mensajes.split(",");
		String outbox=Entorno.getVariable("EMAIL_OUTBOX");
		String enviados=Entorno.getVariable("EMAIL_SENT");
		String error=Entorno.getVariable("EMAIL_ERROR");
		String destino;
		
		for (int i=0;i<archivos.length;i++)
		{
			if (archivos[i].indexOf(enviados)>=0)
			{
				destino=archivos[i].replace(enviados,outbox);
				resultado=resultado && moverArchivo(archivos[i],destino);
			}
			else if (archivos[i].indexOf(error)>=0)
			{
				destino=archivos[i].replace(error,outbox);
				resultado=resultado && moverArchivo(archivos[i],destino);
			}
		}
		return resultado;
	}
	
	public static boolean eliminarMensajes(String mensajes)
	{
		boolean resultado=true;
		String[] archivos=mensajes.split(",");
		String outbox=Entorno.getVariable("SMTP_OUTBOX");
		String enviados=Entorno.getVariable("SMTP_SENT"); //De momento no vamos a restringir eliminar de "Enviados" pero hay que planteárselo
		String error=Entorno.getVariable("SMTP_ERROR");
		String destino;
		
		for (int i=0;i<archivos.length;i++)
		{
			eliminarMensaje(archivos[i]);
		}
		return resultado;
	}
	
	private static JSONArray obtenerMensajes(String ruta)
	{
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONArray resultado=new JSONArray();
		JSONObject objeto;
		MensajeBean mensaje;
		String blindcopy;
		try (Stream<Path> walk = Files.walk(Paths.get(ruta))) {

			List<String> result = walk.map(x -> x.toString())
					.filter(f -> f.endsWith(".eml")).collect(Collectors.toList());
			int max=1000;
			//if (result.size() <max) max=result.size();
			max=result.size();
			for (int i=0;i<max;i++)
			{
				File archivo=new File (result.get(i));
				mensaje=GenerarArchivoMensaje.generarBeanArchivo(archivo.getAbsolutePath());
				blindcopy=mensaje.getBlindcopyto();
				if (blindcopy.length()>50)
				{
					blindcopy=blindcopy.substring(0,50)+"...";
				}
				objeto=new JSONObject();
				objeto.put("archivo", archivo.getName());
				objeto.put("ruta", archivo.getAbsolutePath());
				objeto.put("from", mensaje.getFrom());
				objeto.put("to", mensaje.getTo());
				objeto.put("cc", mensaje.getCopyto());
				objeto.put("bcc", blindcopy);
				objeto.put("asunto", mensaje.getSubject());
				objeto.put("fecha", df.format(new Date(mensaje.getFecha())));
				objeto.put("fechaUpdate", df.format(new Date(mensaje.getFechaUpdate())));
				resultado.put(objeto);
			}

		} catch (IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}		
		return resultado;
	}
	public static JSONObject listarMensajes()
	{
		JSONObject resultado=new JSONObject();
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
		String rutaSent=Entorno.getVariable("EMAIL_SENT");
		String rutaError=Entorno.getVariable("EMAIL_ERROR");
		resultado.put("enviados",obtenerMensajes(rutaSent));
		resultado.put("erroneos",obtenerMensajes(rutaError));
		resultado.put("pendientes",obtenerMensajes(rutaOutbox));
		return resultado;
	}
	
	public static String enviarMensajes()
	{
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
		String rutaSent=Entorno.getVariable("EMAIL_SENT");
		String rutaError=Entorno.getVariable("EMAIL_ERROR");
		boolean resultadoEnvio;
		JSONObject resultado=new JSONObject();
		resultado.put("resultado","ok");
		resultado.put("enviados", new JSONArray());
		resultado.put("erroneos", new JSONArray());
		resultado.put("movidos", new JSONArray());
		resultado.put("nomovidos", new JSONArray());
		try (Stream<Path> walk = Files.walk(Paths.get(rutaOutbox))) {

			List<String> result = walk.map(x -> x.toString())
					.filter(f -> f.endsWith(".eml")).collect(Collectors.toList());
			int max=100;
			if (result.size() <max) max=result.size();
			for (int i=0;i<max;i++)
			{
				File archivo=new File (result.get(i));
			resultadoEnvio=sendMessage(GenerarArchivoMensaje.generarBeanArchivo(archivo.getAbsolutePath()));
				if (resultadoEnvio)
				{
					resultado.getJSONArray("enviados").put(archivo.getName());
					if (moverArchivo(rutaOutbox+archivo.getName(),rutaSent+archivo.getName()))
					{
						resultado.getJSONArray("movidos").put(archivo.getName());	
					}
					else
					{
						resultado.getJSONArray("nomovidos").put(archivo.getName());
					}
				}
				else
				{
					resultado.getJSONArray("erroneos").put(archivo.getName());
					if (moverArchivo(rutaOutbox+archivo.getName(),rutaError+archivo.getName()))
					{
						resultado.getJSONArray("movidos").put(archivo.getName());	
					}
					else
					{
						resultado.getJSONArray("nomovidos").put(archivo.getName());
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultado.toString();
	}
	
	public static String getPlantilla(String idPlantilla)
	{
		StringBuilder cuerpoPlantilla=new StringBuilder();
		if (!idPlantilla.equals("none"))
		{
		String ruta=Entorno.getVariable("EMAIL_PLANTILLAS")+idPlantilla+".template";
		File archivo=new File(ruta);
		if (archivo.exists() && archivo.isFile())
		{
			try {
				BufferedReader br=new BufferedReader(new FileReader(archivo));
				String linea;
				
				StringBuilder body=new StringBuilder();
				while ((linea=br.readLine())!=null)
				{
					cuerpoPlantilla.append(linea);
				}
				br.close();
			} catch (IOException e) {
				Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
				cuerpoPlantilla.append("%MENSAJE%");
			}

		}
		else
		{
			cuerpoPlantilla.append("%MENSAJE%");
		}
		}
		else
		{
			cuerpoPlantilla.append("%MENSAJE%");
		}
		return cuerpoPlantilla.toString();
		
	}
	
	public static String leerMensaje(String ruta)
	{
		String cuerpoMensaje="";

		File archivo=new File(ruta);
		if (archivo.exists() && archivo.isFile())
		{
			cuerpoMensaje=formatearMensaje(ruta);

		}
		else
		{
			cuerpoMensaje="El mensaje no existe en la ruta especificada.";
		}

		return cuerpoMensaje;
		
	}
	

	
	private static String parsear(String plantilla,String mensaje,InscritoBean inscrito)
	{
		String resultado=plantilla;
		
		
		resultado=resultado.replaceAll("%MENSAJE%",mensaje);
		if (inscrito!=null)
		{
			UsuarioBean usuario=SQLUsuarios.selectInscrito(inscrito.getId());
			resultado=resultado.replaceAll("%NOMBRE%",inscrito.getNombre());
			resultado=resultado.replaceAll("%APELLIDOS%",inscrito.getApellido());
			resultado=resultado.replaceAll("%NIF%",inscrito.getNif());
			resultado=resultado.replaceAll("%TELEFONO%",inscrito.getTelefono());
			resultado=resultado.replaceAll("%IDINSCRITO%",inscrito.getId());
			resultado=resultado.replaceAll("%EMAIL%",inscrito.getEmail());
			resultado=resultado.replaceAll("%NICK%",inscrito.getPseudonimo());
			resultado=resultado.replaceAll("%TELEGRAM%",inscrito.getTelegram());
			resultado=resultado.replaceAll("%PRECIOPLAZA%",inscrito.calcularImporte());
			resultado=resultado.replaceAll("%TIPOHABITACION%",inscrito.obtenerTipoHabitacion());
			if (usuario!=null)
			{
				resultado=resultado.replaceAll("%USUARIO%",usuario.getUsuario());
				resultado=resultado.replaceAll("%PASSWORD%",usuario.getPassword());
			}
		}
		return resultado;
	}
	
	private static String[] procesarFragmentos(String correoOriginal)
	{
		String resultado=correoOriginal;
		String[] fragmentos=resultado.split("src=\"data:");
		String[] imagenes= new String[fragmentos.length];
		String cid;
		String tipo;
		String base64;
		int posicionCierre;
		if (fragmentos.length>1)
		{
			resultado=fragmentos[0];
			
			for (int i=1;i<fragmentos.length;i++)
			{	
				cid=UUID.randomUUID().toString();
				resultado+="src=\"cid:"+cid+"\">";
				posicionCierre=fragmentos[i].indexOf("\">");
				base64=fragmentos[i].substring(0,posicionCierre);
				tipo=base64.split(";")[0];
				base64=base64.split(",")[1];
				imagenes[i]=cid+":"+tipo+":"+base64;
				resultado+=fragmentos[i].substring(posicionCierre+2,fragmentos[i].length());
			}
		}

		imagenes[0]=resultado;
		return imagenes;
	}

	public static String generarCuerpo(String plantilla, String cuerpo,InscritoBean inscrito) {
		String cuerpoMensaje=cuerpo;
		cuerpoMensaje=cuerpoMensaje.replaceAll("%", "<porcentaje>");
		cuerpoMensaje=URLDecoder.decode(cuerpoMensaje,Charset.forName("UTF-8"));
		cuerpoMensaje=cuerpoMensaje.replaceAll("%2B","+");
		cuerpoMensaje=cuerpoMensaje.replaceAll("<porcentaje>", "%");
		String cuerpoPlantilla=getPlantilla(plantilla);
		cuerpoMensaje=parsear(cuerpoPlantilla,cuerpoMensaje,inscrito);
		return cuerpoMensaje;
	}

	public static String actualizarPlantilla(String tipo, String datos) {
		String respuesta="";
		String ruta=Entorno.getVariable("EMAIL_PLANTILLAS")+tipo+".template";
		File archivo=new File(ruta);
		if (archivo.exists() && archivo.isFile())
		{
			try {
				FileWriter fw = new FileWriter(archivo);
				fw.write(datos);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
				respuesta=e.getLocalizedMessage();
			}
		}
		else
		{
			respuesta="La plantilla no existe";
		}
	
		return respuesta;
	}
}
