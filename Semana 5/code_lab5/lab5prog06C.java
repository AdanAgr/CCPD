import java.io.*;
import java.net.*;

/**
 * Cliente que:
 *  1) Se conecta al servidor.
 *  2) Recibe un objeto ImageChunkMessage con el trozo a procesar.
 *  3) Aplica un filtro/operación (invertir colores, escala de grises, etc.).
 *  4) Devuelve el trozo procesado al servidor.
 */
public class lab5prog06C {
    public static void main(String[] args) {
        try {
            // Puedes pasar la IP/host del servidor como argumento,
            // o usar "localhost" si está en la misma máquina.
            String serverHost = (args.length > 0) ? args[0] : "localhost";
            int port = 4444;

            // Conectarse al servidor
            Socket socket = new Socket(serverHost, port);
            System.out.println("Conectado al servidor " + serverHost + ":" + port);

            // Crear streams
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Recibir el trozo
            ImageChunkMessage chunk = (ImageChunkMessage) ois.readObject();
            System.out.println("Recibido trozo en (" + chunk.startX + "," + chunk.startY + ")"
                    + " tamaño " + chunk.width + "x" + chunk.height);

            // Procesar el trozo (por ejemplo, invertir colores si operationCode=1)
            int[] processedPixels = applyFilter(chunk.pixels, chunk.operationCode);
            chunk.pixels = processedPixels;

            // Devolver trozo procesado al servidor
            oos.writeObject(chunk);
            oos.flush();

            // Cerrar
            oos.close();
            ois.close();
            socket.close();

            System.out.println("Trozo procesado y enviado de vuelta. Cliente finaliza.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejemplo de filtro sencillo:
     *  - Si operationCode=1 => Invertir colores
     *  - Puedes añadir más if/else para diferentes filtros
     */
    private static int[] applyFilter(int[] pixels, int operationCode) {
        if (operationCode == 1) {
            // Invertir colores
            for (int i = 0; i < pixels.length; i++) {
                int argb = pixels[i];
                int alpha = (argb >> 24) & 0xFF;
                int red   = (argb >> 16) & 0xFF;
                int green = (argb >>  8) & 0xFF;
                int blue  = (argb      ) & 0xFF;

                // Invertir
                red   = 255 - red;
                green = 255 - green;
                blue  = 255 - blue;

                // Reconstruir pixel
                pixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        return pixels;
    }
}
