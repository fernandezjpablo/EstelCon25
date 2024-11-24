package es.ste.aderthad.inscritos.data;

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
	
		
	
}
