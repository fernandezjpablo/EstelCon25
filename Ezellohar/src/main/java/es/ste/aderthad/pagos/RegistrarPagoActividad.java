package es.ste.aderthad.pagos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.PagosBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLActividades;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscripcionesActividades;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import es.ste.aderthad.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrarPagoActividad
 */
@WebServlet("/admin/pagos/RegistrarPagoActividad")
public class RegistrarPagoActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegistrarPagoActividad() {
        // TODO Auto-generated constructor stub
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject peticion=new JSONObject();
		JSONObject resultado=new JSONObject();
		StringBuilder sb=new StringBuilder();
		boolean operaciones=true;
		String accion=request.getParameter("accion");
		String mensajeAccion="anulado";
		int estado=3;
		resultado.put("resultado", "ok");
		peticion.put("idInscrito", request.getParameter("idInscrito"));
		peticion.put("idActividad", request.getParameter("idActividad"));
		try
		{
			peticion.put("importe", Double.parseDouble(request.getParameter("importe")));	

		if (accion.equals("1")) {
			estado=1;
			mensajeAccion="Ingreso:";
		}
		else
		{
			estado=0;
			peticion.put("importe", Double.parseDouble(request.getParameter("importe"))*-1);
		}


		InscritoBean inscrito=SQLInscritos.selectIdInscrito(peticion.getString("idInscrito"));
		UsuarioBean usuario=SQLUsuarios.selectInscrito(peticion.getString("idInscrito"));
		JSONObject actividad=SQLActividades.consultarActividad(peticion.getString("idActividad"));
		PagosBean pago=new PagosBean();
		pago.setEstado(1);
		pago.setObservaciones("Actividad;"+actividad.getString("nombreActividad")+";"+usuario.getUsuario()+";Nombre;"+inscrito.getNombre()+" "+inscrito.getApellido()+ ";Importe;"+peticion.getDouble("importe")+";Observaciones:"+SQLInscripcionesActividades.selectObservaciones(peticion.getString("idActividad"), peticion.getString("idInscrito").replaceAll(";", ""))+";Código operación:"+peticion.getString("idInscrito")+"@"+peticion.getString("idActividad"));
		pago.setImporte(peticion.getDouble("importe"));
		pago.setFecha(System.currentTimeMillis());
		pago.setIdInscrito(peticion.getString("idInscrito"));
		pago.setIdactividad(peticion.getString("idActividad"));
		pago.setIdPago(UUID.randomUUID().toString());
		operaciones=SQLPagos.registrarPago(pago);
		if (!operaciones) 
			{
			resultado.put("resultado","Error al registrar el movimiento");
			}
		else
		{
			SQLInscripcionesActividades.updateInscripcionActividad(request.getParameter("idActividad"),request.getParameter("idInscrito"),estado);
			Logger.registrarActividadSession(request, "Registrando pago de actividad "+pago.getIdactividad()+" por "+pago.getImporte()+" del inscrito "+pago.getIdInscrito()+" "+pago.getObservaciones()+".");
		}
		}
		catch(Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado.put("resultado","Error en el importe");
		}
		
		response.getWriter().print(resultado.toString());
	}


}
