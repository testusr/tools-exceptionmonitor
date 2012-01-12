package de.smeo.tools.exceptionmonitor.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailDispatcher {
	private Properties mailServerProperties;

	public EmailDispatcher(String configfile) {
		File file = new File(configfile);
		if (file.exists()) {
			try {
				mailServerProperties = FileUtils.loadPropertiesFromFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			createSamplePropertyFile(file);
		}
	}

	public void sendEmail(String receiverList, String subject,
			String messageBody) throws MessagingException {
		String receivers[] = receiverList.split(",");

		for (String receiver : receivers) {
			System.out.println("---------------------------------------------------------\n");
			System.out.println("Sending email: '" + subject + "'\n");
			System.out.println("receiver: '" + receiver + "'\n");
			System.out.println("messageBody: \n" + messageBody + "\n");
			System.out.println("---------------------------------------------------------\n");

			Session mailSession = Session.getDefaultInstance(
					mailServerProperties, null);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.setContent(messageBody, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					receiver));

			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
		}
	}

	static void createSamplePropertyFile(File file) {
		try {
			boolean fileCreated = file.createNewFile();
			if (fileCreated) {
				FileWriter fstream = new FileWriter(file);
				BufferedWriter ofile = new BufferedWriter(fstream);
				ofile.write("mail.transport.protocol=smpt\n");
				ofile.write("mail.host=mymail.server.org\n");
				ofile.write("mail.password=<your password>\n");
				ofile.write("mail.user=<your user>\n");
				ofile.close();
			} else {
				System.err.println("Could not create file: '"
						+ file.getAbsolutePath() + "'");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
