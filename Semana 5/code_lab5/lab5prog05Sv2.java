import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.io.IOException;


public class lab5prog05Sv2 {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Server started.");
            
            while (true) {
                try (
                    Socket clientSocket = serverSocket.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ) {
                    // Server sends a code to perform an operation
                    oos.writeObject(new MessageV2("Perform operation", 1, 5, 3)); // Example: sum 5 and 3
                    oos.flush();

                    // Server waits for completion message from the client
                    MessageV2 clientMessage = (MessageV2) ois.readObject();
                    if (clientMessage.code == 0) { // Assuming code 0 means completion
                        System.out.println("Client completed operation: " + clientMessage.msg);
                    }

                    // Additional logic here to decide on the next steps, send more codes, etc.
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}