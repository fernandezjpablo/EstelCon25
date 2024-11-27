package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.ste.aderthad.inscritos.log.LoggerInscritos;
import es.ste.aderthad.inscritos.properties.EntornoInscritos;

public class SQLConexionInscritos {

	public static Connection getConexion()  {
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection("jdbc:mysql://"+EntornoInscritos.getVariable("MYSQL_HOST")+":3306/"+EntornoInscritos.getVariable("MYSQL_DBNAME"),EntornoInscritos.getVariable("MYSQL_USER"),EntornoInscritos.getVariable("MYSQL_PWD"));
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Sin conexión en Conexión Inscritos "+e.toString());
			LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			conexion=null;
		}
		
		return conexion;

}

}
