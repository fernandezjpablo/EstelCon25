package es.ste.aderthad.sql;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.HabitacionBean;
import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.data.NoticiaBean;
import es.ste.aderthad.data.PagosBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;
import es.ste.aderthad.sql.SQLConexion;

public class SQLPagos {

	private static String componerSeleccion(String habitaciones)
	{
		String resultado=habitaciones.replaceAll(",","' OR IDINSCRITO='");
		
		resultado= "(IDINSCRITO='"+resultado+"')";
		return resultado;
	}
	
	private static String componerSeleccionMovimientos(String movimientos)
	{
		String resultado=movimientos.replaceAll(",","' OR IDPAGO='");
		
		resultado= "(IDPAGO='"+resultado+"')";
		return resultado;
	}
	
	public static boolean anularMovimientos(String movimientos)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!movimientos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE PAGOS SET ESTADO=99 WHERE "+componerSeleccionMovimientos(movimientos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean confirmarDevoluciones(String movimientos)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!movimientos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE PAGOS SET ESTADO=9 WHERE  "+componerSeleccionMovimientos(movimientos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	public static boolean recuperarIngresos(String movimientos)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!movimientos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE PAGOS SET ESTADO=1 WHERE (ESTADO=99) AND (IMPORTE>=0) AND "+componerSeleccionMovimientos(movimientos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean recuperarPagos(String movimientos)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!movimientos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE PAGOS SET ESTADO=2 WHERE (ESTADO=99) AND (IMPORTE<0) AND "+componerSeleccionMovimientos(movimientos));
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}
	
	public static boolean anularPagos(String inscritos,int estado)
	{
		//Estado 8 es pago anulado, estado 9 es devolución realizada
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!inscritos.equals(""))
			{
				Connection con=SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE PAGOS SET ESTADO=?, IMPORTE=(IMPORTE*-1) WHERE "+componerSeleccion(inscritos));
				sentencia.setInt(1,estado);
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
			return false;
		}
		
		return resultado;
	}
	public static boolean registrarPago(PagosBean bean)
	{
		boolean resultado=true;
		try{
				PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO PAGOS "
					+ "(IDPAGO,IDINSCRITO,IMPORTE,COMPLETO,OBSERVACIONES,FECHA,FECHA_UPDATE,ESTADO) "
					+ "VALUES (?,?,?,?,?,?,?,?)");
			sentencia.setString(1,bean.getIdPago());
			sentencia.setString(2, bean.getIdInscrito());
			sentencia.setDouble(3, bean.getImporte());
			sentencia.setBoolean(4, bean.isPagoCompleto());
			sentencia.setString(5, bean.getObservaciones());
			sentencia.setLong(6, bean.getFecha());
			sentencia.setLong(7,System.currentTimeMillis());
			sentencia.setInt(8, bean.getEstado());

			sentencia.executeUpdate();
			if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}

	public static JSONArray selectPagos(String filtro)
	{
		JSONArray resultados=new JSONArray();
		PagosBean movimiento;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDPAGO,IDINSCRITO,IMPORTE,COMPLETO,OBSERVACIONES,FECHA,FECHA_UPDATE,ESTADO FROM PAGOS ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				movimiento=new PagosBean();
				resultados.put(new JSONObject(movimiento.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static JSONArray selectPagos()
	{
		JSONArray resultados=new JSONArray();
		PagosBean movimiento;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDPAGO,IDINSCRITO,IMPORTE,COMPLETO,OBSERVACIONES,FECHA,FECHA_UPDATE,ESTADO FROM PAGOS ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				movimiento=new PagosBean();
				movimiento.setIdPago(rs.getString(1));
				movimiento.setIdInscrito(rs.getString(2));
				movimiento.setImporte(rs.getDouble(3));
				movimiento.setObservaciones(rs.getString(5));
				movimiento.setPagoCompleto(rs.getBoolean(4));
				movimiento.setFecha(rs.getLong(6));
				movimiento.setFechaUpdate(rs.getLong(7));
				movimiento.setEstado(rs.getInt(8));
				
				resultados.put(new JSONObject(movimiento.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}

	public static JSONArray selectPagosUsuarioActividad(String idInscrito,String idActividad)
	{
		JSONArray resultados=new JSONArray();
		PagosBean movimiento;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDPAGO,IDINSCRITO,IMPORTE,COMPLETO,OBSERVACIONES,FECHA,FECHA_UPDATE,ESTADO FROM PAGOS WHERE OBSERVACIONES LIKE '%Código operación:"+idInscrito+"@"+idActividad+"' ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				movimiento=new PagosBean();
				movimiento.setIdPago(rs.getString(1));
				movimiento.setIdInscrito(rs.getString(2));
				movimiento.setImporte(rs.getDouble(3));
				movimiento.setObservaciones(rs.getString(5));
				movimiento.setPagoCompleto(rs.getBoolean(4));
				movimiento.setFecha(rs.getLong(6));
				movimiento.setFechaUpdate(rs.getLong(7));
				movimiento.setEstado(rs.getInt(8));
				
				resultados.put(new JSONObject(movimiento.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static JSONArray selectPagosUsuario(String idInscrito)
	{
		JSONArray resultados=new JSONArray();
		PagosBean movimiento;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDPAGO,IDINSCRITO,IMPORTE,COMPLETO,OBSERVACIONES,FECHA,FECHA_UPDATE,ESTADO FROM PAGOS WHERE IDINSCRITO='"+idInscrito+"' ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				movimiento=new PagosBean();
				movimiento.setIdPago(rs.getString(1));
				movimiento.setIdInscrito(rs.getString(2));
				movimiento.setImporte(rs.getDouble(3));
				movimiento.setObservaciones(rs.getString(5));
				movimiento.setPagoCompleto(rs.getBoolean(4));
				movimiento.setFecha(rs.getLong(6));
				movimiento.setFechaUpdate(rs.getLong(7));
				movimiento.setEstado(rs.getInt(8));
				
				resultados.put(new JSONObject(movimiento.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static Double calcularPagosUsuario(String idInscrito)
	{
		Double resultado=0.0;

			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(IMPORTE) FROM PAGOS WHERE IDINSCRITO='"+idInscrito+"' AND ESTADO<>99 AND ESTADO<>8 AND ESTADO<>9");
			
			if (rs.next())
			{
				resultado=rs.getDouble(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public static String getBalance()
	{
		String resultado="";
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT ROUND(SUM(IMPORTE),2) FROM PAGOS WHERE ESTADO<>99 AND ESTADO<8");
			
			if (rs.next())
			{
				resultado=rs.getString(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
}
	

