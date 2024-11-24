package es.ste.aderthad.inscritos.data;

import org.json.JSONObject;

public class InscripcionActividadBean {
private String idinscrito;
public String getIdinscrito() {
	return idinscrito;
}
public void setIdinscrito(String idinscrito) {
	this.idinscrito = idinscrito;
}
public String getIdactividad() {
	return idactividad;
}
public void setIdactividad(String idactividad) {
	this.idactividad = idactividad;
}
public int getEstado() {
	return estado;
}
public void setEstado(int estado) {
	this.estado = estado;
}
public long getFecha() {
	return fecha;
}
public void setFecha(long fecha) {
	this.fecha = fecha;
}
private String idactividad;

private String observaciones;

public String getObservaciones() {
	return observaciones;
}
public void setObservaciones(String observaciones) {
	this.observaciones = observaciones;
}
private int estado;//0 inicial 1 inscrito 3 pendiente de pago 4 inscrito pagado
private long fecha;

public String toJson()
{
	JSONObject objeto=new JSONObject();
	objeto.put("idinscrito", idinscrito);
	objeto.put("idactividad", idactividad);
	objeto.put("estado", estado);
	objeto.put("fecha", fecha);
	return objeto.toString();
}

public InscripcionActividadBean(String usuario,String actividad,int est,long f)
{
	idinscrito=usuario;
	idactividad=actividad;
	estado=est;
	fecha=f;
}

public InscripcionActividadBean(String usuario,String actividad,int est,long f,String o)
{
	idinscrito=usuario;
	idactividad=actividad;
	estado=est;
	fecha=f;
	observaciones=o;
}

}
