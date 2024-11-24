package es.ste.aderthad.inscripciones;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.CheckinBean;
import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.MensajeBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.mensajeria.GenerarArchivoMensaje;
import es.ste.aderthad.mensajeria.ServiciosMensajeria;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLCheckin;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLUsuarios;
import io.nayuki.qrcodegen.QrCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NotificarEstadoCheckin
 */
@WebServlet("/NotificarEstadoCheckin")
public class NotificarEstadoCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public NotificarEstadoCheckin() {
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
    private String generarMensajeCheckin(InscritoBean bean,CheckinBean chk)
    {
    	StringBuilder datosQr=new StringBuilder();
    	SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
    	//Comprobamos fecha de expedición para avisar
    	String expedicion=chk.getFechaExpedicion();
    	String alergias="";
    	String alimentos="";
    	HabitacionBean habitacion=SQLHabitaciones.selectHabitacion(bean.getHabitacion());
    	try {
       		
			
			datosQr.append("<h2>Hoja de acceso a la XXVIII Mereth Aderthad</h2>");
			datosQr.append("Nombre: "+chk.getNombre()+" "+chk.getApellidos()+";<br>");
			datosQr.append("Pseudónimo:"+bean.getPseudonimo()+"<br>");
			
			datosQr.append("NIF:"+chk.getNif()+"<br>Expedicion: "+chk.getFechaExpedicion()+"<br>");
			try
			{
				Date dtExpedicion = df.parse(expedicion);
			if (dtExpedicion.compareTo(new Date())>0)
			{
				datosQr.append("Atención: Fecha de <b><u>expedición</u></b> invólida: "+expedicion+" (futura)<br>");
				datosQr.append("Normalmente se debe a confundir Fecha de Expedición con Fecha de Caducidad<br>");
			}
			else
			{
				datosQr.append("Fecha de expedición correcta: "+expedicion+"<br>");	
			}
			} catch (ParseException e) {
				datosQr.append("Atención: Fecha de expedición con formato incorrecto<br>");
			}
			datosQr.append("Fecha nacimiento: "+chk.getFechaNacimiento()+"<br>");
			datosQr.append("Habitacion: "+habitacion.getIdentificador()+"<br>");
			datosQr.append("Observaciones: <br>"+cruzarActividades(bean.getId())+"<br>");
			alergias=bean.getAlergias_txt();
			if (alergias==null) alergias="(No constan alergias)";
			alimentos=bean.getAlimentos_txt();
			if (alimentos==null) alimentos="(No consta dieta específica)";
			datosQr.append("Alergias:"+alergias+"<br>");
			datosQr.append("Dieta:"+alimentos+"<br>");

			QrCode qr;
			qr=QrCode.encodeText(datosQr.toString(),QrCode.Ecc.HIGH);
			BufferedImage imagen=toImage(qr,6,10);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			ImageIO.write(imagen,"png",out);
			datosQr.append("<img style\"-webkit-print-color-adjust: exact !important;\" src=\"data:image/png;base64,"+Base64.getEncoder().encodeToString(out.toByteArray())+"\" style=\"width:640px;\">");


		
		} catch (IOException e) {
			datosQr.append("<br>Error al generar el QR<br>");
		}
    	
    	return datosQr.toString();
    }
    private boolean generarNotificacion(String inscrito)
    {
    	boolean resultado=true;
    	JSONObject resObj;
    	String res="";
    	String plantilla="";
    	CheckinBean beanchk;
    	StringBuilder cuerpo=new StringBuilder();
    	String nacimiento;
    	String expedicion;
    	InscritoBean bean=SQLInscritos.selectIdInscrito(inscrito);

    	MensajeBean mensaje=new MensajeBean();
		mensaje.setBlindcopyto("");
		mensaje.setCopyto("");
		mensaje.setTo(bean.getEmail());
    	beanchk=SQLCheckin.selectCheckin(SQLUsuarios.selectInscrito(inscrito).getUsuario());
    	cuerpo.append("Aiya, "+bean.getNombre()+" "+bean.getApellido()+" ("+bean.getPseudonimo()+")<br>");
    	cuerpo.append("Este es un mensaje automático remitido desde la plataforma de la Mereth Aderthad en relación a la información para acceder al evento (checkin).<br>");
    	if ( beanchk== null)
    	{
    		if (bean.isMenor())
    		{
            	cuerpo.append("Hemos intentado enviarte tus datos de acceso y el código QR, pero la información de checkin no está completa (falta la fecha de nacimiento del DNI-Pasaporte).<br>");
            	cuerpo.append("Aunque para los menores de 14 años no es imprescindible tener el código QR para acceder al evento, sí que es necesario para tener acceso a otra información relevante (dieta, alergias, camiseta, cancionero...)<br>");   
            	cuerpo.append("Os rogamos cumplimentéis los datos del checkin (fecha de nacimiento) para facilitar la tarea a la organización.<br>Muchas gracias.");
   		}
    		else
    		{
        			cuerpo.append("Hemos intentado enviarte tus datos de acceso y el código QR, pero la información de checkin no está cumplimentada (fecha de expedición del DNI-Pasaporte / fecha de nacimiento).<br>");
        			cuerpo.append("Te recordamos que sin realizar el checkin tendrás menos prioridad a la hora de acceder al evento.<br>");   		
    		}
    	}
    	else
    	{
    		nacimiento=beanchk.getFechaNacimiento();
    		expedicion=beanchk.getFechaExpedicion();
    		if ("".equals(nacimiento) && "".equals(expedicion))
    		{
        		if (bean.isMenor())
        		{
                	cuerpo.append("Hemos intentado enviarte tus datos de acceso y el código QR, pero la información de checkin no está completa (falta la fecha de nacimiento del DNI-Pasaporte).<br>");
                	cuerpo.append("Aunque para los menores de 14 años no es imprescindible tener el código QR, sí que es necesario para tener acceso a otra información relevante (dieta, alergias, camiseta, cancionero...)<br>");   
                	cuerpo.append("Os rogamos cumplimentéis los datos del checkin (fecha de nacimiento) para facilitar la tarea a la organización.<br>Muchas gracias.");
        		}
        		else
        		{
            	cuerpo.append("Hemos intentado enviarte tus datos de acceso y el código QR, pero la información de checkin no está completa (fecha de expedición del DNI-Pasaporte / fecha de nacimiento).<br>");
            	cuerpo.append("Te recordamos que sin realizar el checkin tendrás menos prioridad a la hora de acceder al evento.<br>");   
        		}
        		}
    		else
    		{
        		if ("".equals(nacimiento) && bean.isMenor())
        		{
                	cuerpo.append("Hemos intentado enviarte tus datos de acceso y el código QR, pero la información de checkin no está completa (falta la fecha de nacimiento del DNI-Pasaporte).<br>");
                	cuerpo.append("Aunque para los menores de 14 años no es imprescindible tener el código QR, sí que es necesario para tener acceso a otra información relevante (dieta, alergias, camiseta, cancionero...)<br>");   
                	cuerpo.append("Os rogamos cumplimentéis los datos del checkin (fecha de nacimiento) para facilitar la tarea a la organización.<br>Muchas gracias.");
        		}
        		else if ("".equals(expedicion) && !bean.isMenor())
        		{
                	cuerpo.append("Hemos intentado enviarte tus datos de acceso y el código QR, pero la información de checkin no está completa (falta la fecha de expedición del DNI-Pasaporte).<br>");
                	cuerpo.append("Te recordamos que sin realizar el checkin tendrás menos prioridad a la hora de acceder al evento.<br>");   
        		} 

        		else if (!"".equals(nacimiento) && bean.isMenor())
        		{
        			cuerpo.append(generarMensajeCheckin(bean,beanchk));
        		}
        		else if (!bean.isMenor() && !"".equals(nacimiento) && !"".equals(expedicion))
        		{
        			cuerpo.append(generarMensajeCheckin(bean,beanchk));
        		}
        		else
        		{
        			cuerpo.append(generarMensajeCheckin(bean,beanchk));
        		}
        		
    		}
    	}
    	mensaje.setBody(cuerpo.toString());
		mensaje.setSubject("Notificación Mereth Aderthad: Información para acceder al evento");
		mensaje.setTo(bean.getEmail());
		mensaje.setFrom(Entorno.getVariable("EMAIL_USER"));
		res=GenerarArchivoMensaje.generarMensajeArchivo(mensaje);
		resObj=new JSONObject(res);
		resultado= ("ok".equals(resObj.getString("respuesta")));
    	return resultado;
    }
    
    private boolean generarNotificacionesCheckin(String inscritos)
    {
    	boolean res=true;
    	String lista[]=inscritos.split(",");
    	for (int l=0;l<lista.length;l++)
    	{
    		res=res && generarNotificacion(lista[l]);
    	}
    	return res;
    }
    

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String listaInscritos=request.getParameter("inscritos");
		boolean resultado=false;
		Logger.registrarActividadSession(request, "Notificando estado de checkin a: "+listaInscritos+".");
		resultado=generarNotificacionesCheckin(listaInscritos);
		if (resultado)
			{
			response.getWriter().println("{\"resultado\":\"ok\"}");
			}
		else
		{
			response.getWriter().println("{\"resultado\":\"error\"}");
		}
	}

}
