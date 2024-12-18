package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.ste.aderthad.properties.Entorno;

public class SQLConexion {

	public static Connection getConexion() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Conexión de BD -> " + 
			    "jdbc:mysql://" + Entorno.getVariable("MYSQL_HOST") + ":3307/" + Entorno.getVariable("MYSQL_DBNAME") + 
			    ", Usuario: " + Entorno.getVariable("MYSQL_USER") + 
			    ", Contraseña: " + Entorno.getVariable("MYSQL_PWD"));
		return DriverManager.getConnection("jdbc:mysql://"+Entorno.getVariable("MYSQL_HOST")+":3307/"+Entorno.getVariable("MYSQL_DBNAME"),Entorno.getVariable("MYSQL_USER"),Entorno.getVariable("MYSQL_PWD"));

}

}
