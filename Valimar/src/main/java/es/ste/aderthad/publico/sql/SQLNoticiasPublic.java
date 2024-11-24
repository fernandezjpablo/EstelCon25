package es.ste.aderthad.publico.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.publico.data.NoticiaBean;
import es.ste.aderthad.publico.log.LoggerPublic;


public class SQLNoticiasPublic {
	
	public static String getCuerpoNoticia(String id)
	{
		 Clob resultados = null;
		 String respuesta=null;
		NoticiaBean noticia;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
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
		}  catch (SQLException | IOException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
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
			respuesta="";
		}

		return respuesta;
	}
	
	public static JSONObject selectNoticia(String id)
	{
		JSONObject resultados=null;
		NoticiaBean noticia;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDNOTICIA,TITULO,CUERPO,FECHA,FECHAUPDATE,ESTADO FROM NOTICIAS WHERE ESTADO=1 ORDER BY FECHA DESC");
			
			if (rs.next())
			{
				noticia=new NoticiaBean(rs.getString(1),rs.getString(2),convertir(rs.getAsciiStream(3)),rs.getLong(4),rs.getLong(5),rs.getInt(6));
				resultados=new JSONObject(noticia.toJson());
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
	public static JSONArray selectNoticias()
	{
		JSONArray resultados=new JSONArray();
		NoticiaBean noticia;
			try{
			Statement sentencia;
			Connection con=SQLConexionPublic.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT IDNOTICIA,TITULO,CUERPO,FECHA,FECHAUPDATE,ESTADO FROM NOTICIAS WHERE ESTADO=1 ORDER BY FECHA DESC");
			
			while (rs.next())
			{
				noticia=new NoticiaBean(rs.getString(1),rs.getString(2),convertir(rs.getAsciiStream(3)),rs.getLong(4),rs.getLong(5),rs.getInt(6));
				resultados.put(new JSONObject(noticia.toJson()));
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultados;
	}
	
}
