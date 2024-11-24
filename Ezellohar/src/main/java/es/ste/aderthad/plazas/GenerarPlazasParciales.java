package es.ste.aderthad.plazas;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.HabitacionParcialBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLHabitaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GenerarPlazasParciales
 */
@WebServlet("/admin/plazas/GenerarPlazasParciales")
public class GenerarPlazasParciales extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GenerarPlazasParciales() {
        // TODO Auto-generated constructor stub
    }



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String listaPlazas=request.getParameter("plazas");
		String estado=request.getParameter("estado");
		StringBuilder sb=new StringBuilder();
		String idraiz="";
		HabitacionParcialBean parcial;
		JSONObject resultObj=new JSONObject();
		resultObj.put("resultado","ok");
		//Primero reservamos las habitaciones a "convertidas en plazas individuales"
		//Sólo se pueden convertir habitaciones en estado "Inicial" para evitar conflictos
		//Con habitaciones asignadas
		boolean resultado=SQLHabitaciones.fragmentarHabitaciones(listaPlazas, "9");
		if (!resultado) 
		{
			resultObj.put("resultado","error");
			sb.append("Error al cambiar el tipo de plaza (de normal a aleatoria)");
		}
		
		int capacidad;
		//Estado 9 convertido en plazas individuales
		//Después generamos registros de plazas individuales en la tabla correspondiente
		//Sólo de las que se cambiaron a estado 9
		JSONArray habitaciones=SQLHabitaciones.selectSubsetHabitaciones(listaPlazas);
		for (int h=0;h<habitaciones.length();h++)
		{
			if (habitaciones.getJSONObject(h).getInt("estado")==9)
			{
				capacidad=habitaciones.getJSONObject(h).getInt("plazas");
			idraiz=habitaciones.getJSONObject(h).getString("id");
			for (int c=0;c<capacidad;c++)
			{
				parcial=new HabitacionParcialBean();
				parcial.setIdHabitacionRaiz(idraiz);
				parcial.setIdHabitacion(idraiz+"-"+c);
				parcial.setEstado(Integer.valueOf(estado));
				//0 sin activar
				//1 disponible
				//2 bloqueada temp
				resultado=SQLHabitaciones.generarHabitacionParcial(parcial);
				if (!resultado) 
				{
					resultObj.put("resultado", "error");
					sb.append("error al generar habitación "+parcial.getIdHabitacion()+".\n");
				}
				else 
				{
					sb.append("Generada la plaza parcial "+parcial.getIdHabitacion()+".\n");
					Logger.registrarActividadSession(request, "Generando plaza parcial "+parcial.getIdHabitacion()+".");
				}
			}
		}
		}
		resultObj.put("mensaje", sb.toString());
		response.getWriter().print(resultObj.toString());

	}

}
