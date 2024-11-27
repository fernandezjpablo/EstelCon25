package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.ste.aderthad.properties.Entorno;

public class SQLConexion {

	public static Connection getConexion() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://"+Entorno.getVariable("MYSQL_HOST")+":3306/"+Entorno.getVariable("MYSQL_DBNAME"),Entorno.getVariable("MYSQL_USER"),Entorno.getVariable("MYSQL_PWD"));

}

}
