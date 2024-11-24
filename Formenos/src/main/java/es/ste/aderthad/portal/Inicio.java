package es.ste.aderthad.portal;

import java.io.IOException;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Inicio
 */
@WebServlet("/Inicio")
public class Inicio extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Inicio() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			response.sendRedirect("/api/MerethInscritos/login.html");
		}
		else
		{
			response.sendRedirect("/api/MerethInscritos/portal.html");
		}
		
	}


}
