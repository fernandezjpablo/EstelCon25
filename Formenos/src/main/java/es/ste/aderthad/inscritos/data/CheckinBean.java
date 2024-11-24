package es.ste.aderthad.inscritos.data;

import org.json.JSONObject;

public class CheckinBean {
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdInscrito() {
		return idInscrito;
	}
	public void setIdInscrito(String idInscrito) {
		this.idInscrito = idInscrito;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getFechaExpedicion() {
		return fechaExpedicion;
	}
	public void setFechaExpedicion(String fechaExpedicion) {
		this.fechaExpedicion = fechaExpedicion;
	}
	public long getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public long getFechaUpdate() {
		return fechaUpdate;
	}
	public void setFechaUpdate(long fechaUpdate) {
		this.fechaUpdate = fechaUpdate;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String toJson()
	{
		JSONObject objeto=new JSONObject();
		objeto.put("usuario", usuario);
		objeto.put("idInscrito", idInscrito);
		objeto.put("nif", nif);
		objeto.put("fechaNacimiento",fechaNacimiento);
		objeto.put("fechaExpedicion", fechaExpedicion);
		objeto.put("nombre", nombre);
		objeto.put("apellidos", apellidos);
		objeto.put("observaciones", observaciones);
		objeto.put("fechaCreacion", fechaCreacion);
		objeto.put("fechaUpdate", fechaUpdate);
		objeto.put("direccion", direccion);
		objeto.put("codigo_postal", codigo_postal);
		objeto.put("ciudad", ciudad);
		objeto.put("pais", pais);
		return objeto.toString();
	}
	private String usuario;
	private String idInscrito;
	private String nif;
	private String fechaNacimiento;
	private String fechaExpedicion;
	private long fechaCreacion;
	private long fechaUpdate;
	private String nombre;
	private String apellidos;
	private String observaciones;
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCodigo_postal() {
		return codigo_postal;
	}
	public void setCodigo_postal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	private String direccion;
	private String codigo_postal;
	private String ciudad;
	private String pais;
	
	
	
}
