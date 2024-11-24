package es.ste.aderthad.data;

import org.json.JSONObject;

public class HabitacionParcialBean {

	public String getIdHabitacion() {
		return idHabitacion;
	}
	public void setIdHabitacion(String idHabitacion) {
		this.idHabitacion = idHabitacion;
	}
	public String getIdHabitacionRaiz() {
		return idHabitacionRaiz;
	}
	public void setIdHabitacionRaiz(String idHabitacionRaiz) {
		this.idHabitacionRaiz = idHabitacionRaiz;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	private String idHabitacion;
	private String idHabitacionRaiz;
	private int estado;
	
	public HabitacionParcialBean(String idH,String idHR,int estad)
	{
		idHabitacion=idH;
		idHabitacionRaiz=idHR;
		estado=estad;
	}
	public HabitacionParcialBean()
	{
		idHabitacion="";
		idHabitacionRaiz="";
		estado=0;
	}
	
	public String toJson()
	{
		JSONObject resultado=new JSONObject();
		resultado.put("idHabitacion",idHabitacion);
		resultado.put("idRaiz",idHabitacionRaiz);
		resultado.put("estado",estado);
		return resultado.toString();
	}
	
		
	
}
