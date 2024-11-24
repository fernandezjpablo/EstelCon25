package es.ste.aderthad.procesos;

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

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.data.PagosBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLUsuarios;



public class Procesos {


	
	private static String limpiarTildes(String cadena)
	{
		String resultado=cadena;
		resultado=resultado.replaceAll("º", "");
		resultado=resultado.replaceAll("ª", "");
		resultado=resultado.replaceAll(" ", "");
		resultado=resultado.replaceAll("Ñ", "N");
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
		String nom=limpiarTildes(bean.getNombre().toUpperCase()).substring(0,3)+limpiarTildes(bean.getApellido().toUpperCase()).substring(0,3);
		String total=String.valueOf(SQLUsuarios.totalUsuarios()+1);
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

	private static String parsear(String plantilla,String mensaje,InscritoBean inscrito, PagosBean pago)
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
			if (pago!=null)
			{
				resultado=resultado.replaceAll("%IMPORTE%",String.valueOf(pago.getImporte()));
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
	
	
	public static String generarCuerpo(String plantilla, String cuerpo,InscritoBean inscrito,PagosBean pago) {
		String cuerpoMensaje=URLDecoder.decode(cuerpo,Charset.forName("UTF-8")).replaceAll("%2B","+");
		String cuerpoPlantilla=getPlantilla(plantilla);
		cuerpoMensaje=parsear(cuerpoPlantilla,cuerpoMensaje,inscrito,pago);
		return cuerpoMensaje;
	}
	
	public static MensajeBean componerMensajeInscrito(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("inscripción a la Mereth Aderthad recibida.");
		msg.setBlindcopyto(bean.getEmail());
		msg.setTo("");
		msg.setCopyto("");
		msg.setFrom(Entorno.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo("inscritos","",bean));
		return msg;
	}
	
    public static MensajeBean componerMensajeIngreso(PagosBean pago)
	{
    	MensajeBean msg=new MensajeBean();
    	InscritoBean bean=SQLInscritos.selectIdInscrito(pago.getIdInscrito());
		String from=Entorno.getVariable("SMTP_FROM");		
		String copiaIngreso=Entorno.getVariable("COPIA_MOVIMIENTOS");	
		if (!"".equals(copiaIngreso))
		{
			copiaIngreso=";"+copiaIngreso;
		}
		msg.setBlindcopyto(bean.getEmail()+copiaIngreso);
    	msg.setFrom(from);
		msg.setTo("");
		msg.setCopyto("");
    	msg.setSubject("XXVIII Mereth Aderthad - Justificante de Ingreso recibido");
    	msg.setBody(generarCuerpo("ingreso","",bean,pago));
		return msg;
	}
    
    
    public static MensajeBean componerMensajeDevolucion(PagosBean pago)
	{
    	MensajeBean msg=new MensajeBean();
    	InscritoBean bean=SQLInscritos.selectIdInscrito(pago.getIdInscrito());
		String from=Entorno.getVariable("SMTP_FROM");		
		String copiaIngreso=Entorno.getVariable("COPIA_MOVIMIENTOS");	
		if (!"".equals(copiaIngreso))
		{
			copiaIngreso=";"+copiaIngreso;
		}
		msg.setBlindcopyto(bean.getEmail()+copiaIngreso);
    	msg.setFrom(from);
		msg.setTo("");
		msg.setCopyto("");
    	msg.setSubject("XXVIII Mereth Aderthad - Justificante de devolución emitida");
    	msg.setBody(generarCuerpo("devolucion","",bean,pago));
		return msg;
	}
	
	private static MensajeBean componerMensajeListaEspera(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("inscripción a la Lista de Espera de la Mereth Aderthad recibida.");
		msg.setBlindcopyto(bean.getEmail());
		msg.setTo("");
		msg.setCopyto("");
		msg.setFrom(Entorno.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo("espera","",bean));
		return msg;
	}
	
	private static MensajeBean componerMensajeAcceso(InscritoBean bean)
	{
//Notificación al inscrit@
		
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Mereth Aderthad: Datos de acceso a la zona de inscritos..");
		msg.setBlindcopyto(bean.getEmail());
		msg.setReplyTo(Entorno.getVariable("SMTP_REPLYTO"));
		msg.setTo("");
		msg.setCopyto("");
		msg.setFrom(Entorno.getVariable("SMTP_FROM"));
		msg.setBody(generarCuerpo("acceso","",bean));
		return msg;
	}
	
	private static MensajeBean componerMensajeNotificacion(InscritoBean bean)
	{
		//Notificación a la organización
		MensajeBean msg=new MensajeBean();
		msg.setSubject("Nueva inscripción recibida: "+bean.getNombre()+" "+bean.getApellido());
	
		msg.setBlindcopyto("");
		msg.setReplyTo(Entorno.getVariable("SMTP_REPLYTO"));
		msg.setTo(Entorno.getVariable("SMTP_FROM"));
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
		msg.setReplyTo(Entorno.getVariable("SMTP_REPLYTO"));
		msg.setTo(Entorno.getVariable("SMTP_FROM"));
		msg.setCopyto("");
		msg.setFrom(bean.getEmail());
		msg.setBody(generarCuerpo("notificacion","",bean));
		return msg;
	}	
	public static boolean generarMensajeArchivo(MensajeBean mensaje)
	{
		boolean resultado=true;
		String newline = System.getProperty("line.separator");

		String idBase=UUID.randomUUID().toString();
		
		StringBuilder sb=new StringBuilder();
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
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
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			resultado=false;
		}
		
		return resultado;
	}
}
