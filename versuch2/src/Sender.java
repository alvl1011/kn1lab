import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Die "Klasse" Sender liest einen String von der Konsole und zerlegt ihn in einzelne Worte. Jedes Wort wird in ein
 * einzelnes {@link Packet} verpackt und an das Medium verschickt. Erst nach dem Erhalt eines entsprechenden
 * ACKs wird das nächste {@link Packet} verschickt. Erhält der Sender nach einem Timeout von einer Sekunde kein ACK,
 * überträgt er das {@link Packet} erneut.
 */
public class Sender {
    /**
     * Hauptmethode, erzeugt Instanz des {@link Sender} und führt {@link #send()} aus.
     * @param args Argumente, werden nicht verwendet.
     */
    public static void main(String[] args) {
    	System.out.println("Start...\n");
        Sender sender = new Sender();
        try {
            sender.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt neuen Socket. Liest Text von Konsole ein und zerlegt diesen. Packt einzelne Worte in {@link Packet}
     * und schickt diese an Medium. Nutzt {@link SocketTimeoutException}, um eine Sekunde auf ACK zu
     * warten und das {@link Packet} ggf. nochmals zu versenden.
     * @throws IOException Wird geworfen falls Sockets nicht erzeugt werden können.
     */
    private void send() throws IOException {
   	//Text einlesen und in Worte zerlegen
    	
    	Scanner sc = new Scanner(System.in);
    	
        // Socket erzeugen auf Port 9998 und Timeout auf eine Sekunde setzen
    	DatagramSocket client = new DatagramSocket(9998);
    	client.setSoTimeout(1000);
    	
    	// Iteration �ber den Konsolentext
        while (true) {
        	// Paket an Port 9997 senden
        	
        	String input = sc.nextLine() + " EOT";
        	
        	if (input.equals("quit EOT") || input.equals("quit")) {
        		System.out.println("Programm is closing...");
        		break;
        	}
        	
        	packetsToSend(client, "localhost", 9997, () -> input.split(" "));
        }
        
        // Wenn alle Packete versendet und von der Gegenseite bestätigt sind, Programm beenden
        sc.close();
        client.close();
        
        if(System.getProperty("os.name").equals("Linux")) {
            client.disconnect();
        }

        System.exit(0);
    }

    
    private void packetsToSend(DatagramSocket socket, String hostName, int port, Callable<String[]> function) {
    	
    	try {
    		
    		if (socket.isClosed()) {
    			 System.out.println("Socket is not active.");
    			 System.exit(0);
    		}
    		
    		ExecutorService executorService = Executors.newSingleThreadExecutor();
        	Future<String[]> future = executorService.submit(function);
        	
        	String[] resultArray = future.get();
        	
        	 int i = 0;
             int j = 0;
             while (j != resultArray.length) {
            	 
                 Packet packetPayload = new Packet(i, i, false, resultArray[j].getBytes());
             
                 ByteArrayOutputStream b = new ByteArrayOutputStream();
                 ObjectOutputStream o = new ObjectOutputStream(b);
                 o.writeObject(packetPayload);
                 byte[] bufSend = b.toByteArray();
             
                 DatagramPacket packetSend = new DatagramPacket(bufSend, bufSend.length, InetAddress.getByName(hostName), port);
                 socket.send(packetSend);
             
                 try {
                	 
                     byte[] bufRec = new byte[256]; 
                     DatagramPacket packetAcked = new DatagramPacket(bufRec, bufRec.length);
                     socket.receive(packetAcked);
                 
                     ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(packetAcked.getData()));
                     Packet packetIn = (Packet) is.readObject();
                 
                     if (packetIn.isAckFlag() && packetIn.getAckNum() != i + resultArray[j].length()) {
                         continue; 
                     }
                 
                     i += resultArray[j].length();
                     j++;
                 } catch (ClassNotFoundException e) {
                     e.printStackTrace();
                 } catch (SocketTimeoutException e) {
                     System.out.println("Receive timed out, retrying...");
                 }
             }
    		
    	} catch(IOException | InterruptedException | ExecutionException e) {
    		 System.err.println("Problem with packet execution.");
    		 e.printStackTrace();
    	}
	}
}
