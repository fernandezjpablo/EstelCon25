package es.ste.aderthad.publico.sql;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;


import es.ste.aderthad.publico.data.HabitacionBean;
import es.ste.aderthad.publico.data.HabitacionParcialBean;
import es.ste.aderthad.publico.log.LoggerPublic;
import es.ste.aderthad.publico.properties.EntornoPublic;



public class SQLHabitacionesPublic {
	

	
	public static void LimpiarBloqueos()
	{
		Connection con=null;
			try{
				long tiempo=System.currentTimeMillis();

			Statement sentencia;
			con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-Integer.valueOf(EntornoPublic.getVariable("TIEMPO_RESERVA"))));

			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}

		
	}
	
	public static void LimpiarBloqueosParciales()
	{
			try{
				long tiempo=System.currentTimeMillis();

			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONESPARCIALES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-Integer.valueOf(EntornoPublic.getVariable("TIEMPO_RESERVA"))));

			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean estadoHabitaciones(String habitacion,int estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitacion.equals(""))
			{
				habitacion=habitacion.replace("\n", "");
				Connection con=SQLConexionPublic.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO=? WHERE IDHABITACION=?");
				sentencia.setInt(1, estado);
				sentencia.setString(2, habitacion);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean estadoHabitacionesParciales(String habitacion,int estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitacion.equals(""))
			{
				Connection con=SQLConexionPublic.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONESPARCIALES SET ESTADO=? WHERE IDHABITACION=?");
				sentencia.setInt(1, estado);
				sentencia.setString(2, habitacion);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	public static String BloqueoHabitacion(String capacidad,String camas)
	{
		String resultado="";
			try{
				long tiempo=System.currentTimeMillis();
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-600000));
			

			/*Primero bloqueamos con el timestamp*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET ESTADO=2, HORA_BLOQUEO="+tiempo+" WHERE ESTADO=1 AND PLAZAS="+capacidad+" AND CAMAS="+camas+" LIMIT 1");
			
			/*Después obtenemos el id de la que hemos marcado con ese timestamp*/
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION from HABITACIONES WHERE HORA_BLOQUEO="+tiempo);
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (con!=null) con.close();

		}  catch (SQLException e) {
			return null;
		}
		
		return resultado;
	}
	
	public static String BloqueoHabitacionParcial()
	{
		String resultado="";
			try{
				long tiempo=System.currentTimeMillis();
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONESPARCIALES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-600000));
			

			/*Primero bloqueamos con el timestamp*/
			sentencia.executeUpdate("UPDATE HABITACIONESPARCIALES SET ESTADO=2, HORA_BLOQUEO="+tiempo+" WHERE ESTADO=1 LIMIT 1");
			
			/*Después obtenemos el id de la que hemos marcado con ese timestamp*/
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION from HABITACIONESPARCIALES WHERE HORA_BLOQUEO="+tiempo);
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			return null;
		}
		
		return resultado;
	}


	

	public static int checkPlazasTotales()
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) from HABITACIONES");
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public static int checkPlazasTotalesDisponibles()
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES WHERE ESTADO=1");
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			
			rs=sentencia.executeQuery("SELECT COUNT(*) FROM HABITACIONESPARCIALES WHERE ESTADO=1");
			if (rs.next())
			{
				resultado+=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public static int checkPlazasEstadoFull(int estado)
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			

			
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES WHERE ESTADO="+estado);
			
			
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			rs=sentencia.executeQuery("SELECT COUNT(*) FROM HABITACIONESPARCIALES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado+=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public static int checkPlazasReservadasTotales()
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			int estado=3;
			//Lo cambiamos contabilizar los pagos registrados
			
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES WHERE ESTADO="+estado);
			
			
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			rs=sentencia.executeQuery("SELECT COUNT(*) FROM HABITACIONESPARCIALES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado+=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String factor=EntornoPublic.getVariable("FACTOR_CORRECTOR_RESERVAS");
		int factorInt=0;
		try
		{
			factorInt=Integer.parseInt(factor);
		}
		catch (Exception e)
		{
			factorInt=0;
		}
		return resultado-factorInt;
	}
	
	
	public static int checkPlazasPagadas()
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			//Lo cambiamos contabilizar los pagos registrados
			
			
			//ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES WHERE ESTADO="+estado);
			
			
			ResultSet rs=sentencia.executeQuery("select count(distinct observaciones) from pagos where observaciones like 'inscripcion:%'");
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			//rs=sentencia.executeQuery("SELECT COUNT(*) FROM HABITACIONESPARCIALES WHERE ESTADO="+estado);
			//if (rs.next())
			//{
			//	resultado+=rs.getInt(1);
			//}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	public static int checkPlazasEstado(int estado)
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			/*
			rs=sentencia.executeQuery("select count(*) from HABITACIONESPARCIALES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado+=rs.getInt(1);
			}
			*/
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public static int checkPlazasIndividualesEstado(int estado)
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM HABITACIONESPARCIALES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	

	public static JSONArray selectComboHabitaciones()
	{
		JSONArray resultados=new JSONArray();
		JSONObject entrada;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT DISTINCT PLAZAS, COUNT(*),CAMAS FROM HABITACIONES WHERE ESTADO=1 GROUP BY PLAZAS,CAMAS");
			
			while (rs.next())
			{
				entrada=new JSONObject();
				entrada.put("capacidad", rs.getInt(1));
				entrada.put("habitaciones", rs.getInt(2));
				entrada.put("camas",rs.getInt(3));
				resultados.put(entrada);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}

	public static JSONArray selectHabitaciones(String orden)
	{
		JSONArray resultados=new JSONArray();
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES ORDER BY "+orden);
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				resultados.put(new JSONObject(habitacion.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static JSONArray selectHabitaciones()
	{
		JSONArray resultados=new JSONArray();
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES ORDER BY PLANTA ASC, ESTADO ASC");
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				resultados.put(new JSONObject(habitacion.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static HabitacionBean selectHabitacion(String idh) {
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,HABITACIONES.ESTADO,HABITACIONES.OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES  WHERE IDHABITACION='"+idh+"' ");
			
			if (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
			}
			else
			{
				habitacion=null;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
			habitacion=null;
		}
		return habitacion;
	}
	
	public static HabitacionParcialBean selectHabitacionParcial(String idh) {
		es.ste.aderthad.publico.data.HabitacionParcialBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDHABITACIONRAIZ,ESTADO FROM HABITACIONESPARCIALES  WHERE IDHABITACION='"+idh+"' ");
			
			if (rs.next())
			{
				habitacion=new HabitacionParcialBean(rs.getString(1),rs.getString(2),rs.getInt(3));
			}
			else
			{
				habitacion=null;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
			habitacion=null;
		}
		return habitacion;
	}
}
