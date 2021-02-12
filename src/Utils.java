
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Utils {
	
	static void sendRequestToTheServer(Socket connection, StfmpRequest request) throws IOException {
		OutputStream outputStream = connection.getOutputStream();
		PrintWriter printWriter = new PrintWriter(outputStream);
		printWriter.write(request.toRawString());
		printWriter.flush();
	}
	
	static StfmpResponse readResponseFromTheServer(Socket connection) throws IOException {
		InputStream inputStream = connection.getInputStream();
		Scanner scanner = new Scanner(inputStream);
		String rawRespone = scanner.nextLine();
		return StfmpResponse.fromRawResponse(rawRespone);
	}

}
