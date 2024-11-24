package es.ste.aderthad.sql;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.HabitacionParcialBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;

public class SQLHabitaciones {
	
	public static void CambiarNombre(String id,String identificador) throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET IDENTIFICADOR='"+identificador+"' WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();
	}
	
	public static void LimpiarNombres() throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET IDENTIFICADOR=''");
			if (con!=null) con.close();
	}	
	
	public static void CambiarNombres(JSONArray correspondencias) throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			for (int i=0;i<correspondencias.length();i++)
			{
			sentencia.executeUpdate("UPDATE HABITACIONES SET IDENTIFICADOR='"+correspondencias.getJSONObject(i).getString("nombre")+"',PLANTA='"+correspondencias.getJSONObject(i).getString("planta")+"' WHERE IDHABITACION='"+correspondencias.getJSONObject(i).getString("id")+"'");
			}
			if (con!=null) con.close();
	}
	
	public static boolean revisarEstadoHabitaciones(String habitacion)
	{
		boolean resultado=true;
			HabitacionBean hab=selectHabitacion(habitacion);
		int pagados=SQLInscritos.contarEstadoPagados(habitacion, 4);
		int estado=hab.getEstado();
		
		/*Consultamos cuántos de sus inscritos han pagado y comparamos con las plazas
		 * si pagados == plazas entonces el estado cambia a "Pagado 4", si no "Pendiente 3"*/
		if (pagados>= hab.getPlazas())
		{
			estado=4;
		}
		else
		{
			if (estado==4) estado=3;//Si estaba como pagada y ahora hay más plazas (sin pagar) la ponemos en estado 3, si no mantenemos el estado previo que tuviera
		}
			try{
			PreparedStatement sentencia;
			if (!habitacion.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO=? WHERE IDHABITACION=?");
				sentencia.setInt(1, estado);
				sentencia.setString(2, habitacion);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		return resultado;
	}
	
	
	public static boolean revisarEstadoHabitacionesParciales(String habitacion)
	{
		boolean resultado=true;
		HabitacionParcialBean plaza=selectHabitacionParcial(habitacion);
		if (plaza!=null)
		{
			int pagados=SQLInscritos.contarEstadoPagados(habitacion, 4);
		int estado=plaza.getEstado();
		
		/*Consultamos cuántos de sus inscritos han pagado y comparamos con las plazas
		 * si pagados == plazas entonces el estado cambia a "Pagado 4", si no "Pendiente 3"*/
		if (pagados>= 1)
		{
			estado=4;
		}
		else
		{
			estado=3;//Si estaba como pagada pero no hay ningún inscrito-pagado en ella
		}
			try{
			PreparedStatement sentencia;
			if (!habitacion.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONESPARCIALES SET ESTADO=? WHERE IDHABITACION=?");
				sentencia.setInt(1, estado);
				sentencia.setString(2, habitacion);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		}
		return resultado;
	}
	
	public static boolean estadoHabitaciones(String habitacion,int estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitacion.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO=? where IDHABITACION=?");
				sentencia.setInt(1, estado);
				sentencia.setString(2, habitacion);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean parcialHabitaciones(String habitacion,int parcial)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitacion.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ALEATORIA=? where IDHABITACION=?");
				sentencia.setInt(1, parcial);
				sentencia.setString(2, habitacion);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	public static void CambiarObservaciones(String id,String observaciones) throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET OBSERVACIONES='"+observaciones+"' WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();
	}
	
	public static void CambiarPlanta(String id,String planta) throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			sentencia.executeUpdate("UPDATE HABITACIONES SET PLANTA='"+planta+"' WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();
	}
	
	public static void CambiarCamas(String id,String camas) throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			sentencia.executeUpdate("UPDATE HABITACIONES SET CAMAS='"+camas+"' WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();
	}
	
	public static void CambiarPlazas(String id,String plazas) throws SQLException, ClassNotFoundException
	{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			sentencia.executeUpdate("UPDATE HABITACIONES SET PLAZAS='"+plazas+"' WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();
	}
	
	public static void LimpiarBloqueos()
	{
		Connection con=null;
			try{
				long tiempo=System.currentTimeMillis();

			Statement sentencia;
			con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-Integer.valueOf(Entorno.getVariable("TIEMPO_RESERVA"))));

			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}

		
	}
	
	public static void LimpiarBloqueosParciales()
	{
			try{
				long tiempo=System.currentTimeMillis();

			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONESPARCIALES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-Integer.valueOf(Entorno.getVariable("TIEMPO_RESERVA"))));

			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		
	}
	
	public static String BloqueoHabitacion(String capacidad,String camas)
	{
		String resultado="";
			try{
				long tiempo=System.currentTimeMillis();
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			/*Primero desbloqueamos las caducadas*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET ESTADO=1, HORA_BLOQUEO=NULL WHERE ESTADO=2 AND HORA_BLOQUEO<"+(tiempo-Integer.valueOf(Entorno.getVariable("TIEMPO_RESERVA"))));
			

			/*Primero bloqueamos con el timestamp*/
			sentencia.executeUpdate("UPDATE HABITACIONES SET ESTADO=2, HORA_BLOQUEO="+tiempo+" WHERE ESTADO=1 AND PLAZAS="+capacidad+" AND CAMAS="+camas+" LIMIT 1");
			
			/*Después obtenemos el id de la que hemos marcado con ese timestamp*/
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION from HABITACIONES WHERE hora_bloqueo="+tiempo);
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			return null;
		}
		
		return resultado;
	}
	private static String componerSeleccion(String habitaciones)
	{
		String resultado=habitaciones.replaceAll(",","' OR IDHABITACION='");
		
		resultado= "(IDHABITACION='"+resultado+"')";
		return resultado;
	}
	
	public static boolean eliminarHabitaciones(String habitaciones)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("DELETE FROM HABITACIONES WHERE ESTADO=8 AND "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean eliminarHabitacionesParciales(String raiz)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!raiz.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("DELETE FROM HABITACIONESPARCIALES WHERE IDHABITACIONRAIZ='"+raiz+"'");
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean estadoHabitaciones(String habitaciones,String estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO="+estado+" WHERE "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean habilitarHabitaciones(String habitaciones)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO=1 WHERE (ESTADO=0) AND "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean deshabilitarHabitaciones(String habitaciones)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO=0 WHERE ESTADO<>3 AND ESTADO<>4 AND "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean fragmentarHabitaciones(String habitaciones,String estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONES SET ESTADO="+estado+" WHERE ESTADO=0 AND "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean estadoHabitacionesParciales(String habitaciones,String estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONESPARCIALES SET ESTADO="+estado+" WHERE "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean habilitarHabitacionesParciales(String habitaciones)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONESPARCIALES SET ESTADO=1 WHERE ESTADO=0 AND "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=false;
		}
		
		return resultado;
	}
	
	public static boolean deshabilitarHabitacionesParciales(String habitaciones)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!habitaciones.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE HABITACIONESPARCIALES SET ESTADO=0 WHERE ESTADO<>3 AND ESTADO<>4 AND "+componerSeleccion(habitaciones));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static int checkPlazasTotales()
	{
		int resultado=0;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES");
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(PLAZAS) FROM HABITACIONES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado=rs.getInt(1);
			}
			rs=sentencia.executeQuery("SELECT COUNT(*) from HABITACIONESPARCIALES WHERE ESTADO="+estado);
			if (rs.next())
			{
				resultado+=rs.getInt(1);
			}
			if (con!=null) con.close();			
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con=SQLConexion.getConexion();
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
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		
		return resultados;
	}

	public static JSONArray selectSubsetHabitaciones(String lista)
	{
		JSONArray resultados=new JSONArray();
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES WHERE "+componerSeleccion(lista));
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				resultados.put(new JSONObject(habitacion.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES ORDER BY "+orden);
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				habitacion.setOcupacion(SQLInscritos.selectInscritos(habitacion.getId()).length());
				resultados.put(new JSONObject(habitacion.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	
	public static JSONArray selectHabitacionesOcupantes()
	{
		JSONArray resultados=new JSONArray();
		JSONObject habitacionObj;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES ORDER BY PLANTA ASC, IDENTIFICADOR ASC");
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				habitacionObj=new JSONObject(habitacion.toJson());
				habitacionObj.put("ocupantes", SQLInscritos.selectOcupantesInscritos(habitacion.getId()));
				resultados.put(habitacionObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static JSONArray selectHabitaciones()
	{
		JSONArray resultados=new JSONArray();
		HabitacionBean habitacion;
		JSONObject habitacionObj;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES ORDER BY PLANTA ASC, ESTADO ASC");
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				habitacion.setOcupacion(SQLInscritos.selectInscritos(habitacion.getId()).length());
				habitacionObj=new JSONObject(habitacion.toJson());
				habitacionObj.put("ocupantes", new JSONArray());
				if (habitacion.getOcupacion()>0)
				{
								habitacionObj.put("ocupantes", SQLInscritos.ocupantesHabitacion(habitacion.getId()));
				}

				resultados.put(habitacionObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray selectHabitacionesDistribuidas()
	{
		JSONArray resultados=new JSONArray();
		HabitacionBean habitacion;
		JSONObject habitacionObj;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES ORDER BY PLANTA ASC, IDENTIFICADOR ASC, ESTADO ASC");
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				habitacion.setOcupacion(SQLInscritos.selectInscritos(habitacion.getId()).length());
				habitacionObj=new JSONObject(habitacion.toJson());
				habitacionObj.put("ocupantes", new JSONArray());
				if (habitacion.getOcupacion()>0)
				{
								habitacionObj.put("ocupantes", SQLInscritos.ocupantesHabitacion(habitacion.getId()));
				}

				resultados.put(habitacionObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	public static JSONArray selectHabitacionesDisponibles()
	{
		JSONArray resultados=new JSONArray();
		HabitacionBean habitacion;
		JSONObject habitacionObj;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,ESTADO,OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES");
			
			while (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
				habitacion.setOcupacion(SQLInscritos.selectInscritos(habitacion.getId()).length());
				habitacionObj=new JSONObject(habitacion.toJson());
				habitacionObj.put("ocupantes", new JSONArray());
				if (habitacion.getOcupacion()>0)
				{
								habitacionObj.put("ocupantes", SQLInscritos.ocupantesHabitacion(habitacion.getId()));
				}
				if (habitacion.getPlazas()>habitacion.getOcupacion())
				{
					resultados.put(habitacionObj);
				}
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray selectHabitacionesParciales(String raiz)
	{
		JSONArray resultados=new JSONArray();
		HabitacionParcialBean habitacion;
		JSONObject habitacionParcialObj;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDHABITACIONRAIZ,ESTADO FROM HABITACIONESPARCIALES WHERE IDHABITACIONRAIZ='"+raiz+"' ORDER BY IDHABITACIONRAIZ ASC, IDHABITACION ASC");
			
			while (rs.next())
			{
				habitacion=new HabitacionParcialBean(rs.getString(1),rs.getString(2),rs.getInt(3));
				habitacionParcialObj=new JSONObject(habitacion.toJson());
				habitacionParcialObj.put("ocupantes", new JSONArray());
				if (habitacion.getEstado()==4 || habitacion.getEstado()==3)
				{
					habitacionParcialObj.put("ocupantes", SQLInscritos.ocupantesHabitacion(habitacion.getIdHabitacion()));
				}
				resultados.put(habitacionParcialObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray selectHabitacionesParciales()
	{
		JSONArray resultados=new JSONArray();
		HabitacionParcialBean habitacion;
		JSONObject habitacionParcialObj;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDHABITACIONRAIZ,ESTADO FROM HABITACIONESPARCIALES ORDER BY IDHABITACIONRAIZ ASC, IDHABITACION ASC");
			
			while (rs.next())
			{
				habitacion=new HabitacionParcialBean(rs.getString(1),rs.getString(2),rs.getInt(3));
				habitacionParcialObj=new JSONObject(habitacion.toJson());
				habitacionParcialObj.put("ocupantes", new JSONArray());
				if (habitacion.getEstado()==4 || habitacion.getEstado()==3)
				{
					habitacionParcialObj.put("ocupantes", SQLInscritos.ocupantesHabitacion(habitacion.getIdHabitacion()));
				}
				resultados.put(habitacionParcialObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	public static boolean generarHabitacionParcial(HabitacionParcialBean hab)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO HABITACIONESPARCIALES (IDHABITACION,IDHABITACIONRAIZ,FECHA,HORA_BLOQUEO,ESTADO) VALUES (?,?,?,0,?)");
			sentencia.setString(1, hab.getIdHabitacion());
			sentencia.setString(2, hab.getIdHabitacionRaiz());
			sentencia.setLong(3, System.currentTimeMillis());
			sentencia.setLong(4, hab.getEstado());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static boolean generarHabitaciones(int total,String planta,int capacidad,int camas,double precioA,double precioM)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO HABITACIONES (IDHABITACION,IDENTIFICADOR,PLAZAS,PLANTA,OBSERVACIONES,ESTADO, CAMAS,ALEATORIA,OCUPACION,PRECIO_ADULTO,PRECIO_MENOR) VALUES (?,'',?,?,'',0,?,0,0,?,?)");

			for (int i=0;i<total;i++)
			{
				sentencia.setString(1, UUID.randomUUID().toString());
				sentencia.setInt(2, capacidad);
				sentencia.setString(3, planta);
				sentencia.setInt(4, camas);
				sentencia.setDouble(5, precioA);
				sentencia.setDouble(6, precioM);
				sentencia.executeUpdate();
			}
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static HabitacionParcialBean selectHabitacionParcial(String idh) {
		HabitacionParcialBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
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
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			habitacion=null;
		}
		return habitacion;
	}
	
	public static HabitacionBean selectHabitacion(String idh) {
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
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
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			habitacion=null;
		}
		return habitacion;
	}



	public static void CambiarPrecioAdulto(String id, String precio) throws SQLException, ClassNotFoundException {
		Statement sentencia;
		Connection con;

			con = SQLConexion.getConexion();
			sentencia=con.createStatement();
			sentencia.executeUpdate("UPDATE HABITACIONES SET PRECIO_ADULTO="+precio+" WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();

	}
	
	public static void CambiarPrecioMenor(String id, String precio) throws SQLException, ClassNotFoundException {
		Statement sentencia;
		Connection con;

			con = SQLConexion.getConexion();
			sentencia=con.createStatement();
			sentencia.executeUpdate("UPDATE HABITACIONES SET PRECIO_MENOR="+precio+" WHERE IDHABITACION='"+id+"'");
			if (con!=null) con.close();

	}
}
