import java.io.Serializable;

public class Message implements Serializable {
    public String msg;
    public int code;

    public Message(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}