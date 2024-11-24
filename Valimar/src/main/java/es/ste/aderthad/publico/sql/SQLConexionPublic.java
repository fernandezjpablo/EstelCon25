package es.ste.aderthad.publico.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.ste.aderthad.publico.log.LoggerPublic;
import es.ste.aderthad.publico.properties.EntornoPublic;

public class SQLConexionPublic {

	public static Connection getConexion()  {
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection("jdbc:mysql://"+EntornoPublic.getVariable("MYSQL_HOST")+":3306/"+EntornoPublic.getVariable("MYSQL_DBNAME"),EntornoPublic.getVariable("MYSQL_USER"),EntornoPublic.getVariable("MYSQL_PWD"));
		} catch (ClassNotFoundException | SQLException e) {
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			// TODO Auto-generated catch block
			conexion=null;
		}
		
		return conexion;

}

}
