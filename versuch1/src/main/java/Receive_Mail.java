import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class Receive_Mail {
	public static void main(String[] args) throws Exception {
		fetchMail();
	}
	
	public static void fetchMail() {
		try {
			// your code here
			Properties properties = new Properties();
			properties.put("mail.pop3.host", "localhost");
			properties.put("mail.pop3.port", "110");

			Session session = Session.getDefaultInstance(properties);

			Store store = session.getStore("pop3");
			store.connect("localhost", "labrat", "kn1lab");

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			Message[] messages = folder.getMessages();
			System.out.println("Messages:");
			System.out.println(messages.length);
			if (messages.length == 0){
				System.out.println("No messages received");
			} else {
				for (int i = 0, n = messages.length; i < n; i++) {
					Message message = messages[i];
					System.out.println("Nr: " + (i + 1));
					System.out.println("Subject: " + message.getSubject());
					System.out.println("From: " + message.getFrom()[0]);
					System.out.println("Date: " + message.getSentDate());
					System.out.println("Body: " + message.getContent().toString());
				}
			}
			folder.close();
			store.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
