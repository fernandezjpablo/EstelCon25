package es.ste.aderthad.inscritos.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.ste.aderthad.inscritos.log.LoggerInscritos;

public class  EntornoInscritos {
	 private static String entornoLocal="/home/fernandez_juafer2/estelcon/estelcon.properties";
	 private static String entornoRemoto="/home/fernandez_juafer2/estelcon/estelcon.properties";
 
 public static String getVariable(String nombreVariable)
 {
	 String resultado="";
	 File file=new File(entornoRemoto);
	 if (!file.exists())
	 {
		 file=new File(entornoLocal);
		 System.out.println("Entorno Local Escogido");
		 if (!file.exists())
		 {
			 //Crear entrada Log de Property no encontrada
			 System.out.println("No encuentra "+file.toString());
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
				System.out.println("No encuentra "+e.toString());
				LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
				resultado="<error>";
			}
			 return resultado;
		}
	 
}
