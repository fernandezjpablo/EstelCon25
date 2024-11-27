package es.ste.aderthad.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


import es.ste.aderthad.log.Logger;

public class  Entorno {
 //private static String entornoLocal="C:/temp/Proyecto EstelCon/dev - Lendar/docker/files/estelcon.properties.docker";
	// private static String entornoLocal="/home/fernandez_juafer2/estelcon/estelcon.properties";
	// private static String entornoRemoto="/home/fernandez_juafer2/estelcon/estelcon.properties";
	 
	 private static String entornoLocal="/home/estelcon/estelcon.properties";
	 private static String entornoRemoto="/home/estelcon/estelcon.properties";
 
 public static String getVariable(String nombreVariable)
 {
	 String resultado="";
	 
	 File file=new File(entornoRemoto);
	
	 if (!file.exists())
	 {
		 file=new File(entornoLocal);
		 if (!file.exists())
		 {
			 //Crear entrada Log de Property no encontrada
			 return "<error>";
		 }
	}
	
	try {
			 InputStream input=new FileInputStream(file.getAbsolutePath());
			 Properties prop=new Properties();

				prop.load(input);
				resultado=prop.getProperty(nombreVariable);
				if (resultado==null) resultado="";
			} catch (IOException e) {
				Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
				resultado="<error>";
			}
			 return resultado;
		}
	 

public static String getEntorno()
{
	 String resultado="";
	 File file=new File(entornoRemoto);
	 Logger.GenerarEntradaLogMensaje("Entro en el entorno getEntorno "+entornoRemoto, Logger.getFileNameErrorLog());
	 if (!file.exists())
	 {
		 file=new File(entornoLocal);
		 if (!file.exists())
		 {
			 //Crear entrada Log de Property no encontrada
			 return "<error>";
		 }
	}
	
	try {
			 InputStream input=new FileInputStream(file.getAbsolutePath());
			 BufferedReader br=new BufferedReader(new InputStreamReader(input));
			 StringBuilder sb=new StringBuilder();
			 String linea;
			 while ((linea=br.readLine())!=null)
			 {
				 sb.append(linea+"\n");
			 }
			 resultado=sb.toString();
			} catch (IOException e) {
				Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
				resultado="<error>";
			}
			 return resultado;
		}
	 

public static String guardarEntorno(String datos) {
	 String resultado="";
	 File file=new File(entornoRemoto);
	 if (!file.exists())
	 {
		 file=new File(entornoLocal);
		 if (!file.exists())
		 {
			 //Crear entrada Log de Property no encontrada
			 return "<error>";
		 }
	}

	if (file.exists() && file.isFile())
	{
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(datos);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getLocalizedMessage();
		}
	}
	else
	{
		resultado="La configuración no existe";
	}

	return resultado;
}
}
