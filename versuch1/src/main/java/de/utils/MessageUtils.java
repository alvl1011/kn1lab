package de.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public final class MessageUtils {

	/**
	 * Build message from HTML template
	 * @param html
	 * @return
	 */
	public static Multipart buildMessageFromHTML(String html) {
		Multipart content = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		try {
			htmlPart.setText(html, "utf-8", "html");
			content.addBodyPart(htmlPart);
			return content;
		} catch (Exception e) {
			System.err.println("Could not build HTML template");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Read text from File
	 * @param path FULL Path of file
	 * @return
	 */
	public static String readFromFile(String path) {
		
		File file = new File(path);
		Scanner sc;
		
		try {
			sc = new Scanner(file);
			
			String st = "";
		    while (sc.hasNextLine())
		      st += sc.nextLine();
		    sc.close();
		    return st;
		} catch (FileNotFoundException e) {
			System.err.println("Could not read file");
			e.printStackTrace();
			return null;
		}
	}
}
