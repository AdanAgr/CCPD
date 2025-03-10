import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class lab5prog02 {
    public static class Message implements Serializable {  //Serializar es pasar de Objeto a BytesStream y Deserializar es pasar de BytesStream a Objeto
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
        // Crea el objeto Serializado y lo guarda 
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("message.ser"))) {
            oos.writeObject(message); //message.ser es para oos el archivo donde se guardara el objeto
            System.out.println("Serialization done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Deserialize the Message object
        // Lee el objeto Serializado y lo convierte a Objeto
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