package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.ActividadBean;
import es.ste.aderthad.inscritos.data.InscripcionActividadBean;
import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;



public class SQLInscripcionesActividades {
	
	
	
	public static JSONArray selectActividades(String usuario)
	{
		JSONArray resultado=new JSONArray();
		JSONObject objeto;
		ActividadBean actividad;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("SELECT ACTIVIDAD.IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,INSCRIPCIONESACTIVIDADES.ESTADO,PUBLICO,PAGO_ADICIONAL,INSCRIPCIONESACTIVIDADES.OBSERVACIONES,ACTIVIDAD.ESTADO"
				+ " FROM INSCRIPCIONESACTIVIDADES INNER JOIN ACTIVIDAD ON INSCRIPCIONESACTIVIDADES.IDACTIVIDAD = ACTIVIDAD.IDACTIVIDAD AND INSCRIPCIONESACTIVIDADES.IDINSCRITO=?");
		sentencia.setString(1, usuario);
		ResultSet rs=sentencia.executeQuery();
		while (rs.next())
		{
		actividad=new ActividadBean();
		actividad.setIdActividad(rs.getString(1));
		actividad.setNombreActividad(rs.getString(2));
		actividad.setResponsables(rs.getString(3));
		actividad.setNombres_responsables(rs.getString(4));
		actividad.setDescripcion(rs.getString(5));
		actividad.setAforo(rs.getInt(6));
		actividad.setFecha(rs.getLong(7));
		actividad.setHora_inicio(rs.getLong(8));
		actividad.setHora_fin(rs.getLong(9));
		actividad.setEstado(rs.getInt(10));
		actividad.setPublico(rs.getString(11));
		actividad.setPagoAdicional(rs.getBoolean(12));
		actividad.setEstadoActividad(rs.getInt(14));
		objeto=new JSONObject(actividad.toJson());
		objeto.put("ocupacion",SQLActividades.getOcupacion(actividad));
		objeto.put("lista_espera",SQLActividades.getOcupacionListaEspera(actividad));
		objeto.put("observaciones", rs.getString(13));
		
		resultado.put(objeto);
	
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
	}
		return resultado;
	}
	
	
	public static boolean insertInscripcion(InscripcionActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("INSERT INTO INSCRIPCIONESACTIVIDADES (IDACTIVIDAD,IDINSCRITO,FECHA,FECHA_UPDATE,ESTADO) VALUES (?,?,?,?,?)");
			sentencia.setString(1,bean.getIdactividad());
			sentencia.setString(2,bean.getIdinscrito());
			sentencia.setLong(3, System.currentTimeMillis());
			sentencia.setLong(4, System.currentTimeMillis());
			sentencia.setInt(5, bean.getEstado());
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateInscripcion(InscripcionActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRIPCIONESACTIVIDADES SET FECHA_UPDATE=?,ESTADO=? WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setLong(1, System.currentTimeMillis());
			sentencia.setLong(2, bean.getEstado());
			sentencia.setString(3,bean.getIdinscrito());
			sentencia.setString(4,bean.getIdactividad());
			
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateObservacionesInscripcion(InscripcionActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRIPCIONESACTIVIDADES SET FECHA_UPDATE=?,OBSERVACIONES=? WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setLong(1, System.currentTimeMillis());
			sentencia.setString(2, bean.getObservaciones());
			sentencia.setString(3,bean.getIdinscrito());
			sentencia.setString(4,bean.getIdactividad());

			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}	
	
	
	public static String selectObservacionesInscripcion(InscripcionActividadBean bean)
	{
		String resultado="";
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("SELECT OBSERVACIONES FROM INSCRIPCIONESACTIVIDADES WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setString(1,bean.getIdinscrito());
			sentencia.setString(2,bean.getIdactividad());			
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado="";
	}
		return resultado;
	}
		
		public static String selectObservacionesInscripcion(String idActividad,String idInscrito)
		{
			String resultado="";
			try{
				PreparedStatement sentencia;
				Connection con = SQLConexionInscritos.getConexion();
				sentencia=con.prepareStatement("SELECT OBSERVACIONES FROM INSCRIPCIONESACTIVIDADES WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
				sentencia.setString(1,idInscrito);
				sentencia.setString(2,idActividad);			
				ResultSet rs=sentencia.executeQuery();
				if (rs.next())
				{
					resultado=rs.getString(1);
				}
				if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
			resultado="";
		}
		
		return resultado;
	}	
	
	public static boolean eliminarInscripcion(InscripcionActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("DELETE FROM INSCRIPCIONESACTIVIDADES WHERE IDINSCRITO=? AND IDACTIVIDAD=?");
			sentencia.setString(2,bean.getIdactividad().toString());
			sentencia.setString(1,bean.getIdinscrito().toString());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}


	public static boolean recuperar(String idActividad, int recuperables) {
		/*
		 * UPDATE suppliers
SET top_supplier = 'Yes'
ORDER BY volume_2021 DESC
LIMIT 10;
		 * */
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRIPCIONESACTIVIDADES SET ESTADO=0 WHERE IDACTIVIDAD=? AND ESTADO=9 ORDER BY FECHA_UPDATE ASC LIMIT ?");
			sentencia.setString(1,idActividad);
			sentencia.setInt(2,recuperables);
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
