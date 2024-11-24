package es.ste.aderthad.publico.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import es.ste.aderthad.publico.data.InscritoBean;
import es.ste.aderthad.publico.log.LoggerPublic;


public class SQLInscritosPublic {
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
			Connection con = SQLConexionPublic.getConexion();
			sentencia=con.prepareStatement("SELECT * from INSCRITOS WHERE NIF=?");
			sentencia.setString(1,bean.getNif());
			ResultSet rs=sentencia.executeQuery();
			if (rs.next())
			{
				resultado=true;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
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
			Connection con = SQLConexionPublic.getConexion();
			sentencia=con.prepareStatement("INSERT INTO INSCRITOS (NOMBRE,APELLIDOS,PSEUDONIMO,NIF,EMAIL,TELEFONO,"
					+ "MENOR,FECHA,FECHAUPDATE,HABITACION,IDINSCRITO,CON_BEBES,ESTADO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,0)");

			sentencia.setString(1,bean.getNombre());
			sentencia.setString(2,bean.getApellido());
			sentencia.setString(3,bean.getPseudonimo());
			sentencia.setString(4,bean.getNif());
			sentencia.setString(5,bean.getEmail());
			sentencia.setString(6,bean.getTelefono());
			
			sentencia.setBoolean(7,bean.isMenor());
			
			sentencia.setLong(8,System.currentTimeMillis());
			sentencia.setLong(9,System.currentTimeMillis());
			sentencia.setString(10,bean.getHabitacion());
			sentencia.setString(11,bean.getId());
			sentencia.setString(12,bean.conBebes());
			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException e) {
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}
	
	

}
