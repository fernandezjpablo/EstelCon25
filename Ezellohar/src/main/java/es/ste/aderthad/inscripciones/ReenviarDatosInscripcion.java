package es.ste.aderthad.inscripciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import es.ste.aderthad.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReenviarDatosInscripcion
 */
@WebServlet("/ReenviarDatosInscripcion")
public class ReenviarDatosInscripcion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ReenviarDatosInscripcion() {
        // TODO Auto-generated constructor stub
    }

    private static String generarMensajeRecuperacion(String idsusuario)
	{
		JSONObject resultado=new JSONObject();
		resultado.put("mensajes", new JSONArray());
		resultado.put("id_mensaje", new JSONArray());
		String[] inscritosArr=idsusuario.split(",");
		for (int j=0;j<inscritosArr.length;j++)
		{
			String idusuario=inscritosArr[j];
			InscritoBean inscrito=SQLInscritos.selectIdInscrito(idusuario);
		
		StringBuilder cuerpo=new StringBuilder();
		JSONArray recuperaciones=SQLUsuarios.selectDatosAcceso(idusuario);
		if (recuperaciones.length()==0)
		{		
			resultado.put("respuesta","error");
			resultado.put("mensaje", "No se encontraron usuarios registrados con este id");
		}
		else
		{
			JSONObject item;
			cuerpo.append("Hola, estos son tus datos de acceso para la zona de Inscritos de la EstelCon:<br>");
			for (int i=0;i<recuperaciones.length();i++)
			{
				item=recuperaciones.getJSONObject(i);
				cuerpo.append(item.getString("nombre")+" "+item.getString("apellidos")+"<br>");
				cuerpo.append("Usuario/Contraseña: "+item.getString("usuario")+"/"+item.getString("password")+"<br>");
			}
		String asunto="Tus datos de acceso a la EstelCon 2024";


		String from=Entorno.getVariable("SMTP_FROM");		
			
	



			String newline = System.getProperty("line.separator");
		resultado.put("respuesta","ok");
		resultado.put("mensaje", "Correo generado, se enviarán los datos de acceso en los próximos minutos.");
		String idBase=UUID.randomUUID().toString();
		resultado.getJSONArray("id_mensaje").put(idBase);
		
		StringBuilder sb=new StringBuilder();
		String rutaOutbox=Entorno.getVariable("EMAIL_OUTBOX");
		resultado.getJSONArray("mensajes").put(rutaOutbox+idBase+".eml");
		File archivo_mensaje=new File(rutaOutbox+idBase+".eml");
		sb.append("FROM:"+from);
		sb.append(newline);
		sb.append("TO:").append(newline);
		sb.append("CC:").append(newline);
		
		sb.append("BCC:"+inscrito.getEmail()).append(newline);
		
		sb.append("SUBJECT:"+asunto).append(newline);
		sb.append("BODY:").append(newline);
		sb.append(cuerpo).append(newline);
		try {
			if (!archivo_mensaje.exists()) {
				archivo_mensaje.createNewFile();
            }
			BufferedWriter bw = Files.newBufferedWriter(Paths.get(archivo_mensaje.getAbsolutePath()));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException ex) {
			Logger.GenerarEntradaLogError(ex, Logger.getFileNameErrorLog());
			resultado.put("respuesta", "excepcion");
			resultado.put("mensaje", ex.getLocalizedMessage());
		}
		}
		}
		return resultado.toString();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idsusuario=request.getParameter("inscritos");
		response.setContentType("text/plain");
		response.getWriter().println(generarMensajeRecuperacion(idsusuario));
		Logger.registrarActividadSession(request, "Solicitud de recuperación de datos de acceso desde admin");
	}

}
