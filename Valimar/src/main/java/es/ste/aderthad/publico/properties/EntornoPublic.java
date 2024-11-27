package es.ste.aderthad.publico.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.ste.aderthad.publico.log.LoggerPublic;

public class  EntornoPublic {
 private static String entornoLocal="/home/fernandez_juafer2/estelcon/estelcon.properties";
 private static String entornoRemoto="/home/fernandez_juafer2/estelcon/estelcon.properties";
 
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
				LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
				resultado="<error>";
			}
			 return resultado;
		}
	 
}
