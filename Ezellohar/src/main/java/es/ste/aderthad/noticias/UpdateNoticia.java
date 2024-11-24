package es.ste.aderthad.noticias;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONObject;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.sql.SQLNoticias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/admin/noticias/UpdateNoticia")
public class UpdateNoticia extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdateNoticia() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is=request.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
		String linea;
		StringBuilder sb=new StringBuilder();
		while ((linea=br.readLine())!=null)
		{
			sb.append(linea);
		}
		JSONObject objeto=new JSONObject(sb.toString());
		String titulo=URLDecoder.decode(objeto.getString("titulo"), "UTF-8");
		String cuerpo=objeto.getString("cuerpo");
		String id=objeto.getString("id");
		String resultado="ok";
		try
		{
			SQLNoticias.actualizarNoticia(id,titulo,cuerpo);
			Logger.registrarActividadSession(request, "Actualizando noticia "+titulo+"("+id+").");
		}
		catch (Exception e)
		{
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			resultado=e.getMessage();
		}
		response.getWriter().println("{\"resultado\":\""+resultado+"\"}");
	}

}
