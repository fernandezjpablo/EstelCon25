package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.ActividadBean;
import es.ste.aderthad.inscritos.data.EspacioBean;
import es.ste.aderthad.inscritos.data.PlanificacionBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;

public class SQLPlanificacion {
	private static String componerSeleccion(String actividades)
	{
		String resultado=actividades.replaceAll(",","' OR ACTIVIDAD='");
		
		resultado= "(ACTIVIDAD='"+resultado+"')";
		return resultado;
	}
	public static boolean existe(String id)
	{
		boolean resultado=false;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT * FROM PLANIFICACION WHERE IDPLANIFICACION='"+id+"'");
			
			if (rs.next())
			{
						resultado=true;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static PlanificacionBean select(String id)
	{
		PlanificacionBean resultado=null;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT FECHA,ESPACIO,INTERVALO,ACTIVIDAD,NOMBRE_ACTIVIDAD,COLOR FROM PLANIFICACION WHERE IDPLANIFICACION='"+id+"'");
			
			if (rs.next())
			{
				resultado=new PlanificacionBean();
				resultado.setActividad(rs.getString(4));
				resultado.setFecha(rs.getString(1));
				resultado.setEspacio(rs.getString(2));
				resultado.setIntervalo(rs.getInt(3));
				resultado.setNombreActividad(rs.getString(5));
				resultado.setColor(rs.getString(6));
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public static JSONArray selectCalendarioPorEspacios()
	{
		String sentenciaStr="SELECT PLANIFICACION.FECHA,ESPACIOS.IDESPACIO,ESPACIOS.NOMBRE,INTERVALO,ACTIVIDAD.IDACTIVIDAD,ACTIVIDAD.NOMBRE,ACTIVIDAD.RESPONSABLES_PSEUDONIMOS,COLOR,ACTIVIDAD.DESCRIPCION,ACTIVIDAD.AFORO,ACTIVIDAD.PUBLICO FROM PLANIFICACION INNER JOIN ESPACIOS INNER JOIN ACTIVIDAD WHERE ESPACIO=IDESPACIO AND ACTIVIDAD=IDACTIVIDAD ORDER BY PLANIFICACION.FECHA,ESPACIO,INTERVALO";
		JSONArray resultado=new JSONArray();
		String fechaAnt,fechaAct;
		String espacioAnt,espacioAct;
		String nombreEspacio="";
		String nombreActividad="";
		String responsablesActividad="";
		String descripcion="";
		String publico="";
		int aforo=0;
		JSONObject planificacionEspacio;
		JSONObject intervalosEspacio;
		JSONObject intervaloEspacio;
		String color="";
		String intervalo,idActividad;
		try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery(sentenciaStr);
			fechaAnt="";
			espacioAnt="";
			espacioAct="";
			intervalosEspacio=new JSONObject();
			JSONObject planificacionDia=null;
			planificacionEspacio=null;
			fechaAct="";
			while (rs.next())
			{
				fechaAct=rs.getString(1);
				espacioAct=rs.getString(2);
				nombreEspacio=rs.getString(3);
				intervalo=rs.getString(4);
				idActividad=rs.getString(5);
				nombreActividad=rs.getString(6);
				responsablesActividad=rs.getString(7);
				color=rs.getString(8);
				descripcion=rs.getString(9);
				aforo=rs.getInt(10);
				publico=rs.getString(11);
				if (!fechaAnt.equals(fechaAct))
				{
					if (!"".equals(espacioAnt)) 
					{
						planificacionDia.put(espacioAnt, planificacionEspacio);
					}	
					if (planificacionDia!=null) 
						{
						resultado.put(planificacionDia);

						}

				}
				
				if (!espacioAnt.equals(espacioAct))
				{
					if (!"".equals(espacioAnt)) 
					{
					planificacionEspacio.put("intervalos", intervalosEspacio);
					planificacionDia.put(espacioAnt, planificacionEspacio);
					}
					planificacionEspacio=new JSONObject();
					planificacionEspacio.put("nombre", nombreEspacio);
					intervalosEspacio=new JSONObject();	
				}
				if (!fechaAnt.equals(fechaAct))
				{
				planificacionDia=new JSONObject();
				planificacionDia.put("fecha", fechaAct);
				}
				fechaAnt=fechaAct;
				intervaloEspacio=new JSONObject();
				intervaloEspacio.put("intervalo", intervalo);
				intervaloEspacio.put("actividad", idActividad);
				intervaloEspacio.put("publico", publico);
				intervaloEspacio.put("nombreActividad", nombreActividad);
				intervaloEspacio.put("ponente", responsablesActividad);
				intervaloEspacio.put("color", color);
				intervaloEspacio.put("aforo", aforo);
				intervaloEspacio.put("descripcion", descripcion);
				intervalosEspacio.put(String.valueOf(intervalo),intervaloEspacio);
				espacioAnt=espacioAct;
			}

				if (!"".equals(espacioAnt)) 
				{
				planificacionEspacio.put("intervalos", intervalosEspacio);
				planificacionDia.put(espacioAnt, planificacionEspacio);
				}
				planificacionEspacio=new JSONObject();
				planificacionEspacio.put("nombre", nombreEspacio);
				intervalosEspacio=new JSONObject();	
				espacioAnt=espacioAct;

			if (planificacionDia!=null) resultado.put(planificacionDia);
			planificacionDia=new JSONObject();
			planificacionDia.put("fecha", fechaAct);
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String select(String fecha,long hora,long intervalo)
	{
		JSONArray actividades=new JSONArray();
		JSONObject actividad;
		JSONObject objEspacio;
		JSONObject espaciosEliminados=new JSONObject();
		boolean espaciosDesaparecidos=false;
		String idEspacio;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT ACTIVIDAD,ACTIVIDAD.NOMBRE,COLOR,ESPACIO,AFORO,RESPONSABLES_PSEUDONIMOS,DESCRIPCION FROM PLANIFICACION INNER JOIN ACTIVIDAD WHERE PLANIFICACION.ACTIVIDAD=ACTIVIDAD.IDACTIVIDAD AND FECHA='"+fecha+"' AND (INTERVALO>="+hora+" AND INTERVALO<"+(hora+intervalo)+")");
			
			while (rs.next())
			{
				actividad=new JSONObject();
				actividad.put("nombre",rs.getString(2));
				actividad.put("id",rs.getString(1));
				actividad.put("color",rs.getString(3));
				idEspacio=rs.getString(4);
				objEspacio=SQLEspacios.consultarEspacio(idEspacio);
				if (!objEspacio.has("nombreEspacio"))
				{
					//El espacio no existe, hay que eliminarlo de la planificación
					espaciosEliminados.put(idEspacio, true);
					espaciosDesaparecidos=true;
				}
				else
				{
					actividad.put("espacio", objEspacio.getString("nombreEspacio"));
					actividades.put(actividad);
				}
				actividad.put("aforo", rs.getString(5));
				actividad.put("responsable", rs.getString(6));
				actividad.put("descripcion", rs.getString(7));
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
			if (espaciosDesaparecidos)
			{
				LoggerInscritos.GenerarEntradaLogMensaje("Detectados espacios planificados inexistentes en el calendario: \n"+espaciosEliminados.toString(), LoggerInscritos.getFileNameErrorLog());
			}
		return actividades.toString();
	}
	
	
	
	public static String getFechaMin()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MIN(FECHA) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getFechaMax()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MAX(FECHA) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getHoraMin()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MIN(INTERVALO) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getHoraMax()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MAX(INTERVALO) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static JSONObject getSimultaneas(JSONObject actividad)
	{
		JSONObject resultado=null;
		JSONObject lista=new JSONObject();
		try{
		Statement sentencia;
		Connection con=SQLConexionInscritos.getConexion();
		sentencia=con.createStatement();			
		ResultSet rs=sentencia.executeQuery("SELECT DISTINCT ACTIVIDAD,FECHA,NOMBRE FROM PLANIFICACION INNER JOIN ACTIVIDAD WHERE FECHA='"+actividad.getString("fecha")+"' AND INTERVALO<="+actividad.getInt("max")+" AND INTERVALO>="+actividad.getInt("min")+" AND ACTIVIDAD=IDACTIVIDAD AND ACTIVIDAD<>'"+actividad.getString("actividad")+"'");
		
		while (rs.next())
		{
			resultado=new JSONObject();
			resultado.put("fecha", rs.getString(2));
			resultado.put("actividad",rs.getString(1));
			resultado.put("nombre",rs.getString(3));
			lista.put(rs.getString(1), resultado);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
	}
		return lista;
	}
	
	public static JSONObject getIntervaloActividad(String idactividad)
	{
		JSONObject resultado=new JSONObject();
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();			
			ResultSet rs=sentencia.executeQuery("SELECT FECHA,MAX(INTERVALO),MIN(INTERVALO),ACTIVIDAD FROM PLANIFICACION WHERE ACTIVIDAD='"+idactividad+"' GROUP BY ACTIVIDAD");
			
			if (rs.next())
			{
				resultado.put("fecha", rs.getString(1));
				resultado.put("max", rs.getInt(2));
				resultado.put("min", rs.getInt(3));
				resultado.put("actividad",rs.getString(4));
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static JSONArray selectActividades()
	{
		JSONArray resultado=new JSONArray();
		try{
		Statement sentencia;
		Connection con=SQLConexionInscritos.getConexion();
		sentencia=con.createStatement();

		
		ResultSet rs=sentencia.executeQuery("SELECT DISTINCT(ACTIVIDAD) FROM PLANIFICACION");
		
		while (rs.next())
		{
			resultado.put(rs.getString(1));
			
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
	}
	return resultado;
	}
	

	public static boolean insertActividad(PlanificacionBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("INSERT INTO PLANIFICACION (IDPLANIFICACION,FECHA,ESPACIO,INTERVALO,ACTIVIDAD,COLOR,FECHA_CREACION,FECHA_UPDATE,NOMBRE_ACTIVIDAD)"
					+ " VALUES (?,?,?,?,?,?,?,?,?)");
			sentencia.setString(1,bean.getIdPlanificacion());
			sentencia.setString(2,bean.getFecha());
			sentencia.setString(3, bean.getEspacio());
			sentencia.setInt(4, bean.getIntervalo());
			sentencia.setString(5, bean.getActividad());
			sentencia.setString(6, bean.getColor());
			sentencia.setLong(7,System.currentTimeMillis());
			sentencia.setLong(8,System.currentTimeMillis());
			sentencia.setString(9,bean.getNombreActividad());
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateActividad(PlanificacionBean objeto)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE PLANIFICACION SET FECHA=?,ESPACIO=?,INTERVALO=?,ACTIVIDAD=?,COLOR=?,FECHA_UPDATE=?,NOMBRE_ACTIVIDAD=? WHERE IDPLANIFICACION=?");
			sentencia.setString(1,objeto.getFecha());
			sentencia.setString(2,objeto.getEspacio());
			sentencia.setInt(3,objeto.getIntervalo());
			sentencia.setString(4, objeto.getActividad());
			sentencia.setString(5, objeto.getColor());
			sentencia.setLong(6,System.currentTimeMillis());
			sentencia.setString(7,objeto.getNombreActividad());
			sentencia.setString(8,objeto.getIdPlanificacion());
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static boolean limpiarPlanificacion() {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("DELETE FROM PLANIFICACION WHERE ACTIVIDAD = ''");
		
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static boolean limpiarPlanificacionActividades(String actividades) {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("DELETE FROM PLANIFICACION WHERE "+componerSeleccion(actividades));
		
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean liberarPlanificacion() {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE PLANIFICACION SET ACTIVIDAD=''");
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
}
