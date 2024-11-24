package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;

public class SQLInscritos {
	
	private static String componerSeleccion(String inscritos)
	{
		String resultado=inscritos.replaceAll(",","' OR IDINSCRITO='");
		
		resultado= "(IDINSCRITO='"+resultado+"')";
		return resultado;
	}
	
	private static String componerSeleccionHabitaciones(String habitaciones,String idcolumna)
	{
		String resultado=habitaciones.replaceAll(",","' OR "+idcolumna+"='");
		
		resultado= "("+idcolumna+"='"+resultado+"')";
		return resultado;
	}
	public static boolean estadoInscritos(String inscritos,String estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!inscritos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE INSCRITOS SET ESTADO="+estado+" WHERE "+componerSeleccion(inscritos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean limpiarNif(String inscritos)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!inscritos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE INSCRITOS SET NIF='' WHERE "+componerSeleccion(inscritos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	
	public static JSONArray ocupantesHabitacion(String idh)
	{
		JSONArray resultado=new JSONArray();
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
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
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=new JSONArray();
	}
		
		return resultado;
	}
	
	
	
	public static boolean recuperarBajas(String inscritos,String estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!inscritos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE INSCRITOS SET ESTADO="+estado+" WHERE ESTADO=9 AND "+componerSeleccion(inscritos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static String getHabitacion(String idinscrito)
	{
		Statement sentencia;
		String resultado="";
		try {
		Connection con=SQLConexion.getConexion();
		sentencia=con.createStatement();

		
		ResultSet rs=sentencia.executeQuery("SELECT HABITACION FROM INSCRITOS WHERE IDINSCRITO='"+idinscrito+"'");
		if (rs.next())
		{
				return rs.getString(1);
		}
		} catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			
		}
		return resultado;
	}
	
	public static int contarEstadoPagados(String idHabitacion,int estado)
	{
		Statement sentencia;
		int resultado=0;
		try {
		Connection con=SQLConexion.getConexion();
		sentencia=con.createStatement();

		
		ResultSet rs=sentencia.executeQuery("SELECT COUNT(*) FROM INSCRITOS WHERE HABITACION='"+idHabitacion+"' AND ESTADO="+estado);
		if (rs.next())
		{
				return rs.getInt(1);
		}
		} catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			
		}
		return resultado;
	}

