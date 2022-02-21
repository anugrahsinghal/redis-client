import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConcurrentSocketHandler implements Runnable {

	private final Socket clientSocket;

	ConcurrentSocketHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	private static String asRESP(String s) {
		return "+" + s + "\r\n";
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String userInput;
			while ((userInput = in.readLine()) != null) {
				System.out.println("userInput = " + userInput);

				out.write(asRESP("PONG"));
				// flush is needed since only some commands on PrintWriter have flush automatically
				// and the constructor we used does not auto-flush
				out.flush();
			}

			System.out.printf("Thread %s connection was close", Thread.currentThread().getName());
		} catch (IOException e) {
			System.out.println(Thread.currentThread().getName() + " IOException: " + e.getMessage());
		} finally {
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(Thread.currentThread().getName() + " IOException: " + e.getMessage() + " while closing");
			}
		}
	}
}
