package es.ste.aderthad.mensajeria;

import java.io.IOException;


import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GenerarMensajeSaliente
 */
@WebServlet("/admin/mensajeria/GenerarMensajeSaliente")
public class GenerarMensajeSaliente extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GenerarMensajeSaliente() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



		String copiaoculta=request.getParameter("destino");
		String masivo=request.getParameter("masivo");
		String masivopendientes=request.getParameter("masivopendientes");
		String cuerpo=request.getParameter("data");
		String plantilla=request.getParameter("plantilla");
		String asunto=request.getParameter("asunto");
		
		MensajeBean mensaje=new MensajeBean();
		mensaje.setBlindcopyto(copiaoculta);
		mensaje.setCopyto("");
		mensaje.setBody(ServiciosMensajeria.generarCuerpo(plantilla,cuerpo,null));
		mensaje.setSubject(asunto);
		mensaje.setTo("");
		mensaje.setFrom(Entorno.getVariable("EMAIL_USER"));
		String resultado="";
		if (masivo.equals("true"))
		{
			resultado = GenerarArchivoMensaje.generarMensajeArchivoMasivo(mensaje);
		}
		else
			if (masivopendientes.equals("true"))
			{
				resultado = GenerarArchivoMensaje.generarMensajeArchivoMasivoDeuda(mensaje);
			}
			else
			{
				resultado = GenerarArchivoMensaje.generarMensajeArchivo(mensaje);
			}
		Logger.registrarActividadSession(request, "Mensaje saliente generado para "+copiaoculta+".");
		/*En este punto sólo recibimos la parte de texto del mensaje, los anexos de momento no están implementados*/
		
		response.getWriter().append(resultado);
		
	}

}
