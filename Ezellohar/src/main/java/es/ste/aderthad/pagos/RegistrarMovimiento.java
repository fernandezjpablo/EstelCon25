package es.ste.aderthad.pagos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.PagosBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.mensajeria.GenerarMensajeSaliente;
import es.ste.aderthad.procesos.Procesos;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import es.ste.aderthad.sql.SQLUsuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrarPago
 */
@WebServlet("/admin/pagos/RegistrarMovimiento")
public class RegistrarMovimiento extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegistrarMovimiento() {
        // TODO Auto-generated constructor stub
    }
    
    


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String importe=request.getParameter("importe");
		boolean operaciones=true;
		InscritoBean inscrito=null;
		Double importeVal=Double.valueOf(importe);
		String observaciones=request.getParameter("observaciones");
		String tipo=request.getParameter("tipo");
		JSONObject resultado=new JSONObject();
		resultado.put("resultado", "ok");
		String idInscrito="";
		if (tipo.equals("pago") || tipo.equals("devolucion")) importeVal*=-1;
		if (tipo.equals("inscripcion") || tipo.equals("devolucion"))
		{
			inscrito=SQLInscritos.selectUsuario(observaciones);
			if (inscrito!=null) {
				idInscrito=inscrito.getId();
			}
			else
			{
		
				operaciones=false;
			}
		}
		
		StringBuilder sb=new StringBuilder();
		
		if (operaciones)
			{
			PagosBean pago=new PagosBean();
			pago.setEstado(1);//Movimiento ejecutado
			pago.setPagoCompleto(false);
			pago.setObservaciones(tipo+":"+observaciones);
			pago.setImporte(importeVal);
			pago.setFecha(System.currentTimeMillis());
			pago.setIdInscrito(idInscrito);
			pago.setIdPago(UUID.randomUUID().toString());
			operaciones=SQLPagos.registrarPago(pago);
			if (operaciones)
			{
				
				
				Logger.registrarActividadSession(request, "Registrando movimiento de "+pago.getIdInscrito()+" por "+pago.getImporte()+" "+pago.getObservaciones()+".");
				if (tipo.equals("inscripcion"))
				{
				boolean resulMsg=Procesos.generarMensajeArchivo(Procesos.componerMensajeIngreso(pago));
					if (!resulMsg)
					{
						Logger.registrarActividadSession(request, "Error enviando correo de confirmación del pago de "+pago.getIdInscrito()+" por "+pago.getImporte()+" "+pago.getObservaciones()+".");
					}
				}
				else if (tipo.equals("devolucion"))
				{
				boolean resulMsg=Procesos.generarMensajeArchivo(Procesos.componerMensajeDevolucion(pago));
					if (!resulMsg)
					{
						Logger.registrarActividadSession(request, "Error enviando correo de confirmación de devolución de "+pago.getIdInscrito()+" por "+pago.getImporte()+" "+pago.getObservaciones()+".");
					}
				}
			}
			else
			{
				Logger.registrarActividadSession(request, "Error registrando pago de "+pago.getIdInscrito()+" por "+pago.getImporte()+" "+pago.getObservaciones()+".");
			}
			}
		
		if (!operaciones) 
			{
			resultado.put("resultado","Error al registrar el pago");
			if (inscrito==null)
			{
				resultado.put("resultado","No se ha encontrado el inscrito asociado a ese código.");
			}

			}
		else
		{
			resultado.put("resultado","Movimiento registrado");
		}
		response.getWriter().print(resultado.toString());
	}

}
