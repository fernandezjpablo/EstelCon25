package es.ste.aderthad.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class PagosBean {

	public String getIdPago() {
		return idPago;
	}
	public void setIdPago(String idPago) {
		this.idPago = idPago;
	}
	public String getIdInscrito() {
		return idInscrito;
	}
	public void setIdInscrito(String idInscrito) {
		this.idInscrito = idInscrito;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
	public boolean isPagoCompleto() {
		return pagoCompleto;
	}
	public void setPagoCompleto(boolean pagoCompleto) {
		this.pagoCompleto = pagoCompleto;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public long getFecha() {
		return fecha;
	}
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	public long getFechaUpdate() {
		return fechaUpdate;
	}
	public void setFechaUpdate(long fechaUpdate) {
		this.fechaUpdate = fechaUpdate;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	private String idPago;
	private String idInscrito;
	private double importe;
	private boolean pagoCompleto;
	private String observaciones;
	private long fecha;
	private long fechaUpdate;
	private int estado;
	public String getIdactividad() {
		return idactividad;
	}
	public void setIdactividad(String idactividad) {
		this.idactividad = idactividad;
	}
	private String idactividad;//Cuando el pago sea un pago adicional de actividad
	
	public PagosBean()
	{
		idPago="";
		idInscrito="";
		importe=0;
		pagoCompleto=false;
		observaciones="";
		fecha=System.currentTimeMillis();
		fechaUpdate=System.currentTimeMillis();
		estado=0;
		idactividad="";
	}
	
	public PagosBean(String idp,String idi,double imp,boolean comp,String observ,long fec,long fecUp,int estad,String idActividad)
	{
		idPago=idp;
		idInscrito=idi;
		importe=imp;
		pagoCompleto=comp;
		observaciones=observ;
		fecha=fec;
		fechaUpdate=fecUp;
		estado=estad;
		idactividad=idActividad;
	}
	
	public String toJson()
	{
		SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		
		JSONObject objeto=new JSONObject();
		objeto.put("idPago",idPago);
		objeto.put("idInscrito", idInscrito);
		objeto.put("importe", importe);
		objeto.put("pagoCompleto", pagoCompleto);
		objeto.put("observaciones", observaciones);
		objeto.put("fecha", df.format(new Date(fecha)));
		objeto.put("fechaUpdate", df.format(new Date(fechaUpdate)));
		objeto.put("estado", estado);
		objeto.put("idactividad", idactividad);
		return objeto.toString();
		
	}
	
	
}
