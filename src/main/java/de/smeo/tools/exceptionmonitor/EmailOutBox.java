package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import de.smeo.tools.exceptionmonitor.SingleFileExceptionReport.ReportedExceptionOccurances;

public class EmailOutBox {
	List<ExceptionReportEmail> preparedEmails = new ArrayList<EmailOutBox.ExceptionReportEmail>();

	public void emailExceptionToRecipient(
			EmailAdress emailAdress, MonitoredFile monitoredFile, ReportedExceptionOccurances exception) {
		ExceptionReportEmail exceptionReportEmail = getOrCreatePreparedExceptionReportEmail(emailAdress, monitoredFile.getFilename());
		exceptionReportEmail.addReportedExcpetion(exception);
	}
	
	private ExceptionReportEmail getOrCreatePreparedExceptionReportEmail(
			EmailAdress emailAdress, String filename) {
		ExceptionReportEmail preparedEmail = getExceptionReportEmail(emailAdress, filename);
		if (preparedEmail == null){
			preparedEmail = new ExceptionReportEmail(emailAdress, filename);
		}
		return preparedEmail;
	}
	
	private ExceptionReportEmail getExceptionReportEmail(EmailAdress emailAdress, String filename){
		for (ExceptionReportEmail currExceptionReportEmail : preparedEmails){
			if (currExceptionReportEmail.getEmailAdress().equals(emailAdress)
				&& currExceptionReportEmail.getFilename().equals(filename)){
					return currExceptionReportEmail;
				}
		}
		return null;
	}

		

	public void sendEmails() {
	      Properties props = new Properties();
	      props.setProperty("mail.transport.protocol", "smtp");
	      props.setProperty("mail.host", "mymail.server.org");
	      props.setProperty("mail.password", "");
	      props.setProperty("mail.user", "truehl");

	      for (ExceptionReportEmail currEmail : preparedEmails){
		      try {
			      Session mailSession = Session.getDefaultInstance(props, null);
			      Transport transport = mailSession.getTransport();
		
			      MimeMessage message = new MimeMessage(mailSession);
			      message.setSubject(currEmail.getSubject());
			      message.setContent(currEmail.getText(), "text/plain");
			      message.addRecipient(Message.RecipientType.TO,
			           new InternetAddress(currEmail.getEmailAdress().getAdressString()));
		
			      transport.connect();
			      transport.sendMessage(message,
			          message.getRecipients(Message.RecipientType.TO));
			      transport.close();
		      } catch (MessagingException e){
		    	  e.printStackTrace();
		      }
		 }
		
	}

	public static class ExceptionReportEmail {
		private final EmailAdress emailAdress;
		private final String filename;
		private ExceptionReport exceptionReport = new ExceptionReport();
		
		public ExceptionReportEmail(EmailAdress emailAdress, String filename) {
			this.emailAdress = emailAdress;
			this.filename = filename;
		}

		public String getFilename() {
			return filename;
		}

		public EmailAdress getEmailAdress() {
			return emailAdress;
		}

		public String getSubject(){
			return "ExceptionReport: " + filename;
		}
		
		public String getText(){
			return exceptionReport.toString();
		}

		public void addReportedExcpetion(ReportedExceptionOccurances exception) {
			exceptionReport.addReportedExceptionOccurances(exception);
		}
		
	}
}
