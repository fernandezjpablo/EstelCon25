package es.ste.aderthad.data;

import org.json.JSONObject;

public class ActividadBean {

	public String getIdActividad() {
		return idActividad;
	}
	public void setIdActividad(String idActividad) {
		this.idActividad = idActividad;
	}
	public String getNombreActividad() {
		return nombreActividad;
	}
	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}
	public String getResponsables() {
		return responsables;
	}
	public void setResponsables(String responsables) {
		this.responsables = responsables;
	}
	public String getNombres_responsables() {
		return nombres_responsables;
	}
	public void setNombres_responsables(String nombres_responsables) {
		this.nombres_responsables = nombres_responsables;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getAforo() {
		return aforo;
	}
	public void setAforo(int aforo) {
		this.aforo = aforo;
	}
	public long getFecha() {
		return fecha;
	}
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	public long getHora_inicio() {
		return hora_inicio;
	}
	public void setHora_inicio(long hora_inicio) {
		this.hora_inicio = hora_inicio;
	}
	public long getHora_fin() {
		return hora_fin;
	}
	public void setHora_fin(long hora_fin) {
		this.hora_fin = hora_fin;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getPublico() {
		return publico;
	}
	public void setPublico(String publico) {
		this.publico = publico;
	}
	private String idActividad;
	private String nombreActividad;
	private String responsables;
	private String nombres_responsables;
	private String descripcion;
	
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getRequisitos() {
		return requisitos;
	}
	public void setRequisitos(String requisitos) {
		this.requisitos = requisitos;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	private String observaciones;
	private String requisitos;
	private String tipo;
	
	private int aforo;
	private long fecha;
	private long hora_inicio;
	private long hora_fin;
	private int estado;
	private String publico;
	private boolean pagoAdicional;
	private String duracion;
	
	public boolean isPagoAdicional() {
		return pagoAdicional;
	}
	public void setPagoAdicional(boolean pagoAdicional) {
		this.pagoAdicional = pagoAdicional;
	}
	public String toJson()
	{
		JSONObject resultado=new JSONObject();
		resultado.put("idActividad", idActividad);
		resultado.put("nombreActividad", nombreActividad);
		resultado.put("responsables", responsables);
		resultado.put("nombres_responsables", nombres_responsables);
		
		resultado.put("descripcion", descripcion);
		resultado.put("aforo", aforo);
		resultado.put("fecha", fecha);
		resultado.put("hora_inicio", hora_inicio);
		resultado.put("hora_fin", hora_fin);
		resultado.put("estado", estado);
		resultado.put("publico", publico);		
		resultado.put("pagoAdicional", pagoAdicional);
		resultado.put("duracion", getDuracion());
		resultado.put("tipo",tipo);
		resultado.put("observaciones", observaciones);
		resultado.put("requisitos", requisitos);
		return resultado.toString();
	}
	public String getDuracion() {
		if (duracion==null) duracion="";
		return duracion;
	}
	public void setDuracion(String duracion) {
		if(duracion==null) duracion=""; 
		this.duracion = duracion;
	}
	
}
