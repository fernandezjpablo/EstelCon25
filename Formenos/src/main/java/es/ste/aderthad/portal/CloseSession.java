package es.ste.aderthad.portal;

import java.io.IOException;

import org.json.JSONObject;

import es.ste.aderthad.inscritos.log.LoggerInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class CloseSession
 */
@WebServlet("/CloseSession")
public class CloseSession extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CloseSession() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion=request.getSession();
		LoggerInscritos.registrarActividadSession(request, "Cerrando sesión");
		JSONObject respuesta=new JSONObject();
		respuesta.put("respuesta", "ok");
		sesion.invalidate();
		
    	Cookie loginCookie = null;
    	Cookie sesionCookie=null;
    	Cookie[] cookies = request.getCookies();
    	if(cookies != null){
    	for(Cookie cookie : cookies){
    		if(cookie.getName().equals("usuario")){
    			loginCookie = cookie;
    			if(loginCookie != null){
    	    		loginCookie.setMaxAge(0);
    	        	response.addCookie(loginCookie);
    	    	}
    		}
    		if(cookie.getName().equals("JSESSIONID")){
    			sesionCookie = cookie;
    			if(sesionCookie != null){
    				sesionCookie.setMaxAge(0);
    	        	response.addCookie(sesionCookie);
    	    	}
    		}
    	}
    	}
		response.getWriter().println(respuesta.toString());
	}


}
