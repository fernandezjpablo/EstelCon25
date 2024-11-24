package es.ste.aderthad.inscripciones;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLUsuarios;

/**
 * Servlet implementation class GetAcceso
 */
@WebServlet("/GetAcceso")
public class GetAcceso extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetAcceso() {
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idInscrito=request.getParameter("idinscrito");
		
		JSONObject respuesta=new JSONObject();
		respuesta.put("resultado","ok");
		
		UsuarioBean bean=SQLUsuarios.selectInscrito(idInscrito);
		if (bean==null)
		{
			respuesta.put("resultado", "error");
		}
		else
		{
			respuesta.put("usuario", bean.getUsuario());
			respuesta.put("password", bean.getPassword());
		}
		Logger.registrarActividadSession(request, "Recuperando datos de acceso para: "+bean.getUsuario()+".");
		response.getWriter().println(respuesta.toString());
	}

}
