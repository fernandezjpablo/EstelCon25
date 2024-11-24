package es.ste.aderthad.pagos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.PagosBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrarPago
 */
@WebServlet("/admin/pagos/RegistrarPago")
public class RegistrarPago extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegistrarPago() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject peticion=new JSONObject();
		JSONObject resultado=new JSONObject();
		StringBuilder sb=new StringBuilder();
		double precio;
		boolean operaciones=true;
		resultado.put("resultado", "ok");
		peticion.put("idInscrito", request.getParameter("idInscrito"));
		peticion.put("importe", Double.parseDouble(request.getParameter("importe")));
		if (!peticion.has("completado")) peticion.put("completado", false);
		boolean grupal=request.getParameter("grupal").equals("true");
		InscritoBean inscrito=SQLInscritos.selectIdInscrito(peticion.getString("idInscrito"));
		PagosBean pago=new PagosBean();
		pago.setEstado(1);
		pago.setPagoCompleto(peticion.getBoolean("completado"));
		pago.setObservaciones("Movimiento registrado para "+inscrito.getNombre()+" "+inscrito.getApellido());
		pago.setImporte(peticion.getDouble("importe"));
		pago.setFecha(System.currentTimeMillis());
		pago.setIdInscrito(peticion.getString("idInscrito"));
		pago.setIdPago(UUID.randomUUID().toString());
		operaciones=SQLPagos.registrarPago(pago);
		Logger.registrarActividadSession(request, "Registrando pago "+pago.getImporte()+" "+pago.getObservaciones()+".");
		if (!operaciones) 
			{
			resultado.put("resultado","Error al registrar el pago");
			}
		else
		{
			if (pago.isPagoCompleto())
			{
			operaciones=SQLInscritos.estadoInscritos(pago.getIdInscrito(), "4");
			if (!operaciones) 
				{
				resultado.put("resultado","Error al revisar estado de inscritos");
				}
			else
			{
				String idHabitacion=SQLInscritos.getHabitacion(pago.getIdInscrito());
				if (grupal)
				{
					operaciones=SQLHabitaciones.revisarEstadoHabitaciones(idHabitacion);//Comprobamos que todos los inscritos han pagado
					if (!operaciones) resultado.put("resultado","Error al revisar estado habitaciones");
				}
				else
				{
					operaciones=SQLHabitaciones.revisarEstadoHabitacionesParciales(idHabitacion);
					if (!operaciones) resultado.put("resultado","Error al revisar estado habitación individual");
				}
			}
			}
		}
		response.getWriter().print(resultado.toString());
	}

}
