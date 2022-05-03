package de.utils;

import java.util.HashMap;
import java.util.Properties;
import javax.mail.Session;

public final class SessionUtils {
	
	/**
	 * launchSession()
	 * - Launches session with given properties
	 * @param props
	 * @return
	 */
	public static Session launchSession(Properties props) {
		try {
			Session session = null;
			if (props != null) {
				System.out.println("Start of session...");
				session = Session.getInstance(props);
			}
			return session;
		} catch(Exception e) {
			System.err.println("Could not set a session.\n");
			return null;
		}
	}
	
	/**
	 * setPropertiesForSMTPSession()
	 * - Set properties for SMTP session (overridden)
	 * @param host
	 * @param port
	 * @param options
	 * @return
	 */
	public static Properties setPropertiesForSMTPSession(String host, String port, HashMap<String, String> options) {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			
			for (String key: options.keySet()) {
				props.put(key, options.get(key));
			}
			return props;
		} catch (Exception e) {
			System.err.println("Could not set properties for SMTP.\n");
			return null;
		}
		
	}
	
	/**
	 * Set properties for SMTP session
	 * @param host
	 * @param port
	 * @param options
	 * @return
	 */
	public static Properties setPropertiesForSMTPSession(String host, String port) {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			
			return props;
		} catch (Exception e) {
			System.err.println("Could not set properties for SMTP.\n");
			return null;
		}
		
	}

}
