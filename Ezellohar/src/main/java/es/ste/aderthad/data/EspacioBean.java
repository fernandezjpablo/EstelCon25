package es.ste.aderthad.data;

import org.json.JSONObject;

public class EspacioBean {

	public String getIdEspacio() {
		return idEspacio;
	}
	public void setIdEspacio(String idEspacio) {
		this.idEspacio = idEspacio;
	}
	public String getNombreEspacio() {
		return nombreEspacio;
	}
	public void setNombreEspacio(String nombreEspacio) {
		this.nombreEspacio = nombreEspacio;
	}
	public int getAforo() {
		return aforo;
	}
	public void setAforo(int aforo) {
		this.aforo = aforo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getPlanta() {
		return planta;
	}
	public void setPlanta(String planta) {
		this.planta = planta;
	}
	private String idEspacio;
	private String nombreEspacio;
	private int aforo;
	private String descripcion;
	private int estado;
	private String planta;
	
	public String toJson()
	{
		JSONObject resultado=new JSONObject();
		resultado.put("idEspacio", idEspacio);
		resultado.put("nombreEspacio", nombreEspacio);
		resultado.put("aforo", aforo);
		resultado.put("descripcion", descripcion);
		resultado.put("planta", planta);
		resultado.put("estado", estado);//actualmente el estado no tiene uso en los espacios
		
		return resultado.toString();
	}
}
