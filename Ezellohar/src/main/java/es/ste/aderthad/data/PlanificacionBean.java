package es.ste.aderthad.data;

import org.json.JSONObject;

public class PlanificacionBean {

	public String getIdPlanificacion() {
		return fecha+";"+espacio+";"+String.valueOf(intervalo);
	}
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getEspacio() {
		return espacio;
	}
	public void setEspacio(String espacio) {
		this.espacio = espacio;
	}
	public String getActividad() {
		return actividad;
	}
	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getIntervalo() {
		return intervalo;
	}
	public void setIntervalo(int intervalo) {
		this.intervalo = intervalo;
	}
	private String fecha;
	private String espacio;
	private String actividad;
	public String getNombreActividad() {
		return nombreActividad;
	}

	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}
	private String nombreActividad;
	private String color;
	private int intervalo;
	
	
}
