package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.InscritoBean;
import es.ste.aderthad.inscritos.data.UsuarioBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;



public class SQLUsuarios {
	
	public static boolean updatePassword(String idUsuario,String oldPass,String newPass)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("UPDATE USUARIOS SET PASSWORD=?,FECHA_UPDATE=? WHERE USUARIO=? AND PASSWORD=?");
			sentencia.setString(1,newPass);
			sentencia.setString(3,idUsuario);
			sentencia.setLong(2,System.currentTimeMillis());
			sentencia.setString(4, oldPass);
			
			int total=sentencia.executeUpdate();
			if (total>=1) 
				resultado=true;
			else
				resultado=false;
			if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	public static UsuarioBean selectInscrito(String idInscrito)
	{
		UsuarioBean bean=null;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("SELECT USUARIO,PASSWORD,ID,IDINSCRITO FROM USUARIOS WHERE IDINSCRITO=?");
		sentencia.setString(1, idInscrito);
		ResultSet rs=sentencia.executeQuery();
		if (rs.next())
		{
			bean=new UsuarioBean();
			bean.setId(rs.getString(3));
			bean.setIdInscrito(rs.getString(4));
			bean.setUsuario(rs.getString(1));
			bean.setPassword(rs.getString(2));
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		bean=null;
	}
		return bean;
	}
	
	public static JSONArray selectDatosAcceso(String email)
	{
		JSONArray beanArr=new JSONArray();
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,USUARIOS.USUARIO,USUARIOS.PASSWORD FROM INSCRITOS INNER JOIN USUARIOS ON INSCRITOS.IDINSCRITO = USUARIOS.IDINSCRITO AND INSCRITOS.EMAIL=?");
		sentencia.setString(1, email);
		ResultSet rs=sentencia.executeQuery();
		JSONObject bean;
		while (rs.next())
		{
			bean=new JSONObject();
			bean.put("nombre", rs.getString(2));
			bean.put("apellidos", rs.getString(3));
			bean.put("pseudonimo", rs.getString(4));
			bean.put("usuario", rs.getString(5));
			bean.put("password", rs.getString(6));
			beanArr.put(bean);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
	}
		return beanArr;
	}
	
	public static InscritoBean selectUsuario(String usuario)
	{
		InscritoBean bean=null;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,SMIAL,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,INSCRITOS.ESTADO,INSCRITOS.HABITACION,INSCRITOS.CON_BEBES FROM INSCRITOS INNER JOIN USUARIOS ON INSCRITOS.IDINSCRITO = USUARIOS.IDINSCRITO AND USUARIO=?");
		sentencia.setString(1, usuario);
		ResultSet rs=sentencia.executeQuery();
		if (rs.next())
		{
			bean=new InscritoBean();
			bean.setId(rs.getString(1));
			bean.setNombre(rs.getString(2));
			bean.setApellido(rs.getString(3));
			bean.setPseudonimo(rs.getString(4));
			bean.setNif(rs.getString(5));
			bean.setEmail(rs.getString(6));
			bean.setTelefono(rs.getString(7));
			bean.setTelegram(rs.getString(8));
			bean.setMenor(rs.getBoolean(9));
			bean.setAlergias(rs.getBoolean(10));
			bean.setAlergias_txt(rs.getString(11));
			bean.setAlimentos(rs.getBoolean(12));
			bean.setAlimentos_txt(rs.getString(13));
			bean.setObservaciones(rs.getString(14));
			bean.setSmial(rs.getString(15));
			bean.setEstado(rs.getInt(18));
			bean.setHabitacion(rs.getString(19));
			bean.setConBebes(rs.getString(20));
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		bean=null;
	}
		return bean;
	}
	

	
	public static InscritoBean selectInscritoFull(String inscrito)
	{
		InscritoBean bean=null;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,NIF,EMAIL,TELEFONO,TELEGRAM,MENOR,ALERGIAS,ALERGIAS_TXT,ALIMENTOS,ALIMENTOS_TXT,INSCRITOS.OBSERVACIONES,INSCRITOS.FECHA,INSCRITOS.FECHAUPDATE,INSCRITOS.ESTADO FROM INSCRITOS INNER JOIN USUARIOS ON INSCRITOS.IDINSCRITO = USUARIOS.IDINSCRITO AND INSCRITOS.IDINSCRITO=?");
		sentencia.setString(1, inscrito);
		ResultSet rs=sentencia.executeQuery();
		if (rs.next())
		{
			bean=new InscritoBean();
			bean.setId(rs.getString(1));
			bean.setNombre(rs.getString(2));
			bean.setApellido(rs.getString(3));
			bean.setPseudonimo(rs.getString(4));
			bean.setNif(rs.getString(5));
			bean.setEmail(rs.getString(6));
			bean.setTelefono(rs.getString(7));
			bean.setTelegram(rs.getString(8));
			bean.setMenor(rs.getBoolean(9));
			bean.setAlergias(rs.getBoolean(10));
			bean.setAlergias_txt(rs.getString(11));
			bean.setAlimentos(rs.getBoolean(12));
			bean.setAlimentos_txt(rs.getString(13));
			bean.setObservaciones(rs.getString(14));

		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		e.printStackTrace();
		bean=null;
	}
		return bean;
	}
	
	public static int totalUsuarios()
	{

		int resultado=0;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionInscritos.getConexion();
		sentencia=con.prepareStatement("SELECT COUNT(*) FROM USUARIOS");
		ResultSet rs=sentencia.executeQuery();
		if (rs.next())
		{
			resultado=rs.getInt(1);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
		resultado=-1;
	}
	
	return resultado;
	}
	
	
	public static boolean checkExiste(UsuarioBean bean)
	{
		boolean resultado=false;
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("SELECT * FROM USUARIOS WHERE USUARIO=?");
			sentencia.setString(1,bean.getUsuario());
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
	
	
	public static boolean validarUsuario(String usuario,String password)
	{
		boolean resultado=false;
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("SELECT * FROM USUARIOS WHERE USUARIO=? AND PASSWORD=?");
			sentencia.setString(1,usuario.toUpperCase());
			sentencia.setString(2,password);
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
	
	
	public static boolean insertUsuario(UsuarioBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexionInscritos.getConexion();
			sentencia=con.prepareStatement("INSERT INTO USUARIOS (ID,USUARIO,PASSWORD,FECHA,FECHA_UPDATE,IDINSCRITO) VALUES (?,?,?,?,?,?)");
			sentencia.setString(1,bean.getId());
			sentencia.setString(2,bean.getUsuario());
			sentencia.setString(3,bean.getPassword());
			sentencia.setLong(4,System.currentTimeMillis());
			sentencia.setLong(5,System.currentTimeMillis());
			sentencia.setString(6, bean.getIdInscrito());
			
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
