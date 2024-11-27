package es.ste.aderthad.inscritos.log;

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


import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoggerInscritos {

	public static String getFileNameErrorLog()
	{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String nombre=EntornoInscritos.getVariable("ERRORLOG_PATH")+"/"+df.format(System.currentTimeMillis())+".log";
		return nombre;
	}
	
	public static String getFileNameActivityLog()
	{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String nombre=EntornoInscritos.getVariable("ACTLOG_PATH")+"/"+df.format(System.currentTimeMillis())+".userlog";
		return nombre;
	}
	
	public static void registrarActividadSession(HttpServletRequest request,String entrada)
	{	
		
		HttpSession sesion=request.getSession(true);
		String usuario=(String) sesion.getAttribute("usuario");
		String idsesion=(String) sesion.getAttribute("idSesion");
		String entradaCompleta="Usuario: "+usuario+"/"+idsesion+";"+entrada;
		GenerarEntradaLogMensaje(entradaCompleta,getFileNameActivityLog());
	}
	public static void GenerarEntradaLogError(Exception e, String archivo) {
	    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	    String newline = System.getProperty("line.separator");
	    File archivo_log = new File(archivo);
	    StringBuilder sb = new StringBuilder();
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    e.printStackTrace(pw);
	    sb.append(df.format(System.currentTimeMillis()));
	    sb.append(newline);
	    sb.append(sw.toString());
	    sb.append(newline);
	    String ruta = archivo_log.getAbsolutePath();
        System.out.println("Ruta del archivo: " + ruta);
        System.out.println("Contenido que se va a escribir: ");
        System.out.println(sb.toString());
	    try {
	        if (!archivo_log.exists()) {
	            archivo_log.createNewFile();
	        }
	        
	        // Imprimir el absolutePath y el contenido a escribir
	         ruta = archivo_log.getAbsolutePath();
	        System.out.println("Ruta del archivo: " + ruta);
	        System.out.println("Contenido que se va a escribir: ");
	        System.out.println(sb.toString());
	        
	        // Escribir el contenido en el archivo
	        FileWriter fw = new FileWriter(ruta, true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        PrintWriter out = new PrintWriter(bw);
	        out.println(sb.toString());
	        bw.close();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	}

	public static void GenerarEntradaLogMensaje(String mensaje, String archivo) {
	    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	    String newline = System.getProperty("line.separator");
	    File archivo_log = new File(archivo);
	    StringBuilder sb = new StringBuilder();
	    sb.append(df.format(System.currentTimeMillis()));
	    sb.append(newline);
	    sb.append(mensaje);
	    sb.append(newline);
	    
	    try {
	        if (!archivo_log.exists()) {
	            archivo_log.createNewFile();
	        }
	        
	        // Imprimir el absolutePath y el contenido a escribir
	        String ruta = archivo_log.getAbsolutePath();
	        System.out.println("Ruta del archivo: " + ruta);
	        System.out.println("Contenido que se va a escribir: ");
	        System.out.println(sb.toString());
	        
	        // Escribir el contenido en el archivo
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
