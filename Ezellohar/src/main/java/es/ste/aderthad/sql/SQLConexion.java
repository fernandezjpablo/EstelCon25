package es.ste.aderthad.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.ste.aderthad.properties.Entorno;

public class SQLConexion {

    public static Connection getConexion() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String host = Entorno.getVariable("MYSQL_HOST");
        String dbName = Entorno.getVariable("MYSQL_DBNAME");
        String user = Entorno.getVariable("MYSQL_USER");
        String password = Entorno.getVariable("MYSQL_PWD");
        System.out.println("Conexión de BD -> jdbc:mysql://" + host + ":3306/" + dbName + ", Usuario: " + user + ", Contraseña: " + password);
        return DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + dbName, user, password);
    }
}