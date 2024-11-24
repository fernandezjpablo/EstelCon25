package es.ste.aderthad.inscritos.data;

import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class HabitacionBean {

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public int getCamas() {
		return camas;
	}

	public void setCamas(int cam) {
		this.camas = cam;
	}

	private String id="";
	private String identificador="";
	private int plazas=0;
	private String planta="";
	private String observaciones="";
	private int estado=0; //Libre
	private int camas=0; //camas < plazas = cama doble
	public double getPrecioAdultos() {
		return precioAdultos;
	}

	public void setPrecioAdultos(double precioAdultos) {
		this.precioAdultos = precioAdultos;
	}

	public double getPrecioMenores() {
		return precioMenores;
	}

	public void setPrecioMenores(double precioMenores) {
		this.precioMenores = precioMenores;
	}

	private double precioAdultos=0;
	private double precioMenores=0;
	
	public HabitacionBean()
	{
		id="";
		plazas=0;
		observaciones="";
		estado=0;
		planta="";
		identificador="";
		camas=0;
		precioMenores=0;
		precioAdultos=0;
	}
	
	public HabitacionBean(String i,String iden,int plaz,String floor,String obser,int status,int cam,double precioA,double precioM)
	{
		id=i;
		identificador=iden;
		plazas=plaz;
		planta=floor;
		observaciones=URLDecoder.decode(obser,Charset.forName("UTF-8"));
		estado=status;
		camas= cam;
		precioMenores=precioM;
		precioAdultos=precioA;
	}
	
	public String toJson()
	{
		JSONObject resultado=new JSONObject();
		resultado.put("id",id);
		resultado.put("identificador",identificador);
		resultado.put("plazas",plazas);
		resultado.put("observaciones",observaciones);
		resultado.put("estado",estado);
		resultado.put("planta",planta);
		resultado.put("camas", camas);
		resultado.put("precioAdultos", precioAdultos);
		resultado.put("precioMenores", precioMenores);
		return resultado.toString();
	}

	
}
