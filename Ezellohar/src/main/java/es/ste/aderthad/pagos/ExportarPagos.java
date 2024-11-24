package es.ste.aderthad.pagos;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.data.InscritoBean;
import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLInscritos;
import es.ste.aderthad.sql.SQLPagos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ExportarPagos
 */
@WebServlet("/admin/pagos/ExportarPagos")
public class ExportarPagos extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ExportarPagos() {
        // TODO Auto-generated constructor stub
    }

    private String parsear(int estado)
    {
    	String estadoStr=String.valueOf(estado);
		if (estado==1)
		{
			estadoStr="Movimiento realizado";
		}
		else if (estado==2)
		{
			estadoStr="Pago Realizado";
		}
		else if (estado==8)
		{
			estadoStr="Devolución pendiente";
		}
		else if (estado==99)
		{
			estadoStr="Movimiento anulado";
		}
		else if (estado==9)
		{
			estadoStr="Devolución realizada";
		}

    		return estadoStr;

    }
    private String formatearListado(JSONArray lista)
    {
    		StringBuilder sb=new StringBuilder();
    		JSONObject objPago;
    		SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    		sb.append("IDPAGO;TIPO DE MOVIMIENTO;IDINSCRITO;NOMBRE;APELLIDOS;NIF;IMPORTE;CONCEPTO;FECHA DE EJECUCIÓN;ULTIMA ACTUALIZACIÓN;\n");
    		for (int a=0;a<lista.length();a++)
    		{
    			objPago=lista.getJSONObject(a);
    			sb.append(objPago.getString("idPago")+";");
    			sb.append(parsear(objPago.getInt("estado"))+";");
    			sb.append(objPago.getString("idInscrito")+";");
    			InscritoBean bean=SQLInscritos.select(objPago.getString("idInscrito"));
    			sb.append(bean.getNombre()+";");
    			sb.append(bean.getApellido()+";");
    			sb.append(bean.getNif()+";");
    			sb.append(objPago.getInt("importe")+";");
    			sb.append(objPago.getString("observaciones")+";");
    			sb.append(objPago.getString("fecha")+";");
    			sb.append(objPago.getString("fechaUpdate")+";\n");

    		}
    		return sb.toString();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray listado=SQLPagos.selectPagos();
		Logger.registrarActividadSession(request, "Exportando movimientos de dinero a excel.");
		SimpleDateFormat df =new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		response.setCharacterEncoding("ISO-8859-15");
		response.setHeader("Content-Disposition", "attachment; filename=\"pagos-estelcon"+df.format(new Date())+".csv\"");
		response.getWriter().append(formatearListado(listado));
	}



}
