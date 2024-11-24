package es.ste.aderthad.portal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;
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
@WebServlet("/ActualizarPerfil")
public class ActualizarPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ActualizarPerfil() {
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject resultado=new JSONObject();
		HttpSession sesion=request.getSession();
		String usuario=(String) sesion.getAttribute("usuario");
		String idsesion=(String) sesion.getAttribute("idSesion");
		boolean bloqueo_datos=EntornoInscritos.getVariable("BLOQUEAR_DATOS_INSCRITOS").toUpperCase().equals("SI");
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
			resultado.put("respuesta", "caducada");
		}
		else
		{

			InscritoBean inscrito=new InscritoBean();
			inscrito.setNombre(request.getParameter("nombre"));
			inscrito.setPseudonimo(request.getParameter("pseudonimo"));
			inscrito.setApellido(request.getParameter("apellido"));
			inscrito.setTelefono(request.getParameter("telefono"));
			inscrito.setTelegram(request.getParameter("telegram"));
			inscrito.setAlergias_txt(request.getParameter("alergias_txt"));
			inscrito.setAlimentos_txt(request.getParameter("alimentos_txt"));
			inscrito.setId(request.getParameter("id"));
			inscrito.setAlergias(request.getParameter("alergias").equals("true"));
			inscrito.setAlimentos(request.getParameter("alimentos").equals("true"));
			inscrito.setNif(request.getParameter("nif"));
			inscrito.setEmail(request.getParameter("email"));
			inscrito.setSmial(request.getParameter("smial"));
			inscrito.setConBebes(request.getParameter("con_bebes"));
			inscrito.setMenor(request.getParameter("menor").equals("true"));
			inscrito.setObservaciones(URLDecoder.decode(request.getParameter("observaciones"),"UTF-8"));
			boolean respuesta=false;
					
			if (!bloqueo_datos) {
				respuesta=SQLInscritos.actualizarInscrito(inscrito);
			}
			if (respuesta)
				{
				resultado.put("respuesta", "ok");
				LoggerInscritos.registrarActividadSession(request, "Actualización de datos");
				}
			else
			{
				if (bloqueo_datos)
				{
					resultado.put("respuesta", "error: la organización ha bloqueado la modificación de perfiles");
					LoggerInscritos.registrarActividadSession(request, "Error al actualizar el perfil");
				}
				else
					{
					resultado.put("respuesta", "error");
					LoggerInscritos.registrarActividadSession(request, "Error al actualizar el perfil");
					}
				
			}
		}
    	response.getWriter().println(resultado.toString());
	}

}
