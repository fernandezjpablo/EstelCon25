package es.ste.aderthad.portal;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Login() {
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("Metodo no permitido");
		}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usuario=request.getParameter("usuario");
		String password=request.getParameter("password");
		JSONObject resultado=new JSONObject();
		resultado.put("respuesta", "");
		boolean validar=SQLUsuarios.validarUsuario(usuario,password);
		if (validar)
		{ 
			HttpSession sesion=request.getSession(true);
			
			sesion.setAttribute("usuario",usuario);
			sesion.setAttribute("idSesion",sesion.getId());
			
			Cookie loginCookie = new Cookie("usuario",usuario);
			//setting cookie to expiry in 30 mins
			loginCookie.setMaxAge(30*60);
			Cookie sesionCookie=new Cookie("JSESSIONID",sesion.getId());
			response.addCookie(loginCookie);
			response.addCookie(sesionCookie);
			resultado.put("respuesta", "sesiongenerada");
			LoggerInscritos.registrarActividadSession(request, "Sesión iniciada");
		}
		else
		{
			resultado.put("respuesta", "loginerroneo");
			LoggerInscritos.registrarActividadSession(request, "Login erróneo");
		}
		response.getWriter().println(resultado.toString());
	}

}
