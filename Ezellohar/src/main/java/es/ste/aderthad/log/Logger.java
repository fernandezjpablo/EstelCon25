package es.ste.aderthad.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.mensajeria.GenerarArchivoMensaje;
import es.ste.aderthad.properties.Entorno;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Logger {

	public static String getFileNameErrorLog()
	{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String nombre=Entorno.getVariable("ERRORLOG_PATH")+df.format(System.currentTimeMillis())+".log";
		return nombre;
	}
	
	public static String getFileNameActivityLog()
	{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String nombre=Entorno.getVariable("ACTLOG_PATH")+df.format(System.currentTimeMillis())+".actlog";
		return nombre;
	}
	
	public static void registrarActividadSession(HttpServletRequest request,String entrada)
	{	
		String entradaCompleta="Usuario: "+request.getRemoteUser()+";"+entrada;
		GenerarEntradaLogMensaje(entradaCompleta,getFileNameActivityLog());
	}
	
	public static void GenerarEntradaLogError(Exception e,String archivo)
	{
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String newline = System.getProperty("line.separator");
		File archivo_log=new File(archivo);
		StringBuilder sb=new StringBuilder();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		sb.append(df.format(System.currentTimeMillis()));
		sb.append(newline);
		sb.append(sw.toString());
		try {
			if (!archivo_log.exists()) {
				archivo_log.createNewFile();
            }
			String ruta=archivo_log.getAbsolutePath();
			FileWriter fw = new FileWriter(ruta, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
			out.println(sb.toString());
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	
	
	public static void GenerarEntradaLogMensaje(String mensaje,String archivo)
	{
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String newline = System.getProperty("line.separator");
		File archivo_log=new File(archivo);
		StringBuilder sb=new StringBuilder();
				sb.append(df.format(System.currentTimeMillis()));
		sb.append(newline);
		sb.append(mensaje);
		try {
			if (!archivo_log.exists()) {
				archivo_log.createNewFile();
            }
			String ruta=archivo_log.getAbsolutePath();
			FileWriter fw = new FileWriter(ruta, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
			out.println(sb.toString());
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static JSONArray obtenerLogs(String ruta,String tipoArchivo)
	{
		JSONArray resultado=new JSONArray();
		JSONObject objeto;
		try (Stream<Path> walk = Files.walk(Paths.get(ruta))) {

			List<String> result = walk.map(x -> x.toString())
					.filter(f -> f.endsWith("."+tipoArchivo)).collect(Collectors.toList());
			int max=100;
			if (result.size() <max) max=result.size();
			for (int i=0;i<max;i++)
			{
				File archivo=new File (result.get(i));
				objeto=new JSONObject();
				objeto.put("archivo", archivo.getName());
				objeto.put("ruta", archivo.getAbsolutePath());
				resultado.put(objeto);
			}
		} catch (IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}		
		return resultado;
	}

	
	private static String cargarLog(String path)
	{
		StringBuilder contenido=new StringBuilder();
		try {
			BufferedReader br=new BufferedReader(new FileReader(path));
			String linea;
			
	
			while ((linea=br.readLine())!=null)
			{
				contenido.append(linea);
			}

		}
		catch(Exception e)
		{
			contenido.append("<<<<<<No se pudo cargar el contenido del archivo de log>>>>>>");
		}
		return contenido.toString();
	}
	public static String leerLog(String ruta) {
		String contenidoLog="";

		File archivo=new File(ruta);
		if (archivo.exists() && archivo.isFile())
		{
			contenidoLog=cargarLog(ruta);

		}
		else
		{
			contenidoLog="El log no existe en la ruta especificada.";
		}

		return contenidoLog;
	}

}
