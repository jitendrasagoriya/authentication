package com.js.authentication.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailHelper {
	private final static Logger logger = LogManager.getLogger(EmailHelper.class);

	public static void sendMailViaGodaddy(String to, String subject, String text) {
		try {
			String from = "admin@auth-as-a-service.com";
			String password = "J1tendr@S@g0r1y@";
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "smtpout.secureserver.net");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.ssl.enable", "true");

			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, password);
				}
			});

			session.setDebug(false);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("NoReplay@auth-as-a-service.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(text, "text/html");
			Transport.send(message);
			System.out.println("OK");

			logger.debug("Email via go daddy sent");
		} catch (Exception e) {
			logger.error("Failed to send Email : " + e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		sendMailViaGodaddy("jitendra.sagoriya@outlook.com", "THis is test mail ", "Test body");
	}

}
