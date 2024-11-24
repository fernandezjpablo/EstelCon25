package es.ste.aderthad.pagos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Servlet implementation class EstadoMovimientos
 */
@WebServlet("/admin/pagos/EstadoMovimientos")
public class EstadoMovimientos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EstadoMovimientos() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tarea=request.getParameter("accion");
		String movimientosStr=request.getParameter("items");
		JSONObject resultado=new JSONObject();
		StringBuilder sb=new StringBuilder();
		boolean operaciones=true;
		resultado.put("resultado", "ok");
		if (tarea.equals("anular"))
		{
			operaciones=SQLPagos.anularMovimientos(movimientosStr);
		}
		else if (tarea.equals("recuperar"))
		{
			operaciones=SQLPagos.recuperarIngresos(movimientosStr);
			operaciones=SQLPagos.recuperarPagos(movimientosStr);
		}
		else if (tarea.equals("devoluciones"))
		{
			operaciones=SQLPagos.confirmarDevoluciones(movimientosStr);
		}
		Logger.registrarActividadSession(request, "Actualizando datos de pagos: "+tarea+" sobre "+movimientosStr+".");
		if (!operaciones) 
			{
			resultado.put("resultado","Error al actualizar movimientos");
			}
		else
		{
			resultado.put("resultado","Movimientos actualizados");
		}
		response.getWriter().print(resultado.toString());
	}


}
