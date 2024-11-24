package es.ste.aderthad.publico.data;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.ste.aderthad.publico.log.LoggerPublic;
import es.ste.aderthad.publico.properties.EntornoPublic;

public class MensajeBean {
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCopyto() {
		return copyto;
	}
	public void setCopyto(String copyto) {
		this.copyto = copyto;
	}
	public String getBlindcopyto() {
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
	

	public Message generarMensaje()
	{
		Properties prop=System.getProperties();
		prop.put("mail.smtp.host", EntornoPublic.getVariable("SMTP_HOST"));
		prop.put("mail.smtp.auth","true");
		prop.put("mail.smtp.port", EntornoPublic.getVariable("SMTP_PORT"));
		prop.put("mail.smtp.starttls.enable", EntornoPublic.getVariable("SMTP_ENABLE_TLS"));
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
			LoggerPublic.GenerarEntradaLogError(e, LoggerPublic.getFileNameErrorLog());
			e.printStackTrace();
		}
		
		return msg;
	}
	
}
