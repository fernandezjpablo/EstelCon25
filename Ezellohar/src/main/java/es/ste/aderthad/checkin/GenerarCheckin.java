package es.ste.aderthad.checkin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.CheckinBean;
import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLCheckin;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLUsuarios;
import io.nayuki.qrcodegen.QrCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
/**
 * Servlet implementation class GenerarCheckin
 */
@WebServlet("/GenerarCheckin")
public class GenerarCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GenerarCheckin() {
        // TODO Auto-generated constructor stub
    }

	private String cruzarActividades(String idInscrito)
	{
		StringBuilder sb=new StringBuilder();
		ActividadBean beanActividad;
		String observaciones;
		String listaActividades=Entorno.getVariable("ACTIVIDADES_CHECKIN");
		if (!listaActividades.equals(""))
		{
			String[] actividades = listaActividades.split(",");
			for (int a=0;a<actividades.length;a++)
			{
				beanActividad=SQLActividades.buscarActividad(actividades[a]);
				if (beanActividad!=null)
				{
					observaciones=SQLInscripcionesActividades.selectObservacionesInscripcion(beanActividad.getIdActividad(),idInscrito);
					sb.append(actividades[a]+":"+observaciones+"<br>");
				}
			}
		}
		return sb.toString();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String usuario=request.getParameter("usuario");
		String alergias="";
		String alimentos="";

		
		try
		{
		InscritoBean inscrito=SQLInscritos.selectIdInscrito(usuario);
		UsuarioBean usuarioBean=SQLUsuarios.selectInscrito(usuario);
		CheckinBean beanCheckin=SQLCheckin.selectCheckin(usuarioBean.getUsuario());
		HabitacionBean habitacion=SQLHabitaciones.selectHabitacion(inscrito.getHabitacion());
		
		response.setContentType("text/html");
		if (beanCheckin==null)
		{
			response.getWriter().println("Este usuario aún no ha registrado los datos para el Check-in. Atualice la fecha de expedición del Documento identificativo y su fecha de nacimiento para poder obtener el código.");
		}
		else
		{
			//Tiene checkin, generamos QR

			StringBuilder datosQr=new StringBuilder();
			datosQr.append("<h2>Hoja de acceso a la XXVIII Mereth Aderthad</h2>");
			datosQr.append("Nombre: "+beanCheckin.getNombre()+" "+beanCheckin.getApellidos()+";<br>");
			datosQr.append("Pseudónimo:"+inscrito.getPseudonimo()+"<br>");
			
			datosQr.append("NIF:"+beanCheckin.getNif()+"\nExpedicion: "+beanCheckin.getFechaExpedicion()+"<br>");
			datosQr.append("Fecha nacimiento: "+beanCheckin.getFechaNacimiento()+"<br>");
			datosQr.append("Habitacion: "+habitacion.getIdentificador()+"<br>");
			datosQr.append("Observaciones: "+cruzarActividades(inscrito.getId())+"<br>");
			alergias=inscrito.getAlergias_txt();
			if (alergias==null) alergias="(No constan alergias)";
			alimentos=inscrito.getAlimentos_txt();
			if (alimentos==null) alimentos="(No consta dieta específica)";
			datosQr.append("Alergias:"+alergias+"<br>");
			datosQr.append("Dieta:"+alimentos+"<br>");
			datosQr.append("<img src=\"/Ezellohar/GenerarQR?usuario="+usuarioBean.getUsuario()+"\" style=\"width:640px;\">");
			response.getWriter().println(datosQr);

		}
	}
	catch (Exception e)
	{
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		response.getWriter().println("Error al generar Checkin. Pruebe a iniciar sesión de nuevo y reinténtelo.");
	}
	}


}
