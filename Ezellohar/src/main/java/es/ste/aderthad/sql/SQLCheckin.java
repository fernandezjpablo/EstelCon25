package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import es.ste.aderthad.data.CheckinBean;
import es.ste.aderthad.log.Logger;



public class SQLCheckin {

	public static boolean insertCheckin(CheckinBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO CHECKIN (USUARIO,IDUSUARIO,NOMBRE,APELLIDOS,FECHA_NACIMIENTO,NIF,FECHA_EXPEDICION,FECHA_CREACION,FECHA_UPDATE,OBSERVACIONES) VALUES (?,?,?,?,?,?,?,?,?,?)");
			sentencia.setString(1, bean.getUsuario());
			sentencia.setString(2, bean.getIdInscrito());
			sentencia.setString(3, bean.getNombre());
			sentencia.setString(4, bean.getApellidos());
			sentencia.setString(5, bean.getFechaNacimiento());
			sentencia.setString(6, bean.getNif());
			sentencia.setString(7, bean.getFechaExpedicion());
			sentencia.setLong(8, System.currentTimeMillis());
			sentencia.setLong(9, System.currentTimeMillis());
			sentencia.setString(10, bean.getObservaciones());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateCheckin(CheckinBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE CHECKIN SET NOMBRE=?,APELLIDOS=?,FECHA_NACIMIENTO=?,NIF=?,FECHA_EXPEDICION=?,FECHA_UPDATE=?,OBSERVACIONES=? WHERE USUARIO=?");

			sentencia.setString(1, bean.getNombre());
			sentencia.setString(2, bean.getApellidos());
			sentencia.setString(3, bean.getFechaNacimiento());
			sentencia.setString(4, bean.getNif());
			sentencia.setString(5, bean.getFechaExpedicion());
			sentencia.setLong(6, System.currentTimeMillis());
			sentencia.setString(7, bean.getObservaciones());
			
			sentencia.setString(8, bean.getUsuario());
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static CheckinBean selectCheckin(String usuario)
	{
		boolean resultado=true;
		CheckinBean bean=null;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("SELECT USUARIO,IDUSUARIO,NOMBRE,APELLIDOS,FECHA_NACIMIENTO,NIF,FECHA_EXPEDICION,FECHA_CREACION,FECHA_UPDATE,OBSERVACIONES FROM CHECKIN WHERE USUARIO='"+usuario+"'");
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				bean=new CheckinBean();
				bean.setUsuario(rs.getString(1));
				bean.setIdInscrito(rs.getString(2));
				bean.setNombre(rs.getString(3));
				bean.setApellidos(rs.getString(4));
				bean.setFechaNacimiento(rs.getString(5));
				bean.setNif(rs.getString(6));
				bean.setFechaExpedicion(rs.getString(7));
				bean.setFechaCreacion(rs.getLong(8));
				bean.setFechaUpdate(rs.getLong(9));
				bean.setObservaciones(rs.getString(10));
				
			}
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		bean=null;
	}
		
		return bean;
	}
	
	
	
	public static JSONObject listCheckin()
	{
		JSONObject resultado=new JSONObject();

		CheckinBean bean=null;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("SELECT USUARIO,IDUSUARIO,NOMBRE,APELLIDOS,FECHA_NACIMIENTO,NIF,FECHA_EXPEDICION,FECHA_CREACION,FECHA_UPDATE,OBSERVACIONES FROM CHECKIN");
			ResultSet rs=sentencia.executeQuery();
			while (rs.next())
			{
				bean=new CheckinBean();
				bean.setUsuario(rs.getString(1));
				bean.setIdInscrito(rs.getString(2));
				bean.setNombre(rs.getString(3));
				bean.setApellidos(rs.getString(4));
				bean.setFechaNacimiento(rs.getString(5));
				bean.setNif(rs.getString(6));
				bean.setFechaExpedicion(rs.getString(7));
				bean.setFechaCreacion(rs.getLong(8));
				bean.setFechaUpdate(rs.getLong(9));
				bean.setObservaciones(rs.getString(10));
				resultado.put(bean.getIdInscrito(),new JSONObject(bean.toJson()));
			}
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();

	}
		
		return resultado;
	}
}
