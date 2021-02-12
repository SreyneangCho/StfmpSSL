
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class StfmpServer {
	
	private static final String KEY_STORE_PASSWORD = "123456";

	public static void main(String[] args) {
		
		try {
			// Create an SSL context
			SSLContext context = SSLContext.getInstance("SSL");
			
			// Create a key management factory
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			
			// Create a keystore object
			KeyStore keyStore = KeyStore.getInstance("JKS");
			
			// Fill the keystore object
			FileInputStream inputStream = new FileInputStream("lib/Key.jks");
			keyStore.load(inputStream, KEY_STORE_PASSWORD.toCharArray());
			
			// Initialize the key management factory
			keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
			
			// Initialize the context
			context.init(keyManagerFactory.getKeyManagers(), null, null);
			
			// Create secure socket for server
			SSLServerSocketFactory serverSocketFactory = context.getServerSocketFactory();
			SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(9999);
			
			while(true) {
				// Wait for a client
				System.out.println("Waiting for client...");
				SSLSocket connection = (SSLSocket) serverSocket.accept();
				
				// Create a thread to handle the connection
				System.out.println("A connection was established.");
				System.out.println("Forward the connection to another thread to handle");
				Thread clientHandlerThread = new ClientHandlerThread(connection);
				clientHandlerThread.start();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}