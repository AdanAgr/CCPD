import java.io.*;
import java.net.*;


public class lab5prog04 {
    public static void main(String[] args) {
        new Server().start();
        new Client().start();
    }
}

class Server extends Thread {
    public void run() {
        try (ServerSocket server = new ServerSocket(4444)) {
            while (true) {
                try (Socket socket = server.accept();
                     ObjectInputStream ois = 
                             new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream oos = 
                             new ObjectOutputStream(socket.getOutputStream())) {

                    String message = (String) ois.readObject();
                    System.out.println("Server Received: " + message);

                    oos.writeObject("Server Reply");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace(); // Handle exceptions for each connection separately
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log exception for debugging
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Log exception for debugging
        }
    }
}


