package es.ste.aderthad.mensajeria;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLInscritos;

public class GenerarArchivoMensaje {

	public static MensajeBean generarBeanArchivo(String path)
	{
		MensajeBean mensaje=new MensajeBean();
		File archivo=new File(path);
		boolean inicioBody=false;
		if (archivo.exists() && archivo.isFile())
		{
			BasicFileAttributes attr;
			try {
				attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
				mensaje.setFecha(attr.creationTime().to(TimeUnit.MILLISECONDS));
				mensaje.setFechaUpdate(attr.lastModifiedTime().to(TimeUnit.MILLISECONDS));
			} catch (IOException e1) {
				Logger.GenerarEntradaLogError(e1, Logger.getFileNameErrorLog());
				mensaje.setFecha(-1);
				mensaje.setFechaUpdate(-1); 
			}

			try {
				BufferedReader br=Files.newBufferedReader(Paths.get(path));
				String linea;
				
				StringBuilder body=new StringBuilder();
				while ((linea=br.readLine())!=null)
				{
					if (!inicioBody)
					{
					if (linea.startsWith("TO:"))
					{
						if (linea.split(":").length>1)	mensaje.setTo(linea.split(":")[1]);
					}
					if (linea.startsWith("CC:"))
					{
						if (linea.split(":").length>1) mensaje.setCopyto(linea.split(":")[1]);
					}
					if (linea.startsWith("BCC:"))
					{
						if (linea.split(":").length>1) mensaje.setBlindcopyto(linea.split(":")[1]);
					}
					if (linea.startsWith("FROM:"))
					{
						if (linea.split(":").length>1) mensaje.setFrom(linea.split(":")[1]);
					}
					if (linea.startsWith("SUBJECT:"))
					{
						if (linea.split(":").length>1) mensaje.setSubject(linea.split(":")[1]);
					}
					if (linea.startsWith("BODY:"))
					{
						inicioBody=true;
					}
					}
					else
					{
						body.append(linea);
					}
				}
				mensaje.setBody(body.toString());
				br.close();
			} catch (IOException e) {
				mensaje=null;
			}
		}
		else
		{
			mensaje=null;
		}
		if (!inicioBody) mensaje=null;
		return mensaje;
	}
	
	public static String generarMensajeArchivoMasivo(MensajeBean mensaje)
	{
		JSONArray emails=SQLInscritos.selectEmailsActivos();
		JSONObject resultado=new JSONObject();
		/*Para hacer una comunicación masiva troceamos los destinatario en bloques de 50 y se envían en BCC*/
		int bloques=Math.round(emails.length() / 50)+1;
		String[] destinatarios=new String[bloques];
		int nbloque=0;
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
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
		resultado.put("ruta",rutaOutbox+idBase+".eml");
		File archivo_mensaje=new File(rutaOutbox+idBase+".eml");
		sb.append("FROM:"+mensaje.getFrom()).append(newline);
		sb.append("TO:").append(newline);
		sb.append("CC:").append(newline);
		
		sb.append("BCC:"+destinatarios[b]).append(newline);
		
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
		} catch (IOException ex) {
			resultado.put("respuesta", "error");
			resultado.put("mensaje", ex.getLocalizedMessage());
		}
		}
		return resultado.toString();
	}
	
	
	public static String generarMensajeArchivoMasivoDeuda(MensajeBean mensaje)
	{
		JSONArray emails=SQLInscritos.selectEmailsActivosDeuda();
		JSONObject resultado=new JSONObject();
		/*Para hacer una comunicación masiva troceamos los destinatario en bloques de 50 y se envían en BCC*/
		int bloques=Math.round(emails.length() / 50)+1;
		String[] destinatarios=new String[bloques];
		int nbloque=0;
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
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
		resultado.put("ruta",rutaOutbox+idBase+".eml");
		File archivo_mensaje=new File(rutaOutbox+idBase+".eml");
		sb.append("FROM:"+mensaje.getFrom()).append(newline);
		sb.append("TO:").append(newline);
		sb.append("CC:").append(newline);
		
		sb.append("BCC:"+destinatarios[b]).append(newline);
		
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
		} catch (IOException ex) {
			resultado.put("respuesta", "error");
			resultado.put("mensaje", ex.getLocalizedMessage());
		}
		}
		return resultado.toString();
	}
	public static String generarMensajeArchivo(MensajeBean mensaje)
	{
		JSONObject resultado=new JSONObject();
		String newline = System.getProperty("line.separator");
		resultado.put("respuesta","ok");
		resultado.put("mensaje", "");
		resultado.put("mensajes","");
		String idBase=UUID.randomUUID().toString();
		resultado.put("id_mensaje", idBase);
		
		StringBuilder sb=new StringBuilder();
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
		resultado.put("ruta",rutaOutbox+idBase+".eml");
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
			resultado.put("respuesta", "error");
			resultado.put("mensaje", e.getLocalizedMessage());
		}
		
		return resultado.toString();
	}
}