	public static JSONArray selectOcupantesInscritos(String idHabitacion)
	{
		JSONArray resultados=new JSONArray();
		JSONObject objetoInscrito;
		InscritoBean inscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,OBSERVACIONES,FECHA,FECHAUPDATE,HABITACION,ESTADO,CON_BEBES,SMIAL FROM INSCRITOS WHERE HABITACION LIKE '"+idHabitacion+"%'");
			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion(rs.getString(17).substring(0,36));
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				if (rs.getInt(18)==9) inscrito.setGrupal(false);
				inscrito.setConBebes(rs.getString(19));
				inscrito.setSmial(rs.getString(20));
				habitacion=SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36));
				inscrito.setHabitacionObj(habitacion);
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion",inscrito.obtenerTipoHabitacion());
				objetoInscrito.put("importePlaza",inscrito.calcularImporte());
				resultados.put(objetoInscrito);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray selectInscritos(String idHabitacion)
	{
		JSONArray resultados=new JSONArray();
		JSONObject objetoInscrito;
		InscritoBean inscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,OBSERVACIONES,FECHA,FECHAUPDATE,HABITACION,ESTADO,CON_BEBES,SMIAL FROM INSCRITOS WHERE HABITACION='"+idHabitacion+"'");
			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion(rs.getString(17).substring(0,36));
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				if (rs.getInt(18)==9) inscrito.setGrupal(false);
				inscrito.setConBebes(rs.getString(19));
				inscrito.setSmial(rs.getString(20));
				habitacion=SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36));
				inscrito.setHabitacionObj(habitacion);
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion",inscrito.obtenerTipoHabitacion());
				objetoInscrito.put("importePlaza",inscrito.calcularImporte());
				resultados.put(objetoInscrito);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	

	public static JSONArray selectInscritos()
	{
		JSONArray resultados=new JSONArray();
		InscritoBean inscrito;
		JSONObject objetoInscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,HABITACIONES.ESTADO,INSCRITOS.OBSERVACIONES,CON_BEBES,SMIAL FROM INSCRITOS INNER JOIN HABITACIONES ON LEFT(INSCRITOS.HABITACION,36) = HABITACIONES.IDHABITACION ORDER BY INSCRITOS.HABITACION ASC");
			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion(rs.getString(17).substring(0,36));
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				inscrito.setObservaciones(rs.getString(20));
				inscrito.setConBebes(rs.getString(21));
				inscrito.setSmial(rs.getString(22));
				if (rs.getInt(19)==9) inscrito.setGrupal(false);
				habitacion=SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36));
				inscrito.setHabitacionObj(habitacion);
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion",inscrito.obtenerTipoHabitacion());
				objetoInscrito.put("importePlaza",inscrito.calcularImporte());
				objetoInscrito.put("estadoPagos",inscrito.obtenerEstadoPagos());
				resultados.put(objetoInscrito);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray selectInscritosOrdenadosHabitacion()
	{
		JSONArray resultados=new JSONArray();
		InscritoBean inscrito;
		JSONObject objetoInscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,HABITACIONES.ESTADO,INSCRITOS.OBSERVACIONES,CON_BEBES,SMIAL FROM INSCRITOS INNER JOIN HABITACIONES ON LEFT(INSCRITOS.HABITACION,36) = HABITACIONES.IDHABITACION ORDER BY HABITACIONES.PLANTA ASC, HABITACIONES.IDENTIFICADOR ASC");
			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion(rs.getString(17).substring(0,36));
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				inscrito.setObservaciones(rs.getString(20));
				inscrito.setConBebes(rs.getString(21));
				inscrito.setSmial(rs.getString(22));
				if (rs.getInt(19)==9) inscrito.setGrupal(false);
				habitacion=SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36));
				inscrito.setHabitacionObj(habitacion);
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion",inscrito.obtenerTipoHabitacion());
				objetoInscrito.put("importePlaza",inscrito.calcularImporte());
				objetoInscrito.put("estadoPagos",inscrito.obtenerEstadoPagos());
				resultados.put(objetoInscrito);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}	

	
	public static JSONArray selectEmailsActivos()
	{
		JSONArray resultados=new JSONArray();

			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT EMAIL FROM INSCRITOS WHERE ESTADO<>9");
			
			while (rs.next())
			{
				resultados.put(rs.getString(1));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static JSONArray selectEmailsActivosDeuda()
	{
		JSONArray resultados=new JSONArray();
		InscritoBean inscrito;
		JSONObject objetoInscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,HABITACIONES.ESTADO,INSCRITOS.OBSERVACIONES,CON_BEBES FROM INSCRITOS INNER JOIN HABITACIONES ON LEFT(INSCRITOS.HABITACION,36) = HABITACIONES.IDHABITACION AND INSCRITOS.ESTADO<>9 ORDER BY INSCRITOS.HABITACION ASC");

			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion(rs.getString(17).substring(0,36));
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				inscrito.setObservaciones(rs.getString(20));
				inscrito.setConBebes(rs.getString(21));
				if (rs.getInt(19)==9) inscrito.setGrupal(false);
				habitacion=SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36));
				inscrito.setHabitacionObj(habitacion);
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion",inscrito.obtenerTipoHabitacion());
				objetoInscrito.put("importePlaza",inscrito.calcularImporte());
				objetoInscrito.put("estadoPagos",inscrito.obtenerEstadoPagos());
				if (!objetoInscrito.getString("estadoPagos").equals("0.0"))
				{
					resultados.put(inscrito.getEmail());
				}
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	public static JSONArray selectInscritosSinHabitacion()
	{
		JSONArray resultados=new JSONArray();
		InscritoBean inscrito;
		JSONObject objetoInscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,SMIAL FROM INSCRITOS WHERE HABITACION='' ORDER BY ESTADO ASC");
			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion("");
				inscrito.setEstado(rs.getInt(18));
				inscrito.setSmial(rs.getString(19));
				inscrito.setGrupal(false);
				inscrito.setHabitacionObj(new HabitacionBean());
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion","(Sin habitación)");
				objetoInscrito.put("importePlaza","0");
				objetoInscrito.put("estadoPagos",inscrito.obtenerEstadoPagos());
				resultados.put(objetoInscrito);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	
	public static JSONArray selectInscritosListaEspera()
	{
		JSONArray resultados=new JSONArray();
		InscritoBean inscrito;
		JSONObject objetoInscrito;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO FROM INSCRITOS WHERE HABITACION='(Lista de espera)' ORDER BY ESTADO ASC");
			
			while (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion("");
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(false);
				inscrito.setHabitacionObj(new HabitacionBean());
				objetoInscrito=new JSONObject(inscrito.toJson());
				objetoInscrito.put("tipoHabitacion","(Sin habitación)");
				objetoInscrito.put("importePlaza","0");
				resultados.put(objetoInscrito);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		return resultados;
	}
	
	public static boolean checkExiste(InscritoBean bean)
	{
		boolean resultado=false;
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("SELECT * FROM INSCRITOS WHERE NIF=?");
			sentencia.setString(1,bean.getNif());
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=true;
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultado=false;
		}
		
		return resultado;
	}



	public static boolean altaInscrito(InscritoBean bean)
	{
		boolean resultado=true;
		try{
				PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO INSCRITOS "
					+ "(NOMBRE,HABITACION,PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS, ALERGIAS_TXT,"
					+ "ALIMENTOS,ALIMENTOS_TXT,OBSERVACIONES,FECHA,FECHAUPDATE,ESTADO,APELLIDOS,IDINSCRITO,CON_BEBES) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			sentencia.setString(1,bean.getNombre());
			//sentencia.setString(2,bean.getApellido());
			sentencia.setString(3,bean.getPseudonimo());
			sentencia.setString(4,bean.getNif());
			sentencia.setString(5,bean.getEmail());
			sentencia.setString(6,bean.getTelefono());
			sentencia.setString(7,bean.getTelegram());
			sentencia.setBoolean(8,bean.isMenor());
			sentencia.setBoolean(9,bean.isAlergias());
			sentencia.setString(10,bean.getAlergias_txt());
			sentencia.setBoolean(11,bean.isAlimentos());
			sentencia.setString(12,bean.getAlimentos_txt());
			sentencia.setString(13,bean.getObservaciones());
			sentencia.setLong(14,System.currentTimeMillis());
			sentencia.setLong(15,System.currentTimeMillis());
			sentencia.setInt(16,bean.getEstado());
			sentencia.setString(2,bean.getHabitacion());
			sentencia.setString(17,bean.getApellido());
			sentencia.setString(18,bean.getId());
			sentencia.setString(19,bean.conBebes());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	public static boolean sacarHabitacionParcial(String habitaciones) {
		boolean resultado=true;
		try{
		PreparedStatement sentencia;
		if (!habitaciones.equals(""))
		{
			Connection con=SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE HABITACIONESPARCIALES SET ESTADO=0 WHERE "+componerSeleccionHabitaciones(habitaciones,"IDHABITACION"));
			sentencia.executeUpdate();
			if (con!=null) con.close();
		}
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		resultado=false;
	}
		return resultado;
	}

	public static boolean asignarHabitacion(String inscrito,String habitacion) {
		boolean resultado=true;
		try{
		PreparedStatement sentencia;
		if (!inscrito.equals(""))
		{	
			Connection con=SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRITOS SET HABITACION='"+habitacion+"' WHERE IDINSCRITO='"+inscrito+"'");
			sentencia.executeUpdate();
			if (con!=null) con.close();
		}
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		resultado=false;
	}
		return resultado;
	}	

	
	public static boolean sacarHabitacion(String inscritos) {
		boolean resultado=true;
		String habitaciones;
		try{
		PreparedStatement sentencia;
		if (!inscritos.equals(""))
		{	
			habitaciones=getHabitaciones(inscritos);
			Connection con=SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRITOS SET HABITACION='' WHERE "+componerSeleccion(inscritos));
			sentencia.executeUpdate();
			if (con!=null) con.close();
			sacarHabitacionParcial(habitaciones);
		}
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		// TODO Auto-generated catch block
		return false;
	}
	
	return resultado;
	}
	
	public static String getHabitaciones(String idinscritos)
	{
		Statement sentencia;
		String lista="";
		try {
		Connection con=SQLConexion.getConexion();
		sentencia=con.createStatement();

		
		ResultSet rs=sentencia.executeQuery("SELECT HABITACION FROM INSCRITOS WHERE "+componerSeleccion(idinscritos));
		while (rs.next())
		{
				lista+=rs.getString(1)+",";
		}
		if (!"".equals(lista)) lista=lista.toString().substring(0,lista.length()-1);
		} catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			lista="";
		}
		return lista;
	}

	public static InscritoBean select(String id) 
		{
			InscritoBean inscrito=null;
			HabitacionBean habitacion;
				try{
				Statement sentencia;
				Connection con=SQLConexion.getConexion();
				sentencia=con.createStatement();

				/*Sacamos  los inscritos con habitación*/
				ResultSet rs=sentencia.executeQuery("SELECT IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,HABITACIONES.ESTADO,SMIAL FROM INSCRITOS INNER JOIN HABITACIONES ON LEFT(INSCRITOS.HABITACION,36) = HABITACIONES.IDHABITACION ORDER BY INSCRITOS.HABITACION ASC");
				
				if (rs.next())
				{
					inscrito=new InscritoBean();
					inscrito.setId(rs.getString(1));
					inscrito.setNombre(rs.getString(2));
					inscrito.setApellido(rs.getString(3));
					inscrito.setPseudonimo(rs.getString(4));
					inscrito.setNif(rs.getString(5));
					inscrito.setEmail(rs.getString(6));
					inscrito.setTelefono(rs.getString(7));
					inscrito.setTelegram(rs.getString(8));
					inscrito.setMenor(rs.getBoolean(9));
					inscrito.setAlergias(rs.getBoolean(10));
					inscrito.setAlergias_txt(rs.getString(11));
					inscrito.setAlimentos(rs.getBoolean(12));
					inscrito.setAlimentos_txt(rs.getString(13));
					inscrito.setObservaciones(rs.getString(14));
					inscrito.setFecha(rs.getLong(15));
					inscrito.setFechaUpdate(rs.getLong(16));
					inscrito.setHabitacion(rs.getString(17).substring(0,36));
					inscrito.setEstado(rs.getInt(18));
					inscrito.setGrupal(true);
					if (rs.getInt(19)==9) inscrito.setGrupal(false);
					inscrito.setSmial(rs.getString(20));
					inscrito.setHabitacionObj(SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36)));
				}
				if (con!=null) con.close();
			}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
				e.printStackTrace();
				inscrito=null;
			}
			return inscrito;
		}

	public static InscritoBean selectUsuario(String usuario) 
	{
		InscritoBean inscrito=null;
		HabitacionBean habitacion;
		String habitacionStr="";
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,ESTADO,SMIAL FROM INSCRITOS INNER JOIN USUARIOS ON USUARIOS.IDINSCRITO = INSCRITOS.IDINSCRITO AND UPPER(USUARIO)=UPPER('"+usuario+"')");
			
			if (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				habitacionStr=rs.getString(17);
				if (!habitacionStr.equals(""))
				{
					habitacionStr=rs.getString(17).substring(0,36);
				}
				else
				{
					habitacionStr="";
				}
				inscrito.setHabitacion(habitacionStr);
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				if (rs.getInt(19)==9) inscrito.setGrupal(false);
				inscrito.setHabitacionObj(SQLHabitaciones.selectHabitacion(habitacionStr));
				inscrito.setSmial(rs.getString(20));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			inscrito=null;
		}
		return inscrito;
	}
	
	public static InscritoBean selectIdInscrito(String idInscrito) 
	{
		InscritoBean inscrito=null;
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			/*Sacamos  los inscritos con habitación*/
			ResultSet rs=sentencia.executeQuery("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,HABITACION,INSCRITOS.ESTADO,ESTADO,SMIAL FROM INSCRITOS INNER JOIN USUARIOS ON USUARIOS.IDINSCRITO = INSCRITOS.IDINSCRITO AND UPPER(INSCRITOS.IDINSCRITO)=UPPER('"+idInscrito+"')");
			
			if (rs.next())
			{
				inscrito=new InscritoBean();
				inscrito.setId(rs.getString(1));
				inscrito.setNombre(rs.getString(2));
				inscrito.setApellido(rs.getString(3));
				inscrito.setPseudonimo(rs.getString(4));
				inscrito.setNif(rs.getString(5));
				inscrito.setEmail(rs.getString(6));
				inscrito.setTelefono(rs.getString(7));
				inscrito.setTelegram(rs.getString(8));
				inscrito.setMenor(rs.getBoolean(9));
				inscrito.setAlergias(rs.getBoolean(10));
				inscrito.setAlergias_txt(rs.getString(11));
				inscrito.setAlimentos(rs.getBoolean(12));
				inscrito.setAlimentos_txt(rs.getString(13));
				inscrito.setObservaciones(rs.getString(14));
				inscrito.setFecha(rs.getLong(15));
				inscrito.setFechaUpdate(rs.getLong(16));
				inscrito.setHabitacion(rs.getString(17).substring(0,36));
				inscrito.setEstado(rs.getInt(18));
				inscrito.setGrupal(true);
				if (rs.getInt(19)==9) inscrito.setGrupal(false);
				inscrito.setHabitacionObj(SQLHabitaciones.selectHabitacion(rs.getString(17).substring(0,36)));
				inscrito.setSmial(rs.getString(20));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			inscrito=null;
		}
		return inscrito;
	}
	
	public static boolean limpiarHabitaciones(String listaPlazas) {
		boolean resultado=true;
		try{
		PreparedStatement sentencia;
		if (!listaPlazas.equals(""))
		{
			Connection con=SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE INSCRITOS SET HABITACION='' WHERE "+componerSeleccionHabitaciones(listaPlazas,"HABITACION"));
			sentencia.executeUpdate();
			if (con!=null) con.close();
		}
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		// TODO Auto-generated catch block
		return false;
	}
	
	return resultado;
	}
}

