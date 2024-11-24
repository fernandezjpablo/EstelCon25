package es.ste.aderthad.inscritos.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.ste.aderthad.inscritos.log.LoggerInscritos;



public class SQLPagosInscritos {
	public static Double calcularPagosUsuario(String idInscrito)
	{
		Double resultado=0.0;

			try{
			Statement sentencia;
			Connection con=SQLConexionInscritos.getConexion();
			sentencia=con.createStatement();
			
			ResultSet rs=sentencia.executeQuery("SELECT SUM(IMPORTE) FROM PAGOS WHERE IDINSCRITO='"+idInscrito+"' AND ESTADO<>99 AND ESTADO<>8");
			
			if (rs.next())
			{
				resultado=rs.getDouble(1);
			}
			if (con!=null) con.close();
		}  catch (SQLException e) {LoggerInscritos.GenerarEntradaLogError(e, LoggerInscritos.getFileNameErrorLog());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
}
