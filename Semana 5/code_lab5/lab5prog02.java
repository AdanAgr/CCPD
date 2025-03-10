import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class lab5prog02 {
    public static class Message implements Serializable {
        public String msg = null;
        public int code = 0;
        public Message(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }
    }
    public static void serializeMessage() {
        Message message = new Message("Hello, World!", 1);   
        // Serialize the Message object
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("message.ser"))) {
            oos.writeObject(message);
            System.out.println("Serialization done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Deserialize the Message object
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("message.ser"))) {
            Message readMessage = (Message) ois.readObject();
            System.out.println("Deserialization done. Message: " + readMessage.msg 
               + ", Code: " + readMessage.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        serializeMessage();
    }
}