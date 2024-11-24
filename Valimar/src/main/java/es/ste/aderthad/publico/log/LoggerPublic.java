package es.ste.aderthad.publico.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.json.JSONObject;


import es.ste.aderthad.publico.properties.EntornoPublic;

public class LoggerPublic {

	public static String getFileNameErrorLog()
	{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String nombre=EntornoPublic.getVariable("ERRORLOG_PATH")+"/"+df.format(System.currentTimeMillis())+".log";
		return nombre;
	}
	
	public static String getFileNameActivityLog()
	{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String nombre=EntornoPublic.getVariable("ACTLOG_PATH")+"/"+df.format(System.currentTimeMillis())+".actlog";
		return nombre;
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
}
