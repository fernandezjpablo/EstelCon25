package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.ste.aderthad.inscritos.data.HabitacionBean;
import es.ste.aderthad.inscritos.data.HabitacionParcialBean;
import es.ste.aderthad.inscritos.log.LoggerInscritos;



public class SQLHabitacionesInscritos {
	public static HabitacionBean selectHabitacion(String idh) {
		HabitacionBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDENTIFICADOR,PLAZAS, PLANTA,HABITACIONES.ESTADO,HABITACIONES.OBSERVACIONES,CAMAS,PRECIO_ADULTO,PRECIO_MENOR FROM HABITACIONES  WHERE IDHABITACION='"+idh+"' ");
			
			if (rs.next())
			{
				habitacion=new HabitacionBean(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6),rs.getInt(5),rs.getInt(7),rs.getDouble(8),rs.getDouble(9));
			}
			else
			{
				habitacion=null;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
			habitacion=null;
		}
		return habitacion;
	}
	
	public static HabitacionParcialBean selectHabitacionParcial(String idh) {
		es.ste.aderthad.inscritos.data.HabitacionParcialBean habitacion;
			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDHABITACION,IDHABITACIONRAIZ,ESTADO FROM HABITACIONESPARCIALES  WHERE IDHABITACION='"+idh+"' ");
			
			if (rs.next())
			{
				habitacion=new HabitacionParcialBean(rs.getString(1),rs.getString(2),rs.getInt(3));
			}
			else
			{
				habitacion=null;
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			e.printStackTrace();
			habitacion=null;
		}
		return habitacion;
	}
}
