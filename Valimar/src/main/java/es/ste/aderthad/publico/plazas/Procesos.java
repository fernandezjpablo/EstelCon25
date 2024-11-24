package es.ste.aderthad.publico.plazas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import es.ste.aderthad.publico.log.LoggerPublic;
import es.ste.aderthad.publico.data.InscritoBean;
import es.ste.aderthad.publico.data.MensajeBean;
import es.ste.aderthad.publico.data.UsuarioBean;
import es.ste.aderthad.publico.properties.EntornoPublic;
import es.ste.aderthad.publico.sql.SQLUsuariosPublic;


public class Procesos {

	public static boolean ProcesoPostInscripcion(InscritoBean bean)
	{
		boolean resultado=true;
		/*
		 * Con un nuevo inscrito hay que hacer tres procesos:
		 * Email de bienvenida
		 * Email de notificación de inscripci&oacute;n
		 * Generaci&oacute;n de usuario y contrase&ntilde;a para el inscrito
		 * Si es un inscrito en lista de espera no se le envían datos de acceso
		 * */
		UsuarioBean usuario=generarUsuario(bean);
		resultado=resultado && SQLUsuariosPublic.insertUsuario(usuario);
if (bean.getHabitacion().toLowerCase().contains("lista de espera"))
{
	resultado=resultado && generarMensajeArchivo(componerMensajeListaEspera(bean));
	resultado=resultado && generarMensajeArchivo(componerMensajeNotificacionEspera(bean));	
}
else
{
		resultado=resultado && generarMensajeArchivo(componerMensajeInscrito(bean));
		resultado=resultado && generarMensajeArchivo(componerMensajeNotificacion(bean));
		resultado=resultado && generarMensajeArchivo(componerMensajeAcceso(bean));
}
		return resultado;
	}
	
	private static String limpiarTildes(String cadena)
	{
		String resultado=cadena;
		resultado=resultado.replaceAll("`", "");
		resultado=resultado.replaceAll("'", "");
		resultado=resultado.replaceAll(" ", "");
		resultado=resultado.replaceAll("ñ", "N");
		resultado=resultado.replaceAll("Ó", "O");
		resultado=resultado.replaceAll("Í", "I");
		resultado=resultado.replaceAll("Ú", "U");
		resultado=resultado.replaceAll("É", "E");
		resultado=resultado.replaceAll("Á", "A");
		return resultado;
	}
	private static UsuarioBean generarUsuario(InscritoBean bean)
	{
		UsuarioBean user=new UsuarioBean();
		
		String id=UUID.randomUUID().toString();
		String nom=limpiarTildes(bean.getNombre().toUpperCase()+"000").substring(0,3)+limpiarTildes(bean.getApellido().toUpperCase()+"000").substring(0,3);
		String total=String.valueOf(SQLUsuariosPublic.totalUsuarios()+1);
		user.setUsuario("EC"+nom+total);
		user.setId(id);
		user.setPassword(id.split("-")[0]);
		user.setIdInscrito(bean.getId());
		return user;
	}
	
	private static String getPlantilla(String idPlantilla)
	{
		StringBuilder cuerpoPlantilla=new StringBuilder();
		if (!idPlantilla.equals("none"))
		{
		String ruta=EntornoPublic.getVariable("EMAIL_PLANTILLAS")+idPlantilla+".template";
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
				LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
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
	
	private static String parsear(String plantilla,String mensaje,InscritoBean inscrito)
	{
		String resultado=plantilla;
		
		
		resultado=resultado.replaceAll("%MENSAJE%",mensaje);
		if (inscrito!=null)
		{
			UsuarioBean usuario=SQLUsuariosPublic.selectInscrito(inscrito.getId());
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

	public static String generarCuerpo(String plantilla, String cuerpo,InscritoBean inscrito) {
		String cuerpoMensaje=URLDecoder.decode(cuerpo,Charset.forName("UTF-8")).replaceAll("%2B","+");
		String cuerpoPlantilla=getPlantilla(plantilla);
		cuerpoMensaje=parsear(cuerpoPlantilla,cuerpoMensaje,inscrito);
		return cuerpoMensaje;
	}
	
	private static MensajeBean componerMensajeInscrito(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Inscripción a la Mereth Aderthad recibida.");
		msg.setBlindcopyto(bean.getEmail());
		msg.setTo("");
		msg.setCopyto("");
		msg.setFrom(EntornoPublic.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo("inscritos","",bean));
		return msg;
	}
	
	
	private static MensajeBean componerMensajeListaEspera(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Inscripción a la Lista de Espera de la Mereth Aderthad recibida.");
		msg.setBlindcopyto(bean.getEmail());
		msg.setTo("");
		msg.setCopyto("");
		msg.setFrom(EntornoPublic.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo("espera","",bean));
		return msg;
	}
	
	private static MensajeBean componerMensajeAcceso(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Mereth Aderthad: Datos de acceso a la zona de inscritos..");
		msg.setBlindcopyto(bean.getEmail());
		msg.setReplyTo(EntornoPublic.getVariable("SMTP_REPLYTO"));
		msg.setTo("");
		msg.setCopyto("");
		msg.setFrom(EntornoPublic.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo("acceso","",bean));
		return msg;
	}
	
	private static MensajeBean componerMensajeNotificacion(InscritoBean bean)
	{
		//Notificación a la organización
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Nueva inscripción recibida: "+bean.getNombre()+" "+bean.getApellido());
	
		msg.setBlindcopyto("");
		msg.setReplyTo(EntornoPublic.getVariable("SMTP_REPLYTO"));
		msg.setTo(EntornoPublic.getVariable("SMTP_FROM"));
		msg.setCopyto("");
		msg.setFrom(bean.getEmail());
		msg.setBody(generarCuerpo("notificacion","",bean));
		return msg;
	}
	private static MensajeBean componerMensajeNotificacionEspera(InscritoBean bean)
	{
		//Notificación a la organización
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Nueva inscripción en lista de espera recibida: "+bean.getNombre()+" "+bean.getApellido());
	
		msg.setBlindcopyto("");
		msg.setReplyTo(EntornoPublic.getVariable("SMTP_REPLYTO"));
		msg.setTo(EntornoPublic.getVariable("SMTP_FROM"));
		msg.setCopyto("");
		msg.setFrom(bean.getEmail());
		msg.setBody(generarCuerpo("notificacion","",bean));
		return msg;
	}	
	private static boolean generarMensajeArchivo(MensajeBean mensaje)
	{
		boolean resultado=true;
		String newline = System.getProperty("line.separator");

		String idBase=UUID.randomUUID().toString();
		
		StringBuilder sb=new StringBuilder();
		String rutaOutbox=EntornoPublic.getVariable("EMAIL_OUTBOX");
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
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
			resultado=false;
		}
		
		return resultado;
	}
}
