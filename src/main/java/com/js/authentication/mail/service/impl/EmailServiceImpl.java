package com.js.authentication.mail.service.impl;

//Importing required classes

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.js.authentication.mail.EmailHelper;
import com.js.authentication.mail.model.EmailDetails;
import com.js.authentication.mail.service.EmailService;
import com.js.authentication.model.Authentication;

//Annotation
@Service
//Class
//Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

	private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	public void sendVerificationEmail(Authentication user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		try {
			String toAddress = user.getUserName(); 
			String subject = "Please verify your registration";
			String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
					+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "AuthAsAService.com";

			 

			content = content.replace("[[name]]", user.getUserName());
			String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

			content = content.replace("[[URL]]", verifyURL);

			EmailHelper.sendMailViaGodaddy(toAddress, subject, content);
			 
			logger.info("Email send..");
		} catch (Exception e) {
			logger.error("Email Not Send..", e);

		}

	}

	public void sendForgotPassword(Authentication user)
			throws MessagingException, UnsupportedEncodingException {
		try {
			String toAddress = user.getUserName(); ;
			String subject = "Your password";
			String content = "Dear [[name]],<br><br><br><br>" + "Your one time password is :<br>" + "<h3>" + user.getPassward()
					+ "</h3>" + "Thank you,<br>" + "AuthAsAService.com";
 

			content = content.replace("[[name]]", user.getUserName());

			EmailHelper.sendMailViaGodaddy(toAddress, subject, content);
			
			logger.info("Email send..");
		} catch (Exception e) {
			logger.error("Email Not Send..", e);
		}

	}

	// Method 1
	// To send a simple email
	public String sendSimpleMail(EmailDetails details) {

		// Try block to check for exceptions
		try {

			// Creating a simple mail message
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getRecipient());
			mailMessage.setText(details.getMsgBody());
			mailMessage.setSubject(details.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			return "Error while Sending Mail";
		}
	}

	// Method 2
	// To send an email with attachment
	public String sendMailWithAttachment(EmailDetails details) {
		// Creating a mime message
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		try {

			// Setting multipart as true for attachments to
			// be send
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());

			// Adding the attachment
			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

			mimeMessageHelper.addAttachment(file.getFilename(), file);

			// Sending the mail
			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		// Catch block to handle MessagingException
		catch (MessagingException e) {

			// Display message when exception occurred
			return "Error while sending mail!!!";
		}
	}
}
