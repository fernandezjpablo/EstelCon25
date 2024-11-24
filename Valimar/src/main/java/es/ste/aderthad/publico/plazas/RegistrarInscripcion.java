package es.ste.aderthad.publico.plazas;

import es.ste.aderthad.publico.data.InscritoBean;
import es.ste.aderthad.publico.sql.SQLHabitacionesPublic;
import es.ste.aderthad.publico.sql.SQLInscritosPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Servlet implementation class RegistrarInscripcion
 */
@WebServlet("/RegistrarInscripcion")
public class RegistrarInscripcion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegistrarInscripcion() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String datos=request.getParameter("datos");
		String habitacion=request.getParameter("habitacion");
		String con_bebes="0";
		boolean esParcial=false;
		String habitacionParcial=habitacion;
		JSONArray datosArr=new JSONArray(datos);
		boolean esmenor=false;
		InscritoBean bean;
		StringBuilder sb=new StringBuilder();
		JSONObject item;
		boolean existe=false;
		for (int i=0;i<datosArr.length();i++)
		{
		item=datosArr.getJSONObject(i);
		bean=new InscritoBean();
		bean.setNif(item.getString("iden"));
		esmenor=datosArr.getJSONObject(i).getBoolean("menor");
		if (!esmenor && SQLInscritosPublic.checkExiste(bean)) {
			existe=true;
			sb.append("<p><span class=\"aviso\">El NIF "+bean.getNif()+" ya est&aacute; registrado.</p>");
		}
		}
		if (!existe)
		{
			
			boolean resultadoGlobal=true;
			for (int i=0;i<datosArr.length();i++)
			{
			item=datosArr.getJSONObject(i);
			if (item.has("parcial"))
			{
				esParcial=item.getBoolean("parcial");
				con_bebes="0";
			}
			else
			{
				con_bebes=String.valueOf(item.getInt("con_bebes"));
			}
			bean=new InscritoBean();
			bean.setNif(item.getString("iden"));
			bean.setNombre(item.getString("nombre"));
			bean.setApellido(item.getString("apellidos"));
			bean.setTelefono(item.getString("telefono"));
			bean.setEmail(item.getString("email"));
			bean.setMenor(item.getBoolean("menor"));
			bean.setConBebes(con_bebes);
			bean.setPseudonimo(item.getString("pseudonimo"));
			bean.setId(UUID.randomUUID().toString());
			/*
			if (esParcial)
			{
				habitacion=habitacion.substring(0,habitacion.lastIndexOf("-"));
			}
			*/
			bean.setHabitacion(habitacion);
			resultadoGlobal=resultadoGlobal && SQLInscritosPublic.altaInscrito(bean);
			if (resultadoGlobal) {
				
				sb.append("<p><span class=\"aviso\">"+bean.getNombre()+" "+bean.getApellido()+" ("+bean.getNif()+") registrado.</span></p>");
				if (!Procesos.ProcesoPostInscripcion(bean))
				{
					sb.append("Se ha producido un error al generar los mensajes de confirmaci&oacute;n y de datos de acceso. Por favor, contacta con la organizaci&oacute;n.");
				}
			}
			else
			{
				sb.append("<p>No se ha podido registrar a "+bean.getNombre()+" "+bean.getApellido()+". P&oacute;ngase en contacto con la organizaci&oacute;n del evento.</p>");
			}

			}
			if (resultadoGlobal)
			{
				if (!esParcial)
				{
					resultadoGlobal=resultadoGlobal && SQLHabitacionesPublic.estadoHabitaciones(habitacion,3);
				}
				else
				{
					resultadoGlobal=resultadoGlobal && SQLHabitacionesPublic.estadoHabitacionesParciales(habitacionParcial,3);
				}
			}
			if (resultadoGlobal)
				{
				if (habitacion.toLowerCase().contains("lista de espera"))
				{
					sb.append("<p>Inscrito en la lista de espera.</p>");
				}
				else
					{
					sb.append("<p>El identificador de la habitaci&oacute;n es: <b>"+habitacion+"</b></p>");
					if (esParcial)
						{
						sb.append("<p>En unos minutos recibir&aacute;s un correo de confirmaci&oacute;n y otro con tus datos de acceso a la zona de inscritos (revisa la carpeta de 'Spam').</p>");
						}
					else
					{
						sb.append("<p>En unos minutos recibir&eacute;is un correo (cada un@) de confirmaci&oacute;n de inscripci&oacute;n y otro con los datos de acceso a la zona de inscritos (revisad la carpeta de 'Spam').</p>");
					}
					}
				}
			else
			{
				sb.append("No se ha podido reservar la plaza con id "+habitacionParcial+". P&oacute;ngase en contacto con la organizaci&oacute;n del evento.");
			}
			
		}
		response.getWriter().println(sb.toString());
	}

}
