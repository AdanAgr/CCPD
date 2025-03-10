import java.net.Socket;
import java.net.InetAddress;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.io.Serializable;
import java.io.IOException;


// Define the Message class
class Message implements Serializable {
    public String msg;
    public int code;

    public Message(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}

class Client extends Thread {
    public void run() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            for (int x = 0; x < 5; x++) {
                try (
                    Socket socket = new Socket(host.getHostName(), 4444);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ) {
                    // Send a Message object
                    Message messageToSend = new Message("Client Message " + x, x);
                    oos.writeObject(messageToSend);
                    oos.flush();

                    // Read the response from the server
                    Message receivedMessage = (Message) ois.readObject();
                    System.out.println("Client Received: " + receivedMessage.msg + " with code " + receivedMessage.code);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Server extends Thread {
    public void run() {
        try (ServerSocket server = new ServerSocket(4444)) {
            while (true) {
                try (
                    Socket socket = server.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ) {
                    // Read a Message object
                    Message message = (Message) ois.readObject();
                    System.out.println("Server Received: " + message.msg + " with code " + message.code);

                    // Reply with a Message object
                    oos.writeObject(new Message("Server Reply", message.code + 1));
                    oos.flush();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class lab5prog05 {
    public static void main(String[] args) {
        new Server().start();
        new Client().start();
    }
}