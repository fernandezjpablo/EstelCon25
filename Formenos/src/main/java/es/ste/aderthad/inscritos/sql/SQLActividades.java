package es.ste.aderthad.inscritos.sql;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.ActividadBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;


public class SQLActividades {
	private static String componerSeleccion(String actividades)
	{
		String resultado=actividades.replaceAll(",","' OR IDACTIVIDAD='");
		
		resultado= "(IDACTIVIDAD='"+resultado+"')";
		return resultado;
	}
	
	public static boolean eliminarActividades(String actividades)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!actividades.equals(""))
			{
				Connection con=SQLConexionInscritos.getConexion();
				sentencia=con.prepareStatement("DELETE FROM ACTIVIDAD WHERE "+componerSeleccion(actividades));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			return false;
		}
		
		return resultado;
	}
	
	public static JSONArray listarActividades()
	{
		JSONArray resultados=new JSONArray();
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO FROM ACTIVIDAD");
			
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
				resultados.put(new JSONObject(actividad.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static int getOcupacion(ActividadBean idActividad)
	{
		int resultados=0;
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM INSCRIPCIONESACTIVIDADES WHERE IDACTIVIDAD='"+idActividad.getIdActividad()+"' AND ESTADO<>9");
			
			if (rs.next())
			{
				resultados=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static int getListaEspera(ActividadBean idActividad)
	{
		int resultados=0;
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM INSCRIPCIONESACTIVIDADES WHERE IDACTIVIDAD='"+idActividad.getIdActividad()+"' AND ESTADO=9");
			
			if (rs.next())
			{
				resultados=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static int getOcupacion(String idActividad)
	{
		int resultados=0;
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM INSCRIPCIONESACTIVIDADES WHERE IDACTIVIDAD='"+idActividad+"' AND ESTADO<>9");
			
			if (rs.next())
			{
				resultados=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static int getOcupacionListaEspera(ActividadBean idActividad)
	{
		int resultados=0;
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM INSCRIPCIONESACTIVIDADES WHERE IDACTIVIDAD='"+idActividad.getIdActividad()+"' AND ESTADO=9");
			
			if (rs.next())
			{
				resultados=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static int getOcupacionListaEspera(String idActividad)
	{
		int resultados=0;
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM INSCRIPCIONESACTIVIDADES WHERE IDACTIVIDAD='"+idActividad+"' AND ESTADO=9");
			
			if (rs.next())
			{
				resultados=rs.getInt(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray listarActividadesFlotantesInscribibles(boolean isMenor)
	{
		JSONArray resultados=new JSONArray();
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			JSONObject actividadObj;
			String condicionPublico=" AND PUBLICO<>'menores'";
			if (isMenor) condicionPublico=" AND PUBLICO<>'adultos'";
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL FROM ACTIVIDAD WHERE ESTADO=1 "+condicionPublico);
			
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
				actividadObj=new JSONObject(actividad.toJson());
				actividadObj.put("ocupacion",SQLActividades.getOcupacion(actividad));
				resultados.put(actividadObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray listarActividadesInscribibles(boolean isMenor)
	{
		JSONArray resultados=new JSONArray();
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			JSONObject actividadObj;
			String condicionPublico=" AND PUBLICO<>'menores'";
			if (isMenor) condicionPublico=" AND PUBLICO<>'adultos'";
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL FROM ACTIVIDAD WHERE ESTADO=5 "+condicionPublico);
			
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
				actividadObj=new JSONObject(actividad.toJson());
				actividadObj.put("ocupacion",SQLActividades.getOcupacion(actividad));
				actividadObj.put("espera",SQLActividades.getListaEspera(actividad));
				resultados.put(actividadObj);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray listarInscritosTotales(String idactividad)
	{
		JSONArray lista=new JSONArray();
		try{
		Statement sentencia;
		Connection con=SQLConexionInscritos.getConexion();
		sentencia=con.createStatement();

		JSONObject objeto;
		
		ResultSet rs=sentencia.executeQuery("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS,PSEUDONIMO,EMAIL,TELEFONO,TELEGRAM,MENOR,INSCRIPCIONESACTIVIDADES.ESTADO,INSCRIPCIONESACTIVIDADES.FECHA,INSCRIPCIONESACTIVIDADES.OBSERVACIONES from INSCRIPCIONESACTIVIDADES INNER JOIN INSCRITOS ON INSCRIPCIONESACTIVIDADES.IDINSCRITO=INSCRITOS.IDINSCRITO WHERE INSCRIPCIONESACTIVIDADES.IDACTIVIDAD='"+idactividad+"' ORDER BY ESTADO,INSCRIPCIONESACTIVIDADES.FECHA ASC");
		
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
			objeto.put("observaciones", rs.getString(11));
			lista.put(objeto);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
	}

		return lista;
	}
	
	public static JSONArray listarInscritos(String idactividad)
	{
		JSONArray lista=new JSONArray();
		try{
		Statement sentencia;
		Connection con=SQLConexionInscritos.getConexion();
		sentencia=con.createStatement();

		JSONObject objeto;
		
		ResultSet rs=sentencia.executeQuery("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS,PSEUDONIMO,EMAIL,TELEFONO,TELEGRAM,MENOR,INSCRIPCIONESACTIVIDADES.ESTADO,INSCRIPCIONESACTIVIDADES.FECHA,INSCRIPCIONESACTIVIDADES.OBSERVACIONES from INSCRIPCIONESACTIVIDADES INNER JOIN INSCRITOS ON INSCRIPCIONESACTIVIDADES.IDINSCRITO=INSCRITOS.IDINSCRITO WHERE INSCRIPCIONESACTIVIDADES.ESTADO=0 AND INSCRIPCIONESACTIVIDADES.IDACTIVIDAD='"+idactividad+"' ORDER BY ESTADO,INSCRIPCIONESACTIVIDADES.FECHA ASC");
		
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
			objeto.put("observaciones", rs.getString(11));
			lista.put(objeto);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
	}

		return lista;
	}
	
	public static JSONArray listarInscritosEspera(String idactividad)
	{
		JSONArray lista=new JSONArray();
		try{
		Statement sentencia;
		Connection con=SQLConexionInscritos.getConexion();
		sentencia=con.createStatement();

		JSONObject objeto;
		
		ResultSet rs=sentencia.executeQuery("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS,PSEUDONIMO,EMAIL,TELEFONO,TELEGRAM,MENOR,INSCRIPCIONESACTIVIDADES.ESTADO,INSCRIPCIONESACTIVIDADES.FECHA,INSCRIPCIONESACTIVIDADES.OBSERVACIONES from INSCRIPCIONESACTIVIDADES INNER JOIN INSCRITOS ON INSCRIPCIONESACTIVIDADES.IDINSCRITO=INSCRITOS.IDINSCRITO WHERE INSCRIPCIONESACTIVIDADES.ESTADO=9 AND INSCRIPCIONESACTIVIDADES.IDACTIVIDAD='"+idactividad+"' ORDER BY ESTADO,INSCRIPCIONESACTIVIDADES.FECHA ASC");
		
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
			objeto.put("observaciones", rs.getString(11));
			lista.put(objeto);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
	}

		return lista;
	}
	
	public static JSONArray listarMisActividades(String userid)
	{
		JSONArray resultados=new JSONArray();
		JSONObject objeto;
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION,REQUISITOS,OBSERVACIONES FROM ACTIVIDAD WHERE RESPONSABLES_ID LIKE '%"+userid+"%'");
			
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
				actividad.setDuracion(rs.getString(13));
				actividad.setRequisitos(rs.getString(14));
				actividad.setObservaciones(rs.getString(15));
				objeto=new JSONObject(actividad.toJson());
				objeto.put("ocupacion",SQLActividades.getOcupacion(actividad));
				resultados.put(objeto);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONObject consultarActividad(String id)
	{
		JSONObject resultados=new JSONObject();
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO FROM ACTIVIDAD WHERE IDACTIVIDAD='"+id+"'");
			
			if (rs.next())
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
				resultados=new JSONObject(actividad.toJson());
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static ActividadBean buscarActividad(String nombre)
	{
		ActividadBean actividad=null;
			try{
			Statement sentencia;
			nombre=nombre.trim();
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO FROM ACTIVIDAD WHERE NOMBRE LIKE'"+nombre+"%'");
			
			if (rs.next())
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
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return actividad;
	}

	public static boolean insertActividad(ActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("INSERT INTO ACTIVIDAD (IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,FECHA_CREACION,FECHA_UPDATE,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION,OBSERVACIONES,REQUISITOS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			sentencia.setString(1,bean.getIdActividad());
			sentencia.setString(2,bean.getNombreActividad());
			sentencia.setString(3, bean.getResponsables());
			sentencia.setString(4, bean.getNombres_responsables());
			sentencia.setString(5, bean.getDescripcion());
			sentencia.setInt(6, bean.getAforo());
			sentencia.setLong(7,bean.getFecha());
			sentencia.setLong(8,bean.getHora_inicio());
			sentencia.setLong(9,bean.getHora_fin());
			sentencia.setLong(10,System.currentTimeMillis());
			sentencia.setLong(11,System.currentTimeMillis());
			sentencia.setInt(12,bean.getEstado());
			sentencia.setString(13,bean.getPublico());
			sentencia.setBoolean(14,bean.isPagoAdicional());
			sentencia.setString(15,bean.getDuracion());
			sentencia.setString(16,bean.getObservaciones());
			sentencia.setString(17,bean.getRequisitos());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static boolean updateActividad(ActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET NOMBRE=?,RESPONSABLES_ID=?,RESPONSABLES_PSEUDONIMOS=?,DESCRIPCION=?,AFORO=?,FECHA_UPDATE=?,PUBLICO=?,PAGO_ADICIONAL=?,DURACION=?, OBSERVACIONES=?, REQUISITOS=? WHERE IDACTIVIDAD=?");
			sentencia.setString(1,bean.getNombreActividad());
			sentencia.setString(2, bean.getResponsables());
			sentencia.setString(3, bean.getNombres_responsables());
			sentencia.setString(4, bean.getDescripcion());
			sentencia.setInt(5, bean.getAforo());
			sentencia.setLong(6,System.currentTimeMillis());
			sentencia.setString(7,bean.getPublico());
			sentencia.setBoolean(8,bean.isPagoAdicional());
			sentencia.setString(9, bean.getDuracion());
			sentencia.setString(10, bean.getObservaciones());
			sentencia.setString(11,bean.getRequisitos());
			sentencia.setString(12,bean.getIdActividad());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static boolean updateDescripcion(String id,String descripcion)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET DESCRIPCION=? WHERE IDACTIVIDAD=?");
			sentencia.setString(1,descripcion);
			sentencia.setString(2,id);
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateRequisitos(String id,String requisitos)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET REQUISITOS=? WHERE IDACTIVIDAD=?");
			sentencia.setString(1,requisitos);
			sentencia.setString(2,id);
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateObservaciones(String id,String observaciones)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET OBSERVACIONES=? WHERE IDACTIVIDAD=?");
			sentencia.setString(1,observaciones);
			sentencia.setString(2,id);
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean updateDuracion(String id,String duracion)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET DURACION=? WHERE IDACTIVIDAD=?");
			sentencia.setString(1,duracion);
			sentencia.setString(2,id);
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public List<String> listarPlanificadas()
	{
		ArrayList<String> planificadas=new ArrayList<String>();
		
		
		return planificadas;
	}
	
	public static boolean activarActividades(String ids)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=1 WHERE "+componerSeleccion(ids)+" AND ESTADO=0");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=5 WHERE "+componerSeleccion(ids)+" AND ESTADO=4");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean desactivarActividades(String ids)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE "+componerSeleccion(ids)+" AND ESTADO=1");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=4 WHERE "+componerSeleccion(ids)+" AND ESTADO=5");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean planificarActividades(String ids)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=4 WHERE "+componerSeleccion(ids)+" AND ESTADO=0");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=5 WHERE "+componerSeleccion(ids)+" AND ESTADO=1");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean desplanificarActividades(String ids)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE "+componerSeleccion(ids)+" AND ESTADO=4");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=1 WHERE "+componerSeleccion(ids)+" AND ESTADO=5");
			//4 desactivada planificada, 5 activada
			//0 desactivada sin planificar, 1 activada
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static boolean desactivarActividades()
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE ESTADO=1");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=4 WHERE ESTADO=5");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}


public static boolean revisionPlanificaciones()
{
boolean resultado=true;
/* Extraemos las actividades que están planificadas en la tabla Planificacion
 * Actualizamos los estados en función de la select encontrada
 */
JSONArray actividadesPlanificadas=SQLPlanificacion.selectActividades();
resultado= desplanificarTodasActividades();
resultado = resultado && planificarActividades(actividadesPlanificadas.toString().replaceAll("\"","").replace("[","").replace("]", ""));

return resultado;
}
public static boolean desplanificarTodasActividades()
{
	boolean resultado=true;
	try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE ESTADO=4");
		sentencia.executeUpdate();
		sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=1 WHERE ESTADO=5");
		//4 desactivada planificada, 5 activada
		//0 desactivada sin planificar, 1 activada
		sentencia.executeUpdate();
		if (con!=null) con.close();
}  catch (SQLException e) {
	e.printStackTrace();
	resultado=false;
}
	
	return resultado;
}

}