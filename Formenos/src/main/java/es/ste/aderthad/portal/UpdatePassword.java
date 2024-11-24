package es.ste.aderthad.portal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
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
 * Servlet implementation class UpdatePassword
 */
@WebServlet("/UpdatePassword")
public class UpdatePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdatePassword() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject resultado=new JSONObject();
		HttpSession sesion=request.getSession();
		String usuario=(String) sesion.getAttribute("usuario");
		String idsesion=(String) sesion.getAttribute("idSesion");
		
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

			String oldpass=request.getParameter("oldpass");
			String newpass=request.getParameter("newpass");
			

			boolean respuesta=SQLUsuarios.updatePassword(usuario, oldpass, newpass);
			if (respuesta)
				{
				resultado.put("respuesta", "ok");
				LoggerInscritos.registrarActividadSession(request, usuario+":Contraseña modificada");
				}
			else
			{
				resultado.put("respuesta", "error");
				LoggerInscritos.registrarActividadSession(request, usuario+":Error al modificar contraseña");
			}
		}
    	response.getWriter().println(resultado.toString());
	}

}
