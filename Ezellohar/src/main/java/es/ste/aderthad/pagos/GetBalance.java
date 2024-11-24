package es.ste.aderthad.pagos;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONArray;

import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLPagos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetBalance
 */
@WebServlet("/admin/pagos/GetBalance")
public class GetBalance extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetBalance() {
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.getWriter().println(SQLPagos.getBalance());

	}

}
