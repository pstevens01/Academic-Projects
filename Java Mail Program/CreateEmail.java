package mail.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 
 * @author  Paul Stevens
 * 			
 * 			This class represents the actual email object that is created and sent
 * 			by the Javax Mail API libraries. The email is created and sent from this
 * 			class using the 'sendEmail' method.
 *
 */

public class CreateEmail {
	public static void sendEmail(Properties smtpProperties, String toAddress, String subject,
			String message, File attachFile) throws AddressException, MessagingException, IOException {
		
		//Queries 'smtpProperties' property file for user name and password
		final String user = smtpProperties.getProperty("mail.user");
		final String password = smtpProperties.getProperty("mail.password");
		
		//Creates an authorization object using the queried user name and password
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		};
		//Session object created to collect properties that provide access to the Transport protocols
		Session session = Session.getInstance(smtpProperties, auth);
		
		//Email MimeMessage object used to pack together all components of an email
		Message mailMessage = new MimeMessage(session);
		mailMessage.setFrom(new InternetAddress(user));
		mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
		mailMessage.setSubject(subject);
		mailMessage.setSentDate(new Date());
		
		//Sets the email message content to 'message' parameter
		MimeBodyPart msgBody = new MimeBodyPart();
		msgBody.setContent(message, "text/html");
		
		//Creates a multi-part body in case of attachments in email
		Multipart multi = new MimeMultipart();
		multi.addBodyPart(msgBody);
		
		if (attachFile != null && attachFile.length() > 0) {
			MimeBodyPart attachPart = new MimeBodyPart();
			
			try {
				attachPart.attachFile(attachFile);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			//Adds attachment if applicable
			multi.addBodyPart(attachPart);
		}
		//Appends new content with attachment as 'message + attachment'
		mailMessage.setContent(multi);
		
		//Sends the email
		Transport.send(mailMessage);
	}
}
