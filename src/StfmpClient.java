import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class StfmpClient {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		// Specify trust store info
		System.setProperty("javax.net.ssl.trustStore", "/Users/User/eclipse-workspace/SSL/lib/sreyneang-trust-store.cacerts");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		
		try{
			
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket connection = (SSLSocket) socketFactory.createSocket("localhost", 9999);
			
			// Tell users what they can do
			System.out.println("Please tell us what you want to do:");
			System.out.println(" - Type 'write' to write content to the file");
			System.out.println(" - Type 'view' to view content of the file");
			System.out.println(" - Type 'close' to close the connection.");
			
			// Read input from users, what they want to do
			InputStream keyboardInputStream = System.in;
			Scanner keyboadScanner = new Scanner(keyboardInputStream);
			while(true) {
				System.out.print("Type your action here: ");
				String userInput = keyboadScanner.nextLine();
				
				if(userInput.equals(StfmpAction.WRITE)) {

					System.out.print("Input File Name: ");
					Scanner fileinfo = new Scanner(System.in);
					String filename = fileinfo.nextLine();
					
					System.out.print("Input File Content: ");
					String filedata = fileinfo.nextLine();
					
					List<String> data = new ArrayList<String>(); 
					data.add(filename);
					data.add(filedata);
					
					StfmpRequest request = new StfmpRequest(Constants.PROTOCOL_VERSION, StfmpAction.WRITE, data);
					Utils.sendRequestToTheServer(connection, request);
					
				} else if(userInput.equals(StfmpAction.VIEW)) {
					
					Scanner filenameinput = new Scanner(System.in);
					System.out.print("Input File Name: ");
					String filename = filenameinput.nextLine();
					List<String> data = new ArrayList<String>();  
					data.add(filename);
					System.out.println(filename);
					StfmpRequest request = new StfmpRequest(Constants.PROTOCOL_VERSION, StfmpAction.VIEW, data);
					Utils.sendRequestToTheServer(connection, request);
					
				} else if(userInput.equals(StfmpAction.CLOSE)) {
					
					StfmpRequest request = new StfmpRequest(Constants.PROTOCOL_VERSION, StfmpAction.CLOSE, null);
					Utils.sendRequestToTheServer(connection, request);
					break;
					
				} else {
					System.out.println("Invalid input. Please try again.");
					continue;
				}
				
				// Read response from the server
				StfmpResponse response = Utils.readResponseFromTheServer(connection);
				// Show the result to the user
				System.out.println("Result: ");
				System.out.println(response.getResult());
			}
		} catch (IOException e) {
			System.out.println("Connection fail. " + e.getMessage());
		}
	}
	}