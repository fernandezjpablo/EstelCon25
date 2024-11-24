package es.ste.aderthad.inscritos.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.ste.aderthad.inscritos.log.LoggerInscritos;

public class  EntornoInscritos {
	 private static String entornoLocal="D:\\devtemp\\docker\\files\\aldaron.properties";
	 private static String entornoRemoto="/home/estelcon/aldaron.properties";
 
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
				LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
				resultado="<error>";
			}
			 return resultado;
		}
	 
}
