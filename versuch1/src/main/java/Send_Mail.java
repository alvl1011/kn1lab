import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Imports of Util files
import de.utils.SessionUtils;
import de.utils.MessageUtils;

public class Send_Mail {
	
	public static void main(String[] args) {
		
		sendMail(SessionUtils.launchSession(
				SessionUtils.setPropertiesForSMTPSession("localhost", "2526")),
				"Test subject",
				"vladi@localhost",
				"labrat@localhost",
				MessageUtils.buildMessageFromHTML(MessageUtils.readFromFile("C:\\Users\\vovaa\\Documents\\kn1lab\\versuch1\\src\\main\\resources\\html-templates\\temp-1.txt"))
				);   
		// sendMailAndSaveLocal();
	}
	
	public static void sendMail(Session session, String subject, String from, String to, Multipart content) {
		try {
			Message message = new MimeMessage(session);
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setContent(content);
			System.out.println("Message sent.");
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * LAUNCH ON VIRTUAL MACHINE
	 * 
	 * This function is from task to save emails in folder /home/user/Maildir
	 */
	public static void sendMailAndSaveLocal() {
		try {
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "localhost");
			Session session = Session.getInstance(props);

			Message msg = new MimeMessage(session);
			msg.setSubject("Test Mail");
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress("sent@localhost"));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress("labrat@localhost"));
			msg.setText("First mail text");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
