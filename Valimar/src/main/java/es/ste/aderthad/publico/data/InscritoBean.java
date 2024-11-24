package es.ste.aderthad.publico.data;


import es.ste.aderthad.publico.sql.SQLHabitacionesPublic;


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
	 private String smial;
	 private String con_bebes;
	 

	 
	 public String getSmial() {
		return smial;
	}
	public void setSmial(String smial) {
		this.smial = smial;
	}
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
		 smial="";
		 con_bebes="0";
	 }
	 public InscritoBean(int idn,String nom, String apellidos, String telf,String em,String telg,String id_l,String pseudon,int estad, boolean men,boolean alim,String alerStr,boolean aler,String alimStr,String obser,String hab,String smi,String cb)
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
		 smial=smi;
		 con_bebes=cb;
	 }
		public String calcularImporte()
		{
			String resultado="";
			HabitacionBean habitacion=SQLHabitacionesPublic.selectHabitacion(getHabitacion());
			HabitacionParcialBean parcial;
			if (habitacion==null)
			{
				//Si es null es una plaza parcial, buscamos la habitación padre
				parcial=SQLHabitacionesPublic.selectHabitacionParcial(getHabitacion());
				if (parcial==null)
				{
					habitacion=null;//Lista de espera
				}
				else
				{
					habitacion=SQLHabitacionesPublic.selectHabitacion(parcial.getIdHabitacionRaiz());
				}
				
			}
			if (habitacion!=null)
			{
			if (isMenor())
			{
				resultado=String.valueOf(habitacion.getPrecioMenores());
			}
			else
			{
				resultado=String.valueOf(habitacion.getPrecioAdultos());
			}
			}
			else
			{
				resultado="(Lista de espera)";
			}
			return resultado;
		}
		
		public String obtenerTipoHabitacion()
		{
			String resultado="";
			HabitacionBean habitacion=SQLHabitacionesPublic.selectHabitacion(getHabitacion());
			HabitacionParcialBean parcial;
			if (habitacion==null)
			{
				//Si es null es una plaza parcial, buscamos la habitación padre
				parcial=SQLHabitacionesPublic.selectHabitacionParcial(getHabitacion());
				if (parcial!=null)
				{
				habitacion=SQLHabitacionesPublic.selectHabitacion(parcial.getIdHabitacionRaiz());
				resultado="Plaza individual en habitación compartida";
				}
				else
				{
					resultado="Sin habitación";
				}
			}
			else
			{
			resultado=habitacion.getPlazas()+" plaza(s) con "+habitacion.getCamas()+" camas";
			}
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
		
}
