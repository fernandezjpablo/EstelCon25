package es.ste.aderthad.publico.plazas;

import java.io.IOException;

import es.ste.aderthad.publico.sql.SQLHabitacionesPublic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BloquearHabitacion
 */
@WebServlet("/BloquearHabitacion")
public class BloquearHabitacion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public BloquearHabitacion() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String capacidad = request.getParameter("capacidad");
        String camas = request.getParameter("camas");

        System.out.println("Recibido capacidad: " + capacidad + ", camas: " + camas); // Log para depuración

        if (capacidad == null || camas == null) {
            System.out.println("Error: Parámetros 'capacidad' o 'camas' son nulos.");
        } else {
            System.out.println("Parámetros válidos recibidos.");
        }

        String idbloqueado = SQLHabitacionesPublic.BloqueoHabitacion(capacidad, camas);

        System.out.println("ID bloqueado: " + idbloqueado); // Log para depuración

        response.getWriter().print(idbloqueado);
    }
}