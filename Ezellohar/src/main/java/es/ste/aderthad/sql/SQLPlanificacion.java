package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.data.EspacioBean;
import es.ste.aderthad.data.PlanificacionBean;
import es.ste.aderthad.log.Logger;

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
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT * FROM PLANIFICACION WHERE IDPLANIFICACION='"+id+"'");
			
			if (rs.next())
			{
						resultado=true;
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	public static String select(String fecha,long hora,long intervalo)
	{
		JSONArray actividades=new JSONArray();
		JSONObject actividad;
		boolean resultado;
		JSONObject objEspacio;
		JSONObject espaciosEliminados=new JSONObject();
		boolean espaciosDesaparecidos=false;
		String idEspacio;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT ACTIVIDAD,NOMBRE_ACTIVIDAD,COLOR,ESPACIO FROM PLANIFICACION WHERE FECHA='"+fecha+"' AND (INTERVALO>="+hora+" AND INTERVALO<"+(hora+intervalo)+") ORDER BY ACTIVIDAD");
			
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
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
			if (espaciosDesaparecidos)
			{
				Logger.GenerarEntradaLogMensaje("Detectados espacios planificados inexistentes en el calendario.", Logger.getFileNameErrorLog());
				Logger.GenerarEntradaLogMensaje("Eliminando espacios de la planificación: \n"+espaciosEliminados.toString(), Logger.getFileNameErrorLog());
				Iterator<String> espacios = espaciosEliminados.keys();
				String espacio="";
				while (espacios.hasNext())
				{
					espacio=(String) espacios.next();
					resultado = limpiarPlanificacionEspacio(espacio);
					if (resultado)
					{
						Logger.GenerarEntradaLogMensaje("Eliminado espacio inexistente de la planificación: " +espacio, Logger.getFileNameActivityLog());
					}
					else
					{
						Logger.GenerarEntradaLogMensaje("No se pudo eliminar espacio: \n"+espacio, Logger.getFileNameErrorLog());
					}
				}
			}
		return actividades.toString();
	}
	public static PlanificacionBean select(String id)
	{
		PlanificacionBean resultado=null;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
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
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static JSONArray selectActividades()
	{
		JSONArray resultado=new JSONArray();
		try{
		Statement sentencia;
		Connection con=SQLConexion.getConexion();
		sentencia=con.createStatement();

		
		ResultSet rs=sentencia.executeQuery("SELECT DISTINCT(ACTIVIDAD) FROM PLANIFICACION");
		
		while (rs.next())
		{
			resultado.put(rs.getString(1));
			
		}
		if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
	}
	return resultado;
	}
	public static boolean insertActividad(PlanificacionBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
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
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
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
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static boolean limpiarPlanificacion() {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("DELETE FROM PLANIFICACION WHERE ACTIVIDAD = ''");
		
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static boolean limpiarPlanificacionActividades(String actividades) {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("DELETE FROM PLANIFICACION WHERE "+componerSeleccion(actividades));
		
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean limpiarPlanificacionEspacio(String idEspacio) {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("DELETE FROM PLANIFICACION WHERE ESPACIO='"+idEspacio+"'");
		
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean liberarPlanificacion() {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE PLANIFICACION SET ACTIVIDAD=''");
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static String getFechaMin()
	{
		String resultado="0";
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MIN(FECHA) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getFechaMax()
	{
		String resultado="0";
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MAX(FECHA) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getHoraMin()
	{
		String resultado="0";
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MIN(INTERVALO) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		try
		{
			Long.parseLong(resultado);
		}
		catch (Exception e)
		{
			resultado="0";
		}
		return resultado;
	}
	
	public static String getHoraMax()
	{
		String resultado="0";
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MAX(INTERVALO) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
			try
			{
				Long.parseLong(resultado);
			}
			catch (Exception e)
			{
				resultado="0";
			}
		return resultado;
	}
}
