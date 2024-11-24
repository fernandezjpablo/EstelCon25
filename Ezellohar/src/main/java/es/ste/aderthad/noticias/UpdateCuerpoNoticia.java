package es.ste.aderthad.noticias;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.json.JSONObject;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLNoticias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/admin/noticias/UpdateCuerpoNoticia")
public class UpdateCuerpoNoticia extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdateCuerpoNoticia() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		String datos=URLDecoder.decode(request.getParameter("data"),"UTF-8");
		
		String resultado="ok";
		try
		{
			SQLNoticias.actualizarCuerpoNoticia(id,datos);
			Logger.registrarActividadSession(request, "Actualizando noticia "+id+".");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
