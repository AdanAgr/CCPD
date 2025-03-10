import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class lab5prog03S {
    public static void main(String[] args) {
        String hostName = "localhost"; // Server hostname
        int port = 5000; // Server port
        
        try (Socket socket = new Socket(hostName, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = 
                     new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.println("Enter a message for the server:");
            String userInput = stdIn.readLine(); // Get user input
            out.println(userInput); // Send user input to server
            
            String response = in.readLine(); // Read response from server
            System.out.println("Server response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}