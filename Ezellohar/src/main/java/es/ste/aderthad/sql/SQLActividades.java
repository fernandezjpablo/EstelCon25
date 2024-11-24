package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.ActividadBean;
import es.ste.aderthad.log.Logger;


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
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("DELETE FROM ACTIVIDAD WHERE "+componerSeleccion(actividades));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION, TIPO,OBSERVACIONES,REQUISITOS FROM ACTIVIDAD");
			
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
				actividad.setTipo(rs.getString(14));
				actividad.setObservaciones(rs.getString(15));
				actividad.setRequisitos(rs.getString(16));
				resultados.put(new JSONObject(actividad.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	public static JSONArray listarTiposActividades()
	{
		String tipo="";
		JSONArray resultados=new JSONArray();
			try{
				String sentenciaSql="SELECT DISTINCT TIPO FROM ACTIVIDAD";
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			sentenciaSql=sentenciaSql+" ORDER BY TIPO";
			
			ResultSet rs=sentencia.executeQuery(sentenciaSql);
			
			while (rs.next())
			{
				tipo=rs.getString(1);
				if (tipo==null) tipo="(Sin especificar)";
				resultados.put(tipo);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray listarActividadesFiltro(String filtro,String publico,String noplani)
	{
		JSONArray resultados=new JSONArray();
		ActividadBean actividad;
			try{
				String sentenciaSql="SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION, TIPO,OBSERVACIONES,REQUISITOS FROM ACTIVIDAD";
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			if (!"".equals(filtro) || !"".equals(publico))
			{
				if (!"".equals(filtro))
				{
					if ("(Sin especificar)".equals(filtro))
					{
						sentenciaSql=sentenciaSql+ " WHERE TIPO IS NULL";
						if (!"".equals(publico))
						{
							sentenciaSql=sentenciaSql+ " AND PUBLICO='"+publico+"'";
						}
					}
					else
					{
					sentenciaSql=sentenciaSql+ " WHERE TIPO='"+filtro+"'";
					if (!"".equals(publico))
					{
						sentenciaSql=sentenciaSql+ " AND PUBLICO='"+publico+"'";
					}
					}
				}
				else
				{
					sentenciaSql=sentenciaSql+ " WHERE PUBLICO='"+publico+"'";
				}
				if ("true".equals(noplani))
				{
					sentenciaSql=sentenciaSql+ " AND ESTADO=0";
				}
			}
			else
			{
				if ("true".equals(noplani))
				{
					sentenciaSql=sentenciaSql+ " WHERE ESTADO=0";
				}
			}
			
			sentenciaSql=sentenciaSql+" ORDER BY NOMBRE";
			
			ResultSet rs=sentencia.executeQuery(sentenciaSql);
			
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
				actividad.setTipo(rs.getString(14));
				actividad.setObservaciones(rs.getString(15));
				actividad.setRequisitos(rs.getString(16));
				resultados.put(new JSONObject(actividad.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	public static ActividadBean buscarActividad(String nombre)
	{
		ActividadBean actividad=null;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO, TIPO, OBSERVACIONES, REQUISITOS FROM ACTIVIDAD WHERE NOMBRE LIKE'"+nombre+"%'");
			
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
				actividad.setTipo(rs.getString(12));
				actividad.setObservaciones(rs.getString(13));
				actividad.setRequisitos(rs.getString(14));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return actividad;
	}	
	public static JSONObject consultarActividad(String id)
	{
		JSONObject resultados=new JSONObject();
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION, TIPO, OBSERVACIONES, REQUISITOS FROM ACTIVIDAD WHERE IDACTIVIDAD='"+id+"'");
			
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
				actividad.setPagoAdicional(rs.getBoolean(12));
				actividad.setDuracion(rs.getString(13));
				actividad.setTipo(rs.getString(14));
				actividad.setObservaciones(rs.getString(15));
				actividad.setRequisitos(rs.getString(16));
				resultados=new JSONObject(actividad.toJson());
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONObject consultarActividadByName(String nombre)
	{
		JSONObject resultados=new JSONObject();
		ActividadBean actividad;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION, TIPO, OBSERVACIONES, REQUISITOS FROM ACTIVIDAD WHERE NOMBRE LIKE '"+nombre+"%'");
			
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
				actividad.setPagoAdicional(rs.getBoolean(12));
				actividad.setDuracion(rs.getString(13));
				actividad.setTipo(rs.getString(14));
				actividad.setObservaciones(rs.getString(15));
				actividad.setRequisitos(rs.getString(16));
				resultados=new JSONObject(actividad.toJson());
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}

	public static boolean insertActividad(ActividadBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO ACTIVIDAD (IDACTIVIDAD,NOMBRE,RESPONSABLES_ID,RESPONSABLES_PSEUDONIMOS,DESCRIPCION,AFORO,FECHA_ACTIVIDAD,HORA_INICIO,HORA_FIN,FECHA_CREACION,FECHA_UPDATE,ESTADO,PUBLICO,PAGO_ADICIONAL,DURACION,TIPO,OBSERVACIONES,REQUISITOS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
			sentencia.setInt(12,0);
			sentencia.setString(13,bean.getPublico());
			sentencia.setBoolean(14,bean.isPagoAdicional());
			sentencia.setString(15,bean.getDuracion());
			sentencia.setString(16,bean.getTipo());
			sentencia.setString(17,bean.getObservaciones());
			sentencia.setString(18,bean.getRequisitos());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET NOMBRE=?,RESPONSABLES_ID=?,RESPONSABLES_PSEUDONIMOS=?,DESCRIPCION=?,AFORO=?,FECHA_UPDATE=?,PUBLICO=?,PAGO_ADICIONAL=?,DURACION=?, TIPO=?, OBSERVACIONES=?, REQUISITOS=? WHERE IDACTIVIDAD=?");
			sentencia.setString(1,bean.getNombreActividad());
			sentencia.setString(2, bean.getResponsables());
			sentencia.setString(3, bean.getNombres_responsables());
			sentencia.setString(4, bean.getDescripcion());
			sentencia.setInt(5, bean.getAforo());
			sentencia.setLong(6,System.currentTimeMillis());
			sentencia.setString(7,bean.getPublico());
			sentencia.setBoolean(8,bean.isPagoAdicional());
			sentencia.setString(9, bean.getDuracion());
			sentencia.setString(10, bean.getTipo());
			sentencia.setString(11, bean.getObservaciones());
			sentencia.setString(12, bean.getRequisitos());
			sentencia.setString(13,bean.getIdActividad());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=1 WHERE "+componerSeleccion(ids)+" AND ESTADO=0");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=5 WHERE "+componerSeleccion(ids)+" AND ESTADO=4");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static boolean aceptarActividades(String ids)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE "+componerSeleccion(ids)+" AND ESTADO=9");
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE "+componerSeleccion(ids)+" AND ESTADO=1");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=4 WHERE "+componerSeleccion(ids)+" AND ESTADO=5");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=4 WHERE "+componerSeleccion(ids)+" AND ESTADO=0");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=5 WHERE "+componerSeleccion(ids)+" AND ESTADO=1");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE "+componerSeleccion(ids)+" AND ESTADO=4");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=1 WHERE "+componerSeleccion(ids)+" AND ESTADO=5");
			//4 desactivada planificada, 5 activada
			//0 desactivada sin planificar, 1 activada
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE ESTADO=1");
			sentencia.executeUpdate();
			sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=4 WHERE ESTADO=5");
			//4 sin inscripción planificada, 5 planificada con inscripción
			//0 sin inscripción, 1 con inscripción
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
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
		Connection con = SQLConexion.getConexion();
		sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=0 WHERE ESTADO=4");
		sentencia.executeUpdate();
		sentencia=con.prepareStatement("UPDATE ACTIVIDAD SET ESTADO=1 WHERE ESTADO=5");
		//4 desactivada planificada, 5 activada
		//0 desactivada sin planificar, 1 activada
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