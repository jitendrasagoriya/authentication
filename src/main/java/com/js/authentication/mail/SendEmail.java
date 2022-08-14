package com.js.authentication.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
 
public class SendEmail
{
 
   public static void main(String [] args)
   {   
      // email ID of Recipient.
      String recipient = "jitendra.sagoriya.jio@gmail.com";
 
      // email ID of  Sender.
      String sender = "jitendrasagoriya83@gmail.com";
 
      // using host as localhost
      String host = "localhost";
 
      // Getting system properties
      Properties properties = System.getProperties();
 
      // Setting up mail server
      properties.setProperty("mail.smtp.host", host);
 
      // creating session object to get properties
      Session session = Session.getDefaultInstance(properties);
 
      try
      {
         // MimeMessage object.
         MimeMessage message = new MimeMessage(session);
 
         // Set From Field: adding senders email to from field.
         message.setFrom(new InternetAddress(sender));
 
         // Set To Field: adding recipient's email to from field.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
 
         // Set Subject: subject of the email
         message.setSubject("This is Subject");
 
         // set body of the email.
         message.setContent("<h1>This is a HTML text</h1>","text/html");
 
         // Send email.
         Transport.send(message);
         System.out.println("Mail successfully sent");
      }
      catch (MessagingException mex)
      {
         mex.printStackTrace();
      }
   }
}