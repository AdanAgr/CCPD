import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class lab5prog01 {
    public static void main(String[] args) {
        // Reading from "input.txt"
        try (InputStream inputStream = new FileInputStream("input.txt");
             Reader inputStreamReader = new InputStreamReader(inputStream)) {
            
            int data = inputStreamReader.read();
            StringBuilder inputContent = new StringBuilder();
            
            while (data != -1) {
                char theChar = (char) data;
                inputContent.append(theChar);
                data = inputStreamReader.read();
            }
            
            // Just to demonstrate, print the content read from file
            System.out.println(inputContent.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Writing to "output.txt"
        try (OutputStream outputStream = new FileOutputStream("output.txt");
             Writer outputStreamWriter = new OutputStreamWriter(outputStream)) {
            
            outputStreamWriter.write("Hello World");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}