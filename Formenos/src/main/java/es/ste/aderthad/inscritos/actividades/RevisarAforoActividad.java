package es.ste.aderthad.inscritos.actividades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import es.ste.aderthad.inscritos.data.InscripcionActividadBean;
import es.ste.aderthad.inscritos.data.PlanificacionBean;
import es.ste.aderthad.inscritos.sql.SQLActividades;
import es.ste.aderthad.inscritos.sql.SQLInscripcionesActividades;
import es.ste.aderthad.inscritos.sql.SQLPlanificacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class RevisarAforoActividad
 */
@WebServlet("/RevisarAforoActividad")
public class RevisarAforoActividad {

	public static boolean revisarAforo(String idActividad) {

		int aforo=0;
	boolean resultado=true;
		int ocupacion=SQLActividades.getOcupacion(idActividad);
		int espera=SQLActividades.getOcupacionListaEspera(idActividad);
		int vacantes=0;
		int recuperables=0;
		JSONObject actividad=SQLActividades.consultarActividad(idActividad);
		aforo=actividad.getInt("aforo");
		if ((aforo>0) && (aforo>ocupacion))
		{
			//Han quedado huecos
			if (espera>0)
			{
				//Hay lista de espera
				vacantes=aforo-ocupacion;
				recuperables=vacantes;
				if (recuperables>espera) recuperables=espera;
				resultado=SQLInscripcionesActividades.recuperar(idActividad,recuperables);
				
			}
		}
		return resultado;
	}

}
