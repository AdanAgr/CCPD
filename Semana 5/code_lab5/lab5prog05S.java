import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class lab5prog05S {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(4444)) {
            while (true) {
                try (
                    Socket socket = server.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ) {
                    Message message = (Message) ois.readObject();
                    System.out.println("Server Received: " + message.msg + " with code " + message.code);
                    oos.writeObject(new Message("Server Reply", message.code + 1));
                    oos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}