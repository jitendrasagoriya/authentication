package com.js.authentication.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Importing required classes

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.js.authentication.mail.model.EmailDetails;
import com.js.authentication.mail.service.EmailService;

//Annotation
@RestController 
public class EmailEndpoint {
	
	private final Logger logger = LoggerFactory.getLogger(EmailEndpoint.class);

	@Autowired
	private EmailService emailService;

	// Sending a simple Email
	@PostMapping("/sendMail")
	public String sendMail(@RequestBody EmailDetails details) {
		logger.info("Sending email..");
		String status = emailService.sendSimpleMail(details);
		return status;
	}

	// Sending email with attachment
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}
}
