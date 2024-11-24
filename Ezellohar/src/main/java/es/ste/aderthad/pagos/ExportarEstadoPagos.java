package es.ste.aderthad.pagos;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLInscritos;

import org.json.JSONObject;

import es.ste.aderthad.sql.SQLInscritos;

/**
 * Servlet implementation class ExportarInscritos
 */
@WebServlet("/admin/pagos/ExportarEstadoPagos")
public class ExportarEstadoPagos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ExportarEstadoPagos() {
        // TODO Auto-generated constructor stub
    }

    private String formatearListado(JSONObject inscritos)
    {
		StringBuilder sb=new StringBuilder();
		JSONArray alojados=new JSONArray(inscritos.getString("alojados"));
		JSONArray desalojados=new JSONArray(inscritos.getString("desalojados"));
		JSONArray listaespera=new JSONArray(inscritos.getString("espera"));
		JSONObject objInscrito;
		SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sb.append("APELLIDOS;NOMBRE;NIF;MENOR;TIPO HABITACION;IMPORTE PLAZA;PENDIENTE DE PAGO;HABITACION;FECHA INSCRIPCIÓN;\n");
		for (int a=0;a<alojados.length();a++)
		{
			objInscrito=alojados.getJSONObject(a);
			sb.append(objInscrito.getString("apellidos")+";");
			sb.append(objInscrito.getString("nombre")+";");
			sb.append(objInscrito.getString("nif")+";");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("Menor de 14 años;");
			}
			else
			{
				sb.append("Adulto;");
			}
			sb.append(objInscrito.getString("tipoHabitacion")+";");
			sb.append(objInscrito.getString("importePlaza")+";");
			sb.append(objInscrito.getString("estadoPagos")+";");
			sb.append(objInscrito.getString("habitacion")+";");
			sb.append(df.format(new Date(objInscrito.getLong("fecha")))+";");
			sb.append("\n");
		}
		
		
		
		
		sb.append("DESALOJADOS\n");
		sb.append("APELLIDOS;NOMBRE;NIF;MENOR;TIPO HABITACION;IMPORTE PLAZA;HABITACION;FECHA INSCRIPCIÓN;\n");
		for (int a=0;a<desalojados.length();a++)
		{
			objInscrito=desalojados.getJSONObject(a);
			sb.append(objInscrito.getString("apellidos")+";");
			sb.append(objInscrito.getString("nombre")+";");
			sb.append(objInscrito.getString("nif")+";");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("Menor de 14 años;");
			}
			else
			{
				sb.append("Adulto;");
			}
			sb.append(objInscrito.getString("tipoHabitacion")+";");
			sb.append(objInscrito.getString("importePlaza")+";");
			//sb.append(objInscrito.getString("estadoPagos")+";");
			sb.append(objInscrito.getString("habitacion")+";");
			sb.append(df.format(new Date(objInscrito.getLong("fecha")))+";");
			sb.append("\n");
		}
		
		
		sb.append("EN LISTA DE ESPERA\n");
		sb.append("APELLIDOS;NOMBRE;NIF;MENOR;TIPO HABITACION;IMPORTE PLAZA;HABITACION;FECHA INSCRIPCIÓN;\n");
		for (int a=0;a<listaespera.length();a++)
		{
			objInscrito=listaespera.getJSONObject(a);
			sb.append(objInscrito.getString("apellidos")+";");
			sb.append(objInscrito.getString("nombre")+";");
			sb.append(objInscrito.getString("nif")+";");
			if (objInscrito.getBoolean("menor"))
			{
				sb.append("Menor de 14 años;");
			}
			else
			{
				sb.append("Adulto;");
			}
			sb.append(objInscrito.getString("tipoHabitacion")+";");
			sb.append(objInscrito.getString("importePlaza")+";");
			//sb.append(objInscrito.getString("estadoPagos")+";");
			sb.append(objInscrito.getString("habitacion")+";");
			sb.append(df.format(new Date(objInscrito.getLong("fecha")))+";");
			sb.append("\n");
		}
		return sb.toString();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject inscritos=new JSONObject();
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		inscritos.put("alojados",SQLInscritos.selectInscritos().toString() );
		inscritos.put("desalojados",SQLInscritos.selectInscritosSinHabitacion().toString() );
		inscritos.put("espera",SQLInscritos.selectInscritosListaEspera().toString() );
		Logger.registrarActividadSession(request, "Exportando listado de estado de pagos de inscritos a excel.");
		response.setCharacterEncoding("ISO-8859-15");
		response.setHeader("Content-Disposition", "attachment; filename=\"estado-pagos-inscripciones-estelcon"+df.format(new Date())+".csv\"");
		response.getWriter().append(formatearListado(inscritos));
	}


}
