import java.net.Socket;
import java.net.InetAddress;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

// Version 2 of the Client
public class lab5prog05Cv2 {
    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            try (
                Socket socket = new Socket(host.getHostName(), 4444);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ) {
                // Client receives the message with code for operation
                MessageV2 serverMessage = (MessageV2) ois.readObject();

                // Determine action based on code
                int result = 0;
                switch (serverMessage.code) {
                    case 1: // Sum operation
                        for (int operand : serverMessage.operands) result += operand;
                        System.out.println("Sum: " + result);
                        break;
                    case 2: // Multiply operation
                        result = 1;
                        for (int operand : serverMessage.operands) result *= operand;
                        System.out.println("Multiply: " + result);
                        break;
                    // Add more cases for different operations
                }

                // Send completion message back to server
                oos.writeObject(new Message("Operation completed", 0)); // 0 as a code for completion
                oos.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}