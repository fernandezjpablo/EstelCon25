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
import es.ste.aderthad.data.NoticiaBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;

public class SQLNoticias {
	public static JSONArray selectNoticias()
	{
		JSONArray resultados=new JSONArray();
		NoticiaBean noticia;
			try{
			Statement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.createStatement();
			String url="jdbc:msql://"+Entorno.getVariable("MYSQL_HOST")+":3306/";
			
			ResultSet rs=sentencia.executeQuery("SELECT IDNOTICIA,TITULO,CUERPO,FECHA,FECHAUPDATE,ESTADO FROM NOTICIAS WHERE ESTADO=1 ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				noticia=new NoticiaBean(rs.getString(1),rs.getString(2),rs.getString(3),rs.getLong(4),rs.getLong(5),rs.getInt(6));
				resultados.put(new JSONObject(noticia.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static String getCuerpoNoticia(String id)
	{
		 Clob resultados = null;
		 String respuesta=null;
		NoticiaBean noticia;
			try{
			Statement sentencia;
			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT CUERPO FROM NOTICIAS WHERE IDNOTICIA='"+id+"'");
			if (rs.next())
			{
				InputStream is=rs.getAsciiStream(1);
				BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
				StringBuilder sb=new StringBuilder();
				String linea;
				while ((linea=br.readLine())!=null)
				{
					sb.append(linea);
				}
				respuesta=sb.toString();
			}

			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException | IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		
		return respuesta;
	}
	
	private static String convertir(InputStream is)  {
		String respuesta="";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			StringBuilder sb=new StringBuilder();
			String linea;
			while ((linea=br.readLine())!=null)
			{
				sb.append(linea);
			}
			respuesta=sb.toString().replace("+", "%2B");
		} catch (IOException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			respuesta="";
		}

		return respuesta;
	}
	public static JSONArray selectNoticiasAdmin()
	{
		JSONArray resultados=new JSONArray();
		NoticiaBean noticia;
			try{
			Statement sentencia;

			Connection con=SQLConexion.getConexion();
			sentencia=con.createStatement();

			
			ResultSet rs=sentencia.executeQuery("SELECT IDNOTICIA,TITULO,CUERPO,FECHA,FECHAUPDATE,ESTADO FROM NOTICIAS ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				noticia=new NoticiaBean(rs.getString(1),rs.getString(2),convertir(rs.getAsciiStream(3)),rs.getLong(4),rs.getLong(5),rs.getInt(6));
				resultados.put(new JSONObject(noticia.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}



	public static String actualizarNoticia(String id,String titulo,String cuerpo)
	{
		String resultado="{\"resultado\":\"ok\"}";
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();

			
			sentencia=con.prepareStatement("UPDATE NOTICIAS SET TITULO=?, CUERPO=?, FECHAUPDATE=? WHERE IDNOTICIA=?");
			sentencia.setString(1, titulo);
			sentencia.setString(2, cuerpo);
			sentencia.setLong(3, System.currentTimeMillis());
			sentencia.setString(4, id);
			sentencia.executeUpdate();
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado="{\"resultado\":\""+e.getLocalizedMessage()+"\"}";
		}
		return resultado;
	}
	
	public static String actualizarCuerpoNoticia(String id,String cuerpo)
	{
		String resultado="{\"resultado\":\"ok\"}";
			try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			
			sentencia=con.prepareStatement("UPDATE NOTICIAS SET CUERPO=? WHERE IDNOTICIA=?");
			sentencia.setString(1, cuerpo);
			sentencia.setString(2,id);
			sentencia.executeUpdate();
			if (con!=null) con.close();
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado="{\"resultado\":\""+e.getLocalizedMessage()+"\"}";
		}
		return resultado;
	}
	
	public static boolean generarNoticia(String titulo, String cuerpo) {
		boolean resultado=true;
		try{
			PreparedStatement sentencia;
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("INSERT INTO NOTICIAS (IDNOTICIA,TITULO,CUERPO,FECHA,FECHAUPDATE,ESTADO) VALUES (?,?,?,?,?,0)");

				sentencia.setString(1, UUID.randomUUID().toString());
				sentencia.setString(2, titulo);
				sentencia.setString(3, cuerpo);
				sentencia.setLong(4,System.currentTimeMillis());
				sentencia.setLong(5, System.currentTimeMillis());
				sentencia.executeUpdate();
				if (con!=null) con.close();
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado=false;
	}
		
		return resultado;
	}

	
	private static String componerSeleccion(String habitaciones)
	{
		String resultado=habitaciones.replaceAll(",","' OR IDNOTICIA='");
		
		resultado= "(IDNOTICIA='"+resultado+"')";
		return resultado;
	}
	

	public static boolean actualizarEstadoNoticias(String listaNoticias,String estado)
	{
		boolean resultado=true;
			try{
			PreparedStatement sentencia;
			if (!listaNoticias.equals(""))
			{
				Connection con = SQLConexion.getConexion();
				sentencia=con.prepareStatement("UPDATE NOTICIAS SET ESTADO=?, FECHAUPDATE=? WHERE "+componerSeleccion(listaNoticias));
				sentencia.setString(1, estado);
				sentencia.setLong(2, System.currentTimeMillis());
				sentencia.executeUpdate();
				if (con!=null) con.close();
			}
		}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			// TODO Auto-generated catch block
			return false;
		}
		
		return resultado;
	}



	public static boolean eliminarNoticias(String listaNoticias) {
		boolean resultado=true;
		try{
		PreparedStatement sentencia;
		if (!listaNoticias.equals(""))
		{
			Connection con = SQLConexion.getConexion();
			sentencia=con.prepareStatement("DELETE FROM NOTICIAS where "+componerSeleccion(listaNoticias));
			sentencia.executeUpdate();
			if (con!=null) con.close();
		}
	}  catch (SQLException | ClassNotFoundException e) {Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
		e.printStackTrace();
		resultado= false;
	}
	
	return resultado;
	}
}
