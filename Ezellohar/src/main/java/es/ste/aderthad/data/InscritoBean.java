package es.ste.aderthad.data;

import org.json.JSONObject;

import es.ste.aderthad.sql.SQLHabitaciones;
import es.ste.aderthad.sql.SQLPagos;

public class InscritoBean {

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelegram() {
		return telegram;
	}
	public void setTelegram(String telegram) {
		this.telegram = telegram;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPseudonimo() {
		return pseudonimo;
	}
	public void setPseudonimo(String pseudonimo) {
		this.pseudonimo = pseudonimo;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public boolean isMenor() {
		return menor;
	}
	public void setMenor(boolean menor) {
		this.menor = menor;
	}
	public boolean isAlimentos() {
		return alimentos;
	}
	public void setAlimentos(boolean alimentos) {
		this.alimentos = alimentos;
	}
	public String getAlimentos_txt() {
		return alimentos_txt;
	}
	public void setAlimentos_txt(String alimentos_txt) {
		this.alimentos_txt = alimentos_txt;
	}
	public boolean isAlergias() {
		return alergias;
	}
	public void setAlergias(boolean alergias) {
		this.alergias = alergias;
	}
	public String getAlergias_txt() {
		return alergias_txt;
	}
	public void setAlergias_txt(String alergias_txt) {
		this.alergias_txt = alergias_txt;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	private String nombre;
	 private String apellido;
	 private String telefono;
	 private String email;
	 private String telegram;
	 private String nif;
	 private String id;
	 private String pseudonimo;
	 private int estado;
	 private boolean menor;
	 private boolean alimentos;
	 private String alimentos_txt;
	 private boolean alergias;
	 private String alergias_txt;
	 private String observaciones;
	 private String habitacion;
	 private String con_bebes;
	 private String smial;
	 
	 public HabitacionBean getHabitacionObj() {
		return habitacionObj;
	}
	public void setHabitacionObj(HabitacionBean habitacionObj) {
		this.habitacionObj = habitacionObj;
	}
	private HabitacionBean habitacionObj;
	 
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
	private long fecha;
	 private long fechaUpdate;
	 
	 public boolean isGrupal() {
		return grupal;
	}
	public void setGrupal(boolean grupal) {
		this.grupal = grupal;
	}
	private boolean grupal;
	 
	 public void setHabitacion(String hab)
	 {
		 habitacion=hab;
	 }
	 
	 public String getHabitacion()
	 {
		 return habitacion;
	 }
	 
	 private String formatear(int idn)
	 {
		 String idr=String.valueOf(idn);
		 idr="0000000"+idr;
		 idr.substring(idr.length()-4, idr.length());
		 return idr;
	 }
	 public InscritoBean()
	 {
		 nombre="";
		 apellido="";
		 telefono="";
		 email="";
		 telegram="";
		 nif="";
		 id="";
		 pseudonimo="";
		 estado=0;		
		 menor=false;
		 alimentos=false;
		 alergias=false;
		 alergias_txt="";
		 observaciones="";
		 alimentos_txt="";
		 habitacion="";
		 con_bebes="0";
		 smial="";
	 }
	 public InscritoBean(int idn,String nom, String apellidos, String telf,String em,String telg,String id_l,String pseudon,int estad, boolean men,boolean alim,String alerStr,boolean aler,String alimStr,String obser,String hab,boolean grup,String cb,String smi)
	 {
		 nombre=nom;
		 apellido=apellidos;
		 telefono=telf;
		 email=em;
		 telegram=telg;
		 nif=id_l;
		 id="EC-"+formatear(idn);
		 pseudonimo=pseudon;
		 estado=estad;		
		 menor=men;
		 alimentos=alim;
		 alergias=aler;
		 alergias_txt=alerStr;
		 observaciones=obser;
		 alimentos_txt=alimStr;
		 habitacion=hab;
		 grupal=grup;
		 con_bebes=cb;
		 smial=smi;
	 }
	 
	 public String toJson()
	 {
		 if (observaciones==null) observaciones="";
		 JSONObject objeto=new JSONObject();
		 objeto.put("nombre", nombre);
		 objeto.put("apellidos", apellido);
		 objeto.put("telefono", telefono);
		 objeto.put("telegram", telegram);
		 objeto.put("email", email);
		 objeto.put("nif", nif);
		 objeto.put("id", id);
		 objeto.put("pseudonimo", pseudonimo);
		 objeto.put("estado", estado);
		 objeto.put("menor", menor);
		 objeto.put("alimentos", alimentos);
		 objeto.put("alergias", alergias);
		 objeto.put("alimentosTxt", alimentos_txt);
		 objeto.put("alergiasTxt", alergias_txt);
		 objeto.put("observaciones", observaciones);
		 objeto.put("habitacion", habitacion);
		 objeto.put("grupal", grupal);
		 objeto.put("fecha", fecha);
		 objeto.put("fechaUpdate", fechaUpdate);
		 objeto.put("con_bebes", con_bebes);
		 objeto.put("smial", smial);
		 if (habitacionObj!=null) 
		 {
			 objeto.put("habitacionObj", new JSONObject(habitacionObj.toJson()));
		 }
		 else
		 {
			 objeto.put("habitacionObj", new JSONObject());
		 }
		 return objeto.toString();
	 }

		public String calcularImporte()
		{
			String resultado="";
			HabitacionBean habitacion=SQLHabitaciones.selectHabitacion(getHabitacion());
			if (isMenor())
			{
				resultado=String.valueOf(habitacion.getPrecioMenores());
			}
			else
			{
				resultado=String.valueOf(habitacion.getPrecioAdultos());
			}
			return resultado;
		}
		
		public String obtenerTipoHabitacion()
		{
			String resultado="";
			HabitacionBean habitacion=SQLHabitaciones.selectHabitacion(getHabitacion());
			resultado=habitacion.getPlazas()+" plaza(s) con "+habitacion.getCamas()+" camas";
			return resultado;
		}
		
		public String obtenerEstadoPagos()
		{
			String resultado="";
			Double debe=0.0;
			Double haber=SQLPagos.calcularPagosUsuario(id);
			HabitacionBean habitacion=SQLHabitaciones.selectHabitacion(getHabitacion());
			if (habitacion!=null)
			{
			if (isMenor())
			{
				debe=habitacion.getPrecioMenores();
			}
			else
			{
				debe=habitacion.getPrecioAdultos();
			}
			}
			else
			{
				debe= 0d; //Si no está alojado no tiene precio de habitación
			}

			resultado=String.valueOf(haber-debe);
			return resultado;
		}
		public String conBebes()
		{
			return con_bebes;
		}
		
		public void setConBebes(String bb)
		{
			con_bebes=bb;
		}
		public String getSmial() {
			return smial;
		}
		public void setSmial(String smial) {
			this.smial = smial;
		}
}
