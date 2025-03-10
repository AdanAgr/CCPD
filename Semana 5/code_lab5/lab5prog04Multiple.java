import java.io.*;
import java.net.*;

public class lab5prog04Multiple {
    public static void main(String[] args) {
        new Server().start();
        // Multiple clients can be started from different terminals or by adding more lines here
        new Client().start();
    }
}

class Server extends Thread {
    public void run() {
        try (ServerSocket server = new ServerSocket(4444)) {
            System.out.println("Server started on port 4444");
            while (true) {
                try {
                    Socket socket = server.accept(); // Accept a new client connection
                    System.out.println("Client connected");
                    new ClientHandler(socket).start(); // Handle each client in a separate thread
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// Define a ClientHandler class to manage each client connection
class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            String message;
            while ((message = (String) ois.readObject()) != null) { // Continuously listen for messages
                System.out.println("Server Received: " + message);
                oos.writeObject("Server Reply");
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println("I/O error with client: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

class Client extends Thread {
    public void run() {
        try (Socket socket = new Socket(InetAddress.getLocalHost().getHostName(), 4444);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            for (int x = 0; x < 5; x++) {
                oos.writeObject("Client Message " + x);
                oos.flush(); // Ensure the message is sent immediately

                String message = (String) ois.readObject();
                System.out.println("Client Received: " + message);
            }
        } catch (IOException e) {
            System.err.println("I/O error communicating with server: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}