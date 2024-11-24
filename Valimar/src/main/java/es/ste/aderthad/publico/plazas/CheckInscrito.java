package es.ste.aderthad.publico.plazas;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.publico.data.InscritoBean;
import es.ste.aderthad.publico.sql.SQLInscritosPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckInscrito
 */
@WebServlet("/CheckInscrito")
public class CheckInscrito extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public CheckInscrito() {
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String datos=request.getParameter("datos");
		JSONArray datosArr=new JSONArray(datos);
		boolean esmenor=false;
		InscritoBean bean;
		JSONArray resultado=new JSONArray();
		JSONObject item;
		for (int i=0;i<datosArr.length();i++)
		{
		item=new JSONObject();
		bean=new InscritoBean();
		bean.setNif(datosArr.getJSONObject(i).getString("iden"));
		if (datosArr.getJSONObject(i).has("menor"))esmenor=datosArr.getJSONObject(i).getBoolean("menor");
		boolean existe=false;
		if (!esmenor) existe=SQLInscritosPublic.checkExiste(bean);
		item.put("clave",datosArr.getJSONObject(i).getString("iden"));
		item.put("existe",existe);
		resultado.put(item);
		}
		response.getWriter().println(resultado.toString());
	}

}
