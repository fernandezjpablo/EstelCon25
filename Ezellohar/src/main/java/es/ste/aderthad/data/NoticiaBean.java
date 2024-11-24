package es.ste.aderthad.data;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class NoticiaBean {
	
	public NoticiaBean(String id,String titulo,String cuerpo,long fecha,long fechaUpdate,int estado)
	{
		this.idNoticia=id;
		this.titulo=titulo;
		this.cuerpo=URLDecoder.decode(cuerpo,Charset.forName("UTF-8"));
		this.estado=estado;
		this.fecha=fecha;
		this.fechaUpdate=fechaUpdate;
	}
	private String formatearFecha(long valor)
	{
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return df.format(new Date(valor));
	}
	public String toJson()
	{
		JSONObject objeto=new JSONObject();
		objeto.put("idNoticia", idNoticia);
		objeto.put("titulo", titulo);
		objeto.put("cuerpo", cuerpo);
		objeto.put("fecha", formatearFecha(fecha));
		objeto.put("fechaUpdate", formatearFecha(fechaUpdate));
		objeto.put("estado", estado);
		return objeto.toString();
	}

	public String getIdNoticia() {
		return idNoticia;
	}
	public void setIdNoticia(String idNoticia) {
		this.idNoticia = idNoticia;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getCuerpoNoticia() {
		return cuerpo;
	}
	public void setCuerpoNoticia(String observaciones) {
		this.cuerpo = observaciones;
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
	public long getFechaUpdate() {
		return fechaUpdate;
	}
	public void setFechaUpdate(long fechaUpdate) {
		this.fechaUpdate = fechaUpdate;
	}
	private String idNoticia="";
	private String titulo="";
	private String cuerpo="";
	private int estado=0;
	private long fecha=0;
	private long fechaUpdate=0;
}
