//javac Message.java Server.java Client.java
//  in terminal 1:  java Server
//  in terminal 2:  java Client
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class lab5prog05C {
    public static void main(String[] args) {
        try {
            String host = "localhost";
            for (int x = 0; x < 5; x++) {
                try (
                    Socket socket = new Socket(host, 4444);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ) {
                    Message messageToSend = new Message("Client Message " + x, x);
                    oos.writeObject(messageToSend);
                    oos.flush();
                    Message receivedMessage = (Message) ois.readObject();
                    System.out.println("Client Received: " + receivedMessage.msg + " with code " + receivedMessage.code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}