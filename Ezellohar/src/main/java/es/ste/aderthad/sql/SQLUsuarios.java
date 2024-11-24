package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.UsuarioBean;
import es.ste.aderthad.log.Logger;


public class SQLUsuarios {
	
	public static UsuarioBean selectInscrito(String idInscrito)
	{
		UsuarioBean bean=null;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexion.getConexion();
		sentencia=con.prepareStatement("SELECT USUARIO,PASSWORD,ID,IDINSCRITO FROM USUARIOS WHERE IDINSCRITO='"+idInscrito+"'");
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
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		bean=null;
	}
		return bean;
	}
	public static int totalUsuarios()
	{

		int resultado=0;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexion.getConexion();
		sentencia=con.prepareStatement("SELECT COUNT(*) from USUARIOS");
		ResultSet rs=sentencia.executeQuery();
		if (rs.next())
		{
			resultado=rs.getInt(0);
		}
		if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		resultado=-1;
	}
	
	return resultado;
	}
	
	public static JSONArray selectDatosAcceso(String id)
	{
		JSONArray beanArr=new JSONArray();
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexion.getConexion();
		sentencia=con.prepareStatement("SELECT INSCRITOS.IDINSCRITO,NOMBRE,APELLIDOS, PSEUDONIMO,USUARIOS.USUARIO,USUARIOS.PASSWORD FROM INSCRITOS INNER JOIN USUARIOS ON INSCRITOS.IDINSCRITO = USUARIOS.IDINSCRITO AND INSCRITOS.IDINSCRITO=?");
		sentencia.setString(1, id);
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
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
	}
		return beanArr;
	}
	
	public static boolean checkExiste(UsuarioBean bean)
	{
		boolean resultado=false;
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("SELECT * from USUARIOS WHERE USUARIO=?");
			sentencia.setString(1,bean.getUsuario());
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
	
	
	public static boolean insertUsuario(UsuarioBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO USUARIOS (ID,USUARIO,PASSWORD,FECHA,FECHA_UPDATE,IDINSCRITO) VALUES (?,?,?,?,?,?)");
			sentencia.setString(1,bean.getId());
			sentencia.setString(2,bean.getUsuario());
			sentencia.setString(3,bean.getPassword());
			sentencia.setLong(4,System.currentTimeMillis());
			sentencia.setLong(5,System.currentTimeMillis());
			sentencia.setString(6, bean.getIdInscrito());
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static boolean updateUsuario(UsuarioBean bean)
	{
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("UPDATE USUARIOS SET USUARIO=?,PASSWORD=?,FECHA_UPDATE=? WHERE IDINSCRITO=?");
			sentencia.setString(1,bean.getUsuario());
			sentencia.setString(2,bean.getPassword());
			sentencia.setLong(3,System.currentTimeMillis());
			sentencia.setString(4, bean.getIdInscrito());
			
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	
	public static InscritoBean selectUsuario(String usuario)
	{
		InscritoBean bean=null;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexion.getConexion();
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
	}  catch (SQLException | ClassNotFoundException e) {
		Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		bean=null;
	}
		return bean;
	}
}

