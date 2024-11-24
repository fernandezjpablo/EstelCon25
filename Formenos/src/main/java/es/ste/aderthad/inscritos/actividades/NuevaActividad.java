package es.ste.aderthad.inscritos.actividades;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.ActividadBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.sql.SQLActividades;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class NuevaActividad
 */
@WebServlet("/NuevaActividad")
public class NuevaActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public NuevaActividad() {
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
		String requisitos=request.getParameter("requisitos");
		String observaciones=request.getParameter("observaciones");
		String aforo=request.getParameter("aforo");
		String duracion=request.getParameter("duracion");
		String responsables=request.getParameter("responsables");
		String pseudonimos=request.getParameter("pseudonimos");
		String publico=request.getParameter("publico");
		boolean pago=request.getParameter("pago").equals("true");
		
		String idActividad=UUID.randomUUID().toString();
		
		ActividadBean bean =new ActividadBean();
		
		bean.setNombreActividad(nombre);
		bean.setDescripcion(descripcion);
		bean.setAforo(Integer.valueOf(aforo));
		bean.setResponsables(responsables);
		bean.setRequisitos(requisitos);
		bean.setObservaciones(observaciones);
		bean.setNombres_responsables(pseudonimos);
		bean.setPublico(publico);
		bean.setIdActividad(idActividad);
		bean.setFecha(0);
		bean.setHora_fin(0);
		bean.setHora_inicio(0);
		bean.setPagoAdicional(pago);
		bean.setDuracion(duracion);
		bean.setEstado(9);//Propuesta
		boolean resultado=SQLActividades.insertActividad(bean);
		if (!resultado) 
			{
			respuesta.put("resultado", "error");
			LoggerInscritos.registrarActividadSession(request, "Fallo al proponer actividad.");
			}
		else
		{
			LoggerInscritos.registrarActividadSession(request, "Nueva actividad propuesta: "+nombre);
		}
		response.getWriter().println(respuesta.toString());
	}

}
