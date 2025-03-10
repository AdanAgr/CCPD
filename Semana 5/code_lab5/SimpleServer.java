import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
        int port = 5000; // Server port

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Client connected on port: " + port);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received message: " + inputLine + " from client");
                out.println("Echo: " + inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}