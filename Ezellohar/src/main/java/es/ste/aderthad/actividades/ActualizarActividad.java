package es.ste.aderthad.actividades;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ActualizarActividad
 */
@WebServlet("/admin/actividades/ActualizarActividad")
public class ActualizarActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ActualizarActividad() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject respuesta=new JSONObject();
		
		respuesta.put("resultado", "ok");
		String nombre=request.getParameter("nombre");
		String descripcion=URLDecoder.decode(request.getParameter("descripcion"),"UTF-8");
		String aforo=request.getParameter("aforo");
		String responsables=request.getParameter("responsables");
		String pseudonimos=request.getParameter("pseudonimos");
		String publico=request.getParameter("publico");
		String duracion=request.getParameter("duracion");
		String tipo=request.getParameter("tipo");
		String observaciones=URLDecoder.decode(request.getParameter("observaciones"),"UTF-8");
		String requisitos=URLDecoder.decode(request.getParameter("requisitos"),"UTF-8");		
		boolean pago=request.getParameter("pago").equals("true");
		
		String idActividad=request.getParameter("id");
		
		ActividadBean bean =new ActividadBean();
		
		bean.setNombreActividad(nombre);
		bean.setDescripcion(descripcion);
		bean.setAforo(Integer.valueOf(aforo));
		bean.setResponsables(responsables);
		bean.setNombres_responsables(pseudonimos);
		bean.setPublico(publico);
		bean.setIdActividad(idActividad);
		bean.setPagoAdicional(pago);
		bean.setDuracion(duracion);
		bean.setTipo(tipo);
		bean.setObservaciones(observaciones);
		bean.setRequisitos(requisitos);		
		boolean resultado=SQLActividades.updateActividad(bean);
		if (!resultado) respuesta.put("resultado", "error");
		Logger.registrarActividadSession(request, "Actualizando actividad "+nombre+"("+idActividad+").");
		response.getWriter().println(respuesta.toString());
	}

}
