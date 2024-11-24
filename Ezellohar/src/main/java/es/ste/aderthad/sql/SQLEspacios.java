package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.EspacioBean;
import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.log.Logger;



public class SQLEspacios {
	
	private static String componerSeleccion(String espacios)
	{
		String resultado=espacios.replaceAll(",","' OR IDESPACIO='");
		
		resultado= "(IDESPACIO='"+resultado+"')";
		return resultado;
	}
	
	public static boolean eliminarEspacios(String espacios)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!espacios.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("DELETE FROM ESPACIOS WHERE "+componerSeleccion(espacios));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}

	public static JSONArray listarEspacios()
	{
		JSONArray resultados=new JSONArray();
		EspacioBean espacio;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDESPACIO,NOMBRE,AFORO,DESCRIPCION,ESTADO,PLANTA FROM ESPACIOS");
			
			while (rs.next())
			{
				espacio=new EspacioBean();
				espacio.setIdEspacio(rs.getString(1));
				espacio.setNombreEspacio(rs.getString(2));
				espacio.setAforo(rs.getInt(3));
				espacio.setDescripcion(rs.getString(4));
				espacio.setEstado(rs.getInt(5));
				espacio.setPlanta(rs.getString(6));

				
				resultados.put(new JSONObject(espacio.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	public static JSONObject consultarEspacio(String id)
	{
		JSONObject resultados=new JSONObject();
		EspacioBean espacio;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDESPACIO,NOMBRE,AFORO,DESCRIPCION,ESTADO,PLANTA FROM ESPACIOS WHERE IDESPACIO='"+id+"'");
			
			if (rs.next())
			{
				espacio=new EspacioBean();
				espacio.setIdEspacio(rs.getString(1));
				espacio.setNombreEspacio(rs.getString(2));
				espacio.setAforo(rs.getInt(3));
				espacio.setDescripcion(rs.getString(4));
				espacio.setEstado(rs.getInt(5));
				espacio.setPlanta(rs.getString(6));

				
				resultados=new JSONObject(espacio.toJson());
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static boolean insertEspacio(EspacioBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO ESPACIOS (IDESPACIO,NOMBRE,AFORO,DESCRIPCION,FECHA,FECHA_UPDATE,ESTADO,PLANTA) VALUES (?,?,?,?,?,?,?,?)");
			sentencia.setString(1,bean.getIdEspacio());
			sentencia.setString(2,bean.getNombreEspacio());
			sentencia.setInt(3, bean.getAforo());
			sentencia.setString(4, bean.getDescripcion());
			sentencia.setLong(5,System.currentTimeMillis());
			sentencia.setLong(6,System.currentTimeMillis());
			sentencia.setInt(7,0);
			sentencia.setString(8, bean.getPlanta());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static boolean updateEspacio(EspacioBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ESPACIOS SET NOMBRE=?,AFORO=?,DESCRIPCION=?,FECHA_UPDATE=?,PLANTA=? WHERE IDESPACIO=?");
			sentencia.setString(1,bean.getNombreEspacio());
			sentencia.setInt(2, bean.getAforo());
			sentencia.setString(3, bean.getDescripcion());
			sentencia.setLong(4,System.currentTimeMillis());
			sentencia.setString(5, bean.getPlanta());
			sentencia.setString(6,bean.getIdEspacio());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
}
