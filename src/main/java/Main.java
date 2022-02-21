import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    //  Uncomment this block to pass the first stage
    ServerSocket serverSocket;
    int port = 6379;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      while (true) { // accept all incoming connections on main thread and keep it running
        // create new instance for each connection
        Socket clientSocketConnection = serverSocket.accept();

        // An extension of runnable that takes in an *exclusive* socket as input
        ConcurrentSocketHandler concurrentSocketHandler = new ConcurrentSocketHandler(clientSocketConnection);

        // make each connection be handled in another thread
        Thread thread = new Thread(concurrentSocketHandler);

        thread.start();
      }

    } catch (IOException e) {
      System.out.println(Thread.currentThread().getName() + " IOException: " + e.getMessage());
    }
  }
}