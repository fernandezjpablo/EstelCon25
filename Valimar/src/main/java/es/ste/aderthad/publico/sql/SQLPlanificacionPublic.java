package es.ste.aderthad.publico.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.publico.log.LoggerPublic;


public class SQLPlanificacionPublic {
	
	private static String consultarEspacio(String id)
	{
		String nombreEspacio="";
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT NOMBRE FROM ESPACIOS WHERE IDESPACIO='"+id+"'");
			
			if (rs.next())
			{
				nombreEspacio=rs.getString(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		return nombreEspacio;
	}
	
	public static String select(String fecha,long hora,long intervalo,String espacio)
	{
		JSONArray actividades=new JSONArray();
		JSONObject actividad;
		JSONObject objEspacio;
		JSONObject espaciosEliminados=new JSONObject();
		boolean espaciosDesaparecidos=false;
		String nombreEspacio;
		String idEspacio;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT ACTIVIDAD,NOMBRE_ACTIVIDAD,COLOR,ESPACIO FROM PLANIFICACION WHERE FECHA='"+fecha+"' AND (INTERVALO>="+hora+" AND INTERVALO<"+(hora+intervalo)+")");
			
			while (rs.next())
			{
				actividad=new JSONObject();
				actividad.put("nombre",rs.getString(2));
				actividad.put("id",rs.getString(1));
				actividad.put("color",rs.getString(3));
				idEspacio=rs.getString(4);
				nombreEspacio=consultarEspacio(idEspacio);
				if (nombreEspacio.toLowerCase().indexOf(espacio.toLowerCase())>=0)
				{
					actividad.put("espacio", nombreEspacio);
					actividades.put(actividad);
				}
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
			if (espaciosDesaparecidos)
			{
				LoggerPublic.GenerarEntradaLogMensaje("Detectados espacios planificados inexistentes en el calendario: \n"+espaciosEliminados.toString(), LoggerPublic.getFileNameErrorLog());
			}
		return actividades.toString();
	}
	
	public static String getFechaMin()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MIN(FECHA) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getFechaMax()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MAX(FECHA) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getHoraMin()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MIN(INTERVALO) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String getHoraMax()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT MAX(INTERVALO) FROM PLANIFICACION");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
				
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultado;
	}
}
