package es.ste.aderthad.checkin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.CheckinBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.sql.SQLCheckin;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Servlet implementation class GuardarCheckin
 */
@WebServlet("/GuardarCheckin")
public class GuardarCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GuardarCheckin() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject respuesta=new JSONObject();
		String mensajeRespuesta="";
		boolean resultado=false;
		boolean nuevo=false;
		HttpSession sesion=request.getSession(true);
		String idinscrito=request.getParameter("idinscrito");
		UsuarioBean usuario=SQLUsuarios.selectInscrito(idinscrito);
		String expedicion=request.getParameter("expedicion");
		String nacimiento=request.getParameter("nacimiento");
		InscritoBean beanInscrito;
		CheckinBean bean=SQLCheckin.selectCheckin(usuario.getUsuario());
		beanInscrito=SQLInscritos.selectIdInscrito(idinscrito);
		if (bean==null) {
			bean=new CheckinBean();
			nuevo=true;
		}
		bean.setNombre(beanInscrito.getNombre());
		bean.setApellidos(beanInscrito.getApellido());
		bean.setNif(beanInscrito.getNif());
		bean.setFechaNacimiento(nacimiento);
		bean.setFechaExpedicion(expedicion);
		bean.setIdInscrito(beanInscrito.getId());
		bean.setUsuario(usuario.getUsuario());
		bean.setFechaCreacion(System.currentTimeMillis());
		bean.setFechaUpdate(System.currentTimeMillis());
		

		if (nuevo)//nuevo checkin
		{
			resultado=SQLCheckin.insertCheckin(bean);
			if (!resultado) mensajeRespuesta="Error creando registro de checkin";
		}
		else
		{
			resultado=SQLCheckin.updateCheckin(bean);
			if (!resultado) mensajeRespuesta="Error actualizando datos checkin";
		}
		if (resultado)
		{
			respuesta.put("resultado","ok");
		}
		else
		{
			respuesta.put("resultado","error");
			respuesta.put("respuesta", mensajeRespuesta);
		}
		response.getWriter().println(respuesta.toString());
	}
}
