package es.ste.aderthad.publico.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntornoPublic {

    private static final Logger LOGGER = Logger.getLogger(EntornoPublic.class.getName());

    // Definición de las rutas por defecto
   /* private static String entornoLocal = "/home/estelcon/estelcon.properties";
    private static String entornoRemoto = System.getenv("ESTELCON_PROPERTIES") != null
            ? System.getenv("ESTELCON_PROPERTIES")
            : "/home/estelcon/estelcon.properties";*/

    
    private static String entornoLocal = "/home/fernandez_juafer2/estelcon/estelcon.properties";
    private static String entornoRemoto = System.getenv("ESTELCON_PROPERTIES") != null
            ? System.getenv("ESTELCON_PROPERTIES")
            : "/home/fernandez_juafer2/estelcon/estelcon.properties";
    
    
    /**
     * Obtiene el valor de una variable desde el archivo de propiedades.
     * 
     * @param nombreVariable Nombre de la variable a buscar.
     * @return Valor de la variable o cadena vacía si no se encuentra.
     */
    public static String getVariable(String nombreVariable) {
        String resultado = "";
        File file = new File(entornoRemoto);

        // Comprobar si la ruta remota existe
        if (!file.exists()) {
            LOGGER.info("Archivo de propiedades no encontrado en ruta remota: " + entornoRemoto);
            file = new File(entornoLocal);

            // Comprobar si la ruta local existe
            if (!file.exists()) {
                LOGGER.severe("Archivo de propiedades no encontrado en ruta local: " + entornoLocal);
                return ""; // Devolver cadena vacía en lugar de "<error>"
            }
        }

        // Leer el archivo de propiedades
        try (InputStream input = new FileInputStream(file.getAbsolutePath())) {
            Properties prop = new Properties();
            prop.load(input);

            resultado = prop.getProperty(nombreVariable);
            if (resultado == null) {
                resultado = "";
                LOGGER.warning("Variable no encontrada en el archivo: " + nombreVariable);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error leyendo el archivo de propiedades: " + e.getMessage(), e);
            resultado = "";
        }

        return resultado;
    }

    public static void main(String[] args) {
        // Prueba de la clase para verificar si se encuentra el archivo y las variables
        String pruebaVariable = "miVariable";
        String valor = getVariable(pruebaVariable);

        if (valor.isEmpty()) {
            System.out.println("No se encontró el valor para la variable: " + pruebaVariable);
        } else {
            System.out.println("Valor encontrado para la variable " + pruebaVariable + ": " + valor);
        }
    }
}
