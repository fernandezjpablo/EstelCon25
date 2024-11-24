package es.ste.aderthad.noticias;

import java.io.IOException;

import es.ste.aderthad.sql.SQLNoticias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ListNoticiasPublish
 */
@WebServlet("/admin/noticias/ListNoticiasPublish")
public class ListNoticiasPublish extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ListNoticiasPublish() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.getWriter().println(SQLNoticias.selectNoticias().toString());
	}


}
