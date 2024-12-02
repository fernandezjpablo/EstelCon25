package es.ste.aderthad.publico.plazas;

import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import es.ste.aderthad.publico.sql.SQLHabitacionesPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BloquearHabitacion")
public class BloquearHabitacion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(reader, JsonObject.class);

        String capacidad = json.get("capacidad").getAsString();
        String camas = json.get("camas").getAsString();

        System.out.println("Recibido capacidad: " + capacidad + ", camas: " + camas); // Log para depuración

        if (capacidad == null || camas == null) {
            System.out.println("Error: Parámetros 'capacidad' o 'camas' son nulos.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Error: Parámetros 'capacidad' o 'camas' son nulos.");
            return;
        }

        String idbloqueado = SQLHabitacionesPublic.BloqueoHabitacion(capacidad, camas);

        System.out.println("ID bloqueado: " + idbloqueado); // Log para depuración

        response.getWriter().print(idbloqueado);
    }
}