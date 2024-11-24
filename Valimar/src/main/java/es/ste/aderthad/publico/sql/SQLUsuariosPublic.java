package es.ste.aderthad.publico.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import es.ste.aderthad.publico.data.UsuarioBean;
import es.ste.aderthad.publico.log.LoggerPublic;


public class SQLUsuariosPublic {
	
	public static UsuarioBean selectInscrito(String idInscrito)
	{
		UsuarioBean bean=null;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionPublic.getConexion();
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
		LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
		bean=null;
	}
		return bean;
	}
	
	public static int totalUsuarios()
	{

		int resultado=0;
		try{
		PreparedStatement sentencia;
		Connection con = SQLConexionPublic.getConexion();
		sentencia=con.prepareStatement("SELECT COUNT(*) from USUARIOS");
		ResultSet rs=sentencia.executeQuery();
		if (rs.next())
		{
			resultado=rs.getInt(1);
		}
		if (con!=null) con.close();
	}  catch (SQLException e) {
		resultado=-1;
	}
	
	return resultado;
	}
	
	
	public static boolean checkExiste(UsuarioBean bean)
	{
		boolean resultado=false;
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexionPublic.getConexion();
			sentencia=con.prepareStatement("SELECT * from USUARIOS WHERE USUARIO=?");
			sentencia.setString(1,bean.getUsuario());
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=true;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
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
			Connection con = SQLConexionPublic.getConexion();
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
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
}
