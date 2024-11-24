package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;


import es.ste.aderthad.log.Logger;

public class SQLInscripcionesActividades {
	
	private static String componerSeleccion(String ids)
	{
		String resultado=ids.replaceAll(";","') AND (IDACTIVIDAD='");
		resultado=resultado.replaceAll(",","')) OR ((IDINSCRITO='");
		
		resultado= "((IDINSCRITO='"+resultado+"'))";
		return resultado;
	}
	
	public static String selectObservacionesInscripcion(String idActividad,String idInscrito)
	{
		String resultado="";
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("SELECT OBSERVACIONES FROM INSCRIPCIONESACTIVIDADES WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setString(1,idInscrito);
			sentencia.setString(2,idActividad);			
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado="";
	}
	
	return resultado;
}	
	public static boolean deleteInscripcionActividadMultiple(String ids)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("DELETE FROM INSCRIPCIONESACTIVIDADES WHERE ("+componerSeleccion(ids)+")");
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static boolean updateInscripcionActividadMultiple(String ids,int estado)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRIPCIONESACTIVIDADES SET FECHA_UPDATE=?,ESTADO=? WHERE ("+componerSeleccion(ids)+")");
			sentencia.setLong(1, System.currentTimeMillis());
			sentencia.setLong(2, estado);

			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}	
	public static boolean updateInscripcionActividad(String idactividad,String idinscrito,int estado)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRIPCIONESACTIVIDADES SET FECHA_UPDATE=?,ESTADO=? WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setLong(1, System.currentTimeMillis());
			sentencia.setLong(2, estado);
			sentencia.setString(4,idactividad);
			sentencia.setString(3,idinscrito);
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}	
	
	
	public static boolean updateObservacionesInscripcionActividad(String idactividad,String idinscrito,String observaciones)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRIPCIONESACTIVIDADES SET FECHA_UPDATE=?,OBSERVACIONES=? WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setLong(1, System.currentTimeMillis());
			sentencia.setString(2, observaciones);
			sentencia.setString(4,idactividad);
			sentencia.setString(3,idinscrito);
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}	
	
	public static String selectObservaciones(String idactividad,String idinscrito)
	{
		String resultado="";
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("SELECT OBSERVACIONES FROM INSCRIPCIONESACTIVIDADES WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setString(2,idactividad);
			sentencia.setString(1,idinscrito);
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (resultado==null) resultado="";
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado="";
	}
		
		return resultado;
	}
	
	public static boolean deleteInscripcionActividad(String idactividad,String idinscrito,int estado)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("DELETE FROM INSCRIPCIONESACTIVIDADES WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setString(1,idactividad);
			sentencia.setString(2,idinscrito);
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}	
	
	public static JSONArray listarInscritos(String idactividad)
	{
		JSONArray lista=new JSONArray();
		try{
		PreparedStatement sentencia;
		Connection con=SQLConexion.getConexion();


		JSONObject objeto;
		
		sentencia=con.prepareStatement("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS,PSEUDONIMO,EMAIL,TELEFONO,TELEGRAM,MENOR,INSCRIPCIONESACTIVIDADES.ESTADO,INSCRIPCIONESACTIVIDADES.FECHA,INSCRIPCIONESACTIVIDADES.FECHA_UPDATE,INSCRIPCIONESACTIVIDADES.OBSERVACIONES "
				+ "FROM INSCRIPCIONESACTIVIDADES INNER JOIN INSCRITOS ON INSCRIPCIONESACTIVIDADES.IDINSCRITO=INSCRITOS.IDINSCRITO WHERE INSCRIPCIONESACTIVIDADES.IDACTIVIDAD=? ORDER BY INSCRIPCIONESACTIVIDADES.ESTADO AND INSCRIPCIONESACTIVIDADES.FECHA_UPDATE");
		/*
		 * SELECT ACTIVIDAD.IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,INSCRIPCIONESACTIVIDADES.ESTADO,PUBLICO,PAGO_ADICIONAL"
				+ " FROM INSCRIPCIONESACTIVIDADES INNER JOIN ACTIVIDAD ON INSCRIPCIONESACTIVIDADES.IDACTIVIDAD = ACTIVIDAD.IDACTIVIDAD AND INSCRIPCIONESACTIVIDADES.IDINSCRITO=?
		 * */
		sentencia.setString(1, idactividad);
		ResultSet rs=sentencia.executeQuery();
		
//		System.out.println("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS,PSEUDONIMO,EMAIL,TELEFONO,TELEGRAM,MENOR,INSCRIPCIONESACTIVIDADES.ESTADO FROM INSCRIPCIONESACTIVIDADES INNER JOIN INSCRITOS IN INSCRIPCIONESACTIVIDADES.IDINSCRITO=INSCRITOS.IDINSCRITO WHERE INSCRIPCIONESACTIVIDADES.IDACTIVIDAD='"+idactividad+"'");
//		ResultSet rs=sentencia.executeQuery("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS,PSEUDONIMO,EMAIL,TELEFONO,TELEGRAM,MENOR,INSCRIPCIONESACTIVIDADES.ESTADO FROM INSCRIPCIONESACTIVIDADES INNER JOIN INSCRITOS IN INSCRIPCIONESACTIVIDADES.IDINSCRITO=INSCRITOS.IDINSCRITO WHERE INSCRIPCIONESACTIVIDADES.IDACTIVIDAD='"+idactividad+"'");
		
		while (rs.next())
		{
			objeto=new JSONObject();
			objeto.put("id", rs.getString(1));
			objeto.put("nombre", rs.getString(2));
			objeto.put("apellidos", rs.getString(3));
			objeto.put("pseudonimo", rs.getString(4));
			objeto.put("email", rs.getString(5));
			objeto.put("telefono", rs.getString(6));
			objeto.put("telegram", rs.getString(7));
			objeto.put("menor", rs.getBoolean(8));
			objeto.put("estado", rs.getInt(9));
			objeto.put("fecha", rs.getLong(10));
			objeto.put("fechaUpdate", rs.getLong(11));
			objeto.put("observaciones", "(Sin observaciones del inscrito)");
			if (rs.getString(12)!=null)
			{
				objeto.put("observaciones", rs.getString(12));
			}
			lista.put(objeto);
		}
		if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
	}

		return lista;
	}
}
