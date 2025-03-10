import java.io.Serializable;

public class MessageV2 implements Serializable {
    public String msg;
    public int code; // Operation code: 1 for sum, 2 for multiply, etc.
    public int[] operands; // Operands for operations

    public MessageV2(String msg, int code, int... operands) {
        this.msg = msg;
        this.code = code;
        this.operands = operands;
    }
}
