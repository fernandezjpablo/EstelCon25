package es.ste.aderthad.data;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.ste.aderthad.log.Logger;
import es.ste.aderthad.properties.Entorno;

public class MensajeBean {
	public String getFrom() {
		if (from==null) from="";
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		if (to==null) to="";
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCopyto() {
		if (copyto==null) copyto="";
		return copyto;
	}
	public void setCopyto(String copyto) {
		this.copyto = copyto;
	}
	public String getBlindcopyto() {
		if (blindcopyto==null) blindcopyto="";
		return blindcopyto;
	}
	public void setBlindcopyto(String blindcopyto) {
		this.blindcopyto = blindcopyto;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public void setReplyTo(String rt)
	{
		this.replyto=rt;
	}
	
	public String getReplyTo()
	{
		return replyto;
	}
	
	private String from;
	private String to;
	private String copyto;
	private String blindcopyto;
	private String subject;
	private String body;
	private String replyto;

	public String getReplyto() {
		return replyto;
	}
	public void setReplyto(String replyto) {
		this.replyto = replyto;
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

	private long fecha;
	private long fechaUpdate;
	

	public Message generarMensaje()
	{
		Properties prop=System.getProperties();
		prop.put("mail.smtp.host", Entorno.getVariable("SMTP_HOST"));
		prop.put("mail.smtp.auth","true");
		prop.put("mail.smtp.port", Entorno.getVariable("SMTP_PORT"));
		prop.put("mail.smtp.starttls.enable", Entorno.getVariable("SMTP_ENABLE_TLS"));
		Session session=Session.getInstance(prop,null);
		Message msg = new MimeMessage(session);
		try {
			InternetAddress[] responder=new InternetAddress[1];
			responder[0]=new InternetAddress(getReplyTo());
			msg.setFrom(new InternetAddress(getFrom()));
			msg.setReplyTo(responder);

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(getTo(), false));

			// cc
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(getCopyto(), false));

            msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(getBlindcopyto(), false));
            
			// subject
            msg.setSubject(getSubject());
			
			// content 
            msg.setText(getBody());
			
            msg.setSentDate(new Date());
			
		} catch (MessagingException e) {
			Logger.GenerarEntradaLogError(e, Logger.getFileNameErrorLog());
			e.printStackTrace();
		}
		
		return msg;
	}
	
}
