package es.ste.aderthad.espacios;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.data.EspacioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLEspacios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NuevoEspacio
 */
@WebServlet("/admin/espacios/NuevoEspacio")
public class NuevoEspacio extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public NuevoEspacio() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject respuesta=new JSONObject();
		
		respuesta.put("resultado", "ok");
		
		String nombre=request.getParameter("nombre");
		String descripcion=request.getParameter("descripcion");
		String aforo=request.getParameter("capacidad");
		String planta=request.getParameter("planta");
		String idEspacio=UUID.randomUUID().toString();
		
		EspacioBean bean =new EspacioBean();
		
		bean.setNombreEspacio(nombre);
		bean.setDescripcion(descripcion);
		bean.setAforo(Integer.valueOf(aforo));
		bean.setIdEspacio(idEspacio);
		bean.setPlanta(planta);
		boolean resultado=SQLEspacios.insertEspacio(bean);
		if (!resultado) respuesta.put("resultado", "error");
		Logger.registrarActividadSession(request, "Nuevo espacio "+nombre+"-"+idEspacio+".");
		response.getWriter().println(respuesta.toString());
	}
	

}
