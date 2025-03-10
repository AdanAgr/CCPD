import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class lab5prog03C {
    public static void main(String[] args) {
        String hostName = "localhost"; // Server hostname
        int port = 5000; // Server port
        
        try (Socket socket = new Socket(hostName, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.println("Connected to server. Enter a message for the server:");
            String userInput = stdIn.readLine(); // Get user input from console
            out.println(userInput); // Send user input to server
            
            String response = in.readLine(); // Read response from server
            System.out.println("Server response: " + response);
            
        } catch (Exception e) {
            System.out.println("Could not connect to server at " + hostName + " on port " + port);
            e.printStackTrace();
        }
    }
}