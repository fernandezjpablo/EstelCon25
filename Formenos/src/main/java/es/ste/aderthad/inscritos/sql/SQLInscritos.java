package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.json.JSONArray;

import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;


public class SQLInscritos {
	public static String obtenerId()
	{
		String resultado=UUID.randomUUID().toString();
		return resultado;

	}
	public static boolean checkExiste(InscritoBean bean)
	{
		boolean resultado=false;
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("SELECT * FROM INSCRITOS WHERE NIF=?");
			sentencia.setString(1,bean.getNif());
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=true;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
			resultado=false;
		}
		
		return resultado;
	}
	
	
	public static String ocupantesHabitacion(String idh)
	{
		JSONArray resultado=new JSONArray();
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("SELECT NOMBRE, APELLIDOS, PSEUDONIMO,SMIAL FROM INSCRITOS WHERE HABITACION LIKE '"+idh+"%'");
			ResultSet rs =sentencia.executeQuery();
			String cadena="";
			while (rs.next())
			{
			cadena=rs.getString(1)+" "+rs.getString(2);
			if (rs.getString(3)!=null &&!rs.getString(3).equals(""))
			{
				cadena+="("+rs.getString(3)+") ";
			}
			if (rs.getString(4)!=null && !rs.getString(4).equals(""))
			{
				cadena+="Smial: "+rs.getString(4)+".";
			}
			resultado.put(cadena);
			}
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=new JSONArray();
	}
		
		return resultado.toString();
	}
	
	public static boolean actualizarInscrito(InscritoBean bean)
	{
		boolean resultado=true;
		if (bean.conBebes().equals("")) bean.setConBebes("0");
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRITOS set NOMBRE=?,APELLIDOS=?,PSEUDONIMO=?,NIF=?,EMAIL=?,TELEFONO=?,"
					+ "MENOR=?,FECHAUPDATE=?,ALERGIAS=?,ALERGIAS_TXT=?,ALIMENTOS=?,ALIMENTOS_TXT=?,"
					+ "OBSERVACIONES=?,TELEGRAM=?,SMIAL=?,CON_BEBES=? WHERE IDINSCRITO=?");

			sentencia.setString(1,bean.getNombre());
			sentencia.setString(2,bean.getApellido());
			sentencia.setString(3,bean.getPseudonimo());
			sentencia.setString(4,bean.getNif());
			sentencia.setString(5,bean.getEmail());
			sentencia.setString(6,bean.getTelefono());
			sentencia.setBoolean(7,bean.isMenor());
			sentencia.setLong(8,System.currentTimeMillis());
			sentencia.setBoolean(9, bean.isAlergias());
			sentencia.setString(10,bean.getAlergias_txt());
			sentencia.setBoolean(11, bean.isAlimentos());
			sentencia.setString(12,bean.getAlimentos_txt());
			sentencia.setString(13,bean.getObservaciones());
			sentencia.setString(14,bean.getTelegram());
			sentencia.setString(15,bean.getSmial());
			sentencia.setString(16,bean.conBebes());
			sentencia.setString(17,bean.getId());
			
			
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
