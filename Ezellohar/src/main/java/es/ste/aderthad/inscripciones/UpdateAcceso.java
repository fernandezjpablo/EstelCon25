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
 * Servlet implementation class UpdateAcceso
 */
@WebServlet("/UpdateAcceso")
public class UpdateAcceso extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdateAcceso() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idInscrito=request.getParameter("idinscrito");
		String usuario=request.getParameter("usuario");
		String password=request.getParameter("password");
		
		JSONObject respuesta=new JSONObject();
		respuesta.put("resultado","ok");
		UsuarioBean bean=new UsuarioBean();
		bean.setIdInscrito(idInscrito);
		bean.setPassword(password);
		bean.setUsuario(usuario);
		boolean resultado = SQLUsuarios.updateUsuario(bean);
		if (!resultado) respuesta.put("resultado", "error");
		Logger.registrarActividadSession(request, "Actualizando datos de acceso para "+idInscrito+" con Usuario:"+ usuario+".");
		response.getWriter().println(respuesta.toString());
	}

}
