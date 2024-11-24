package es.ste.aderthad.portal;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.CheckinBean;
import es.ste.aderthad.inscritos.data.HabitacionBean;
import es.ste.aderthad.inscritos.data.HabitacionParcialBean;
import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
import es.ste.aderthad.inscritos.sql.SQLCheckin;
import es.ste.aderthad.inscritos.sql.SQLHabitacionesInscritos;
import es.ste.aderthad.inscritos.sql.SQLInscritos;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class GetPerfil
 */
@WebServlet("/GetPerfil")
public class GetPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetPerfil() {
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject resultado=new JSONObject();
		JSONObject datosCheckin=new JSONObject();
		datosCheckin.put("fechaExpedicion", "");
		datosCheckin.put("fechaNacimiento", "");
		datosCheckin.put("direccion", "");
		datosCheckin.put("codigo_postal", "");
		datosCheckin.put("pais", "");
		datosCheckin.put("ciudad", "");
		
		HttpSession sesion=request.getSession(true);
		String usuario=(String) sesion.getAttribute("usuario");
		String idsesion=(String) sesion.getAttribute("idSesion");
		String idHabitacionPrincipal="";
		resultado.put("checkin",false);
		resultado.put("esteltienda",false);	
		resultado.put("datosCheckin", datosCheckin);
    	Cookie loginCookie = null;
    	Cookie sesionCookie=null;
    	Cookie[] cookies = request.getCookies();
    	if(cookies != null){
    	for(Cookie cookie : cookies){
    		if(cookie.getName().equals("usuario")){
    			loginCookie = cookie;
    			usuario=loginCookie.getValue();
    		}
    		if(cookie.getName().equals("JSESSIONID")){
    			sesionCookie = cookie;
    			idsesion=sesionCookie.getValue();
    		}
    	}
    	}
		
		
		if (usuario==null || idsesion==null)
		{
			resultado.put("respuesta", "error");
		}
		else
		{
			InscritoBean inscrito=SQLUsuarios.selectUsuario(usuario);
			JSONObject objUsuario=new JSONObject(inscrito.toJson());
			objUsuario.put("tipoHabitacion", inscrito.obtenerTipoHabitacion());
			objUsuario.put("importePlaza", inscrito.calcularImporte());
			objUsuario.put("estadoPagos", inscrito.obtenerEstadoPagos());

			HabitacionBean habitacion=SQLHabitacionesInscritos.selectHabitacion(inscrito.getHabitacion());
			HabitacionParcialBean parcial;
			if (habitacion==null)
			{
				//Si es null es una plaza parcial, buscamos la habitación padre
				parcial=SQLHabitacionesInscritos.selectHabitacionParcial(inscrito.getHabitacion());
				if (parcial==null)
				{
					idHabitacionPrincipal="";
				}
				else
				{
					idHabitacionPrincipal=parcial.getIdHabitacionRaiz();
				}
			}
			else
			{
				idHabitacionPrincipal=inscrito.getHabitacion();
			}

			if (EntornoInscritos.getVariable("MOSTRAR_OCUPANTES").toUpperCase().equals("SI"))
			{

					objUsuario.put("ocupantes", SQLInscritos.ocupantesHabitacion(idHabitacionPrincipal));

			}
			if (EntornoInscritos.getVariable("CHECKIN_VISIBLE").toUpperCase().equals("SI"))
			{
					
					CheckinBean beanCheckin = SQLCheckin.selectCheckin(usuario);
					resultado.put("checkin",true);
					if (beanCheckin!=null)
					{
						
					datosCheckin.put("fechaExpedicion", beanCheckin.getFechaExpedicion());
					datosCheckin.put("fechaNacimiento", beanCheckin.getFechaNacimiento());
					datosCheckin.put("direccion", beanCheckin.getDireccion());
					datosCheckin.put("codigo_postal", beanCheckin.getCodigo_postal());
					datosCheckin.put("pais", beanCheckin.getPais());
					datosCheckin.put("ciudad", beanCheckin.getCiudad());
					}
			}
			if (EntornoInscritos.getVariable("ESTELTIENDA_VISIBLE").toUpperCase().equals("SI"))
			{
					resultado.put("esteltienda",true);
			}
			resultado.put("usuario", objUsuario);
			resultado.put("respuesta", "ok");

			
		}
    	response.getWriter().println(resultado.toString());
	}

}
