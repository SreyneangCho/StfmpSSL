import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandlerThread extends Thread {
	
	private Socket connection;
	
	public ClientHandlerThread(Socket connection) {
		super();
		this.connection = connection;
	}
	
	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		super.run();
		
		while(true) {
			try {
			// Read request from the client
			InputStream inputStream = connection.getInputStream();
			Scanner scanner = new Scanner(inputStream);
			String rawRequest = scanner.nextLine();	
			StfmpRequest request = StfmpRequest.fromRawString(rawRequest);
			
			// Process the request
			List<String> params = request.getParams();
			String FileName = params.get(0);
			List<String> data=new ArrayList<String>(); 
			File filename = new File("lib/"+FileName);
			
			if(request.getAction().equals(StfmpAction.VIEW)) {
				// Response: View Content of Requested File to Users 
				try {
					if(filename.exists()) {
						@SuppressWarnings("resource")
						Scanner scan = new Scanner(filename);
						while(scan.hasNextLine()) {
							String line = scan.nextLine();
							data.add(line);
						}
						StfmpResponse response = new StfmpResponse(Constants.PROTOCOL_VERSION, StfmpStatus.OK, data);
						System.out.println("Raw Response: " + response.toRawResponse().replace("\r\n", ""));
						sendResponse(connection, response);
						
					}else {
						data.add("File Not Found.");
						StfmpResponse response = new StfmpResponse(Constants.PROTOCOL_VERSION, StfmpStatus.NOT_FOUND, data);
						System.out.println("Raw Response: " + response.toRawResponse().replace("\r\n", ""));
						sendResponse(connection, response);
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}else if(request.getAction().equals(StfmpAction.CLOSE)) {
				
				System.out.println("Done with the client.");
				connection.close();
				break;
				
			} else if(request.getAction().equals(StfmpAction.WRITE)){
				// Response: Write Content to Requested File and Print Message 
				 try {
					 String FileContent = params.get(1);
					 if(filename.exists()) {
				        FileWriter writer = new FileWriter("lib/"+FileName,true);
				        writer.write("\n" + FileContent);
					    writer.close();
					     data.add("The file has been written");
					     StfmpResponse response = new StfmpResponse(Constants.PROTOCOL_VERSION, StfmpStatus.OK, data);
					     System.out.println("Raw Response: " + response.toRawResponse().replace("\r\n", ""));
					     sendResponse(connection, response);
					     
					 }else {
						data.add("File Not Found.");
						StfmpResponse response = new StfmpResponse(Constants.PROTOCOL_VERSION, StfmpStatus.NOT_FOUND, data);
						System.out.println("Raw Response: " + response.toRawResponse().replace("\r\n", ""));
						sendResponse(connection, response);
						
					 }
				      
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				
			}else {
				data.add("Invalid Request.");
				StfmpResponse response = new StfmpResponse(Constants.PROTOCOL_VERSION, StfmpStatus.INVALID, data);
				sendResponse(connection, response);
			}
			}catch(IOException ex) {
				
			}
		}
	}
	
	private void sendResponse(Socket connection, StfmpResponse response) throws IOException {
		OutputStream outputStream = connection.getOutputStream();
		PrintWriter printWriter = new PrintWriter(outputStream);
		printWriter.write(response.toRawResponse());
		printWriter.flush();
	}

}