package es.ste.aderthad.portal;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.ActividadBean;
import es.ste.aderthad.inscritos.data.CheckinBean;
import es.ste.aderthad.inscritos.data.HabitacionBean;
import es.ste.aderthad.inscritos.data.HabitacionParcialBean;
import es.ste.aderthad.inscritos.data.InscripcionActividadBean;
import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLActividades;
import es.ste.aderthad.inscritos.sql.SQLCheckin;
import es.ste.aderthad.inscritos.sql.SQLHabitacionesInscritos;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLInscritos;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;
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
 * Servlet implementation class GenerarQR
 */
@WebServlet("/GenerarQR")
public class GenerarQR extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GenerarQR() {
        // TODO Auto-generated constructor stub
    }
    private static BufferedImage toImage(QrCode qr, int scale, int border) {
		return toImage(qr, scale, border, 0xFFFFFF, 0x000000);
	}
	private static BufferedImage toImage(QrCode qr, int scale, int border, int lightColor, int darkColor) {
		Objects.requireNonNull(qr);
		if (scale <= 0 || border < 0)
			throw new IllegalArgumentException("Value out of range");
		if (border > Integer.MAX_VALUE / 2 || qr.size + border * 2L > Integer.MAX_VALUE / scale)
			throw new IllegalArgumentException("Scale or border too large");
		
		BufferedImage result = new BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < result.getHeight(); y++) {
			for (int x = 0; x < result.getWidth(); x++) {
				boolean color = qr.getModule(x / scale - border, y / scale - border);
				result.setRGB(x, y, color ? darkColor : lightColor);
			}
		}
		return result;
	}
	
	
	private String cruzarActividades(String idInscrito)
	{
		StringBuilder sb=new StringBuilder();
		ActividadBean beanActividad;
		String observaciones;
		String listaActividades=EntornoInscritos.getVariable("ACTIVIDADES_CHECKIN");
		if (!listaActividades.equals(""))
		{
			String[] actividades = listaActividades.split(",");
			for (int a=0;a<actividades.length;a++)
			{
				beanActividad=SQLActividades.buscarActividad(actividades[a]);
				if (beanActividad!=null)
				{
					observaciones=SQLInscripcionesActividades.selectObservacionesInscripcion(beanActividad.getIdActividad(),idInscrito);
					sb.append(actividades[a]+":"+observaciones+"\n");
				}
			}
		}
		return sb.toString();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion=request.getSession(true);
		String usuario=(String) sesion.getAttribute("usuario");
		String alergias="";
		String alimentos="";
		String idsesion=(String) sesion.getAttribute("idSesion");
		CheckinBean beanCheckin=SQLCheckin.selectCheckin(usuario);
		try
		{
		InscritoBean inscrito=SQLUsuarios.selectUsuario(usuario);
		HabitacionBean habitacion=SQLHabitacionesInscritos.selectHabitacion(inscrito.getHabitacion());
		
		
		if (beanCheckin==null)
		{
			response.getWriter().println("Este usuario aún no ha registrado los datos para el Check-in. Atualice la fecha de expedición del Documento identificativo y su fecha de nacimiento para poder obtener el código.");
		}
		else
		{
			//Tiene checkin, generamos QR
			QrCode qr;
			StringBuilder datosQr=new StringBuilder();
			datosQr.append("Nombre: "+beanCheckin.getNombre()+" "+beanCheckin.getApellidos()+";\n");
			datosQr.append("Pseudónimo:"+inscrito.getPseudonimo()+"\n");
			
			datosQr.append("NIF:"+beanCheckin.getNif()+"\nExpedicion: "+beanCheckin.getFechaExpedicion()+"\n");
			datosQr.append("Fecha nacimiento: "+beanCheckin.getFechaNacimiento()+"\n");
			datosQr.append("Habitacion: "+habitacion.getIdentificador()+"\n");
			datosQr.append("Observaciones: "+cruzarActividades(inscrito.getId())+"\n");
			alergias=inscrito.getAlergias_txt();
			if (alergias==null) alergias="(No constan alergias)";
			alimentos=inscrito.getAlimentos_txt();
			if (alimentos==null) alimentos="(No consta dieta específica)";
			datosQr.append("Alergias:"+alergias+"\n");
			datosQr.append("Dieta:"+alimentos+"\n");
			qr=QrCode.encodeText(datosQr.toString(),QrCode.Ecc.HIGH);
			BufferedImage imagen=toImage(qr,6,10);
			ServletOutputStream out=response.getOutputStream();
			ImageIO.write(imagen,"png",out);
			out.close();
		}
	}
		catch (Exception e)
		{
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
			response.getWriter().println("Error al generar QR. Pruebe a iniciar sesión de nuevo y reinténtelo.");
		}
	}


}
