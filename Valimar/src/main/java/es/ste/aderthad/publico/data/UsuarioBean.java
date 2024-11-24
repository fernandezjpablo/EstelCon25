package es.ste.aderthad.publico.data;

public class UsuarioBean {

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String id;
	private String usuario;
	private String password;
	public String getIdInscrito() {
		return idInscrito;
	}
	public void setIdInscrito(String idInscrito) {
		this.idInscrito = idInscrito;
	}
	private String idInscrito;
	

}
