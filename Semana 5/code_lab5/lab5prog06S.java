import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;

/**
 * Servidor que:
 *  1) Carga una imagen completa (imagen.png).
 *  2) La divide en trozos (en este ejemplo, 4 trozos).
 *  3) Espera conexiones de clientes (workers) y envía cada trozo.
 *  4) Recibe el trozo procesado y lo reinserta en la imagen.
 *  5) Usa un CountDownLatch para sincronizar la finalización de todos los trozos.
 */
public class lab5prog06S {

    private static final int PORT = 4444;
    private static BufferedImage originalImage;
    private static CountDownLatch latch;

    // Número de trozos a procesar (en este ejemplo, 4).
    private static final int NUM_CHUNKS = 4;

    public static void main(String[] args) {
        try {
            // 1) Cargar la imagen (asegúrate de que "imagen.png" exista en la misma carpeta)
            originalImage = ImageIO.read(new File("./mono.png"));
            System.out.println("Imagen cargada. Tamaño: " 
                               + originalImage.getWidth() + "x" + originalImage.getHeight());

            // 2) Crear CountDownLatch con el número de trozos
            latch = new CountDownLatch(NUM_CHUNKS);

            // 3) Iniciar ServerSocket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor escuchando en el puerto " + PORT);

            // 4) Dividir la imagen en trozos
            //    Ejemplo simple: la dividimos en 4 trozos (2 filas x 2 columnas)
            int chunkWidth = originalImage.getWidth() / 2;
            int chunkHeight = originalImage.getHeight() / 2;

            for (int i = 0; i < NUM_CHUNKS; i++) {
                // Calcular coordenadas de cada trozo
                int row = i / 2;
                int col = i % 2;
                int startX = col * chunkWidth;
                int startY = row * chunkHeight;
                int w = (col == 1) ? originalImage.getWidth() - startX : chunkWidth;
                int h = (row == 1) ? originalImage.getHeight() - startY : chunkHeight;

                // Extraer píxeles del trozo
                int[] pixels = new int[w * h];
                originalImage.getRGB(startX, startY, w, h, pixels, 0, w);

                // Iniciar un hilo que:
                //    - Acepta UNA conexión entrante
                //    - Envía el trozo
                //    - Recibe el trozo procesado
                //    - Lo reinserta en la imagen
                new Thread(new ChunkServerHandler(
                    serverSocket, latch, startX, startY, w, h, pixels
                )).start();
            }

            // 5) Esperar a que todos los trozos se procesen
            latch.await();
            System.out.println("Todos los trozos han sido procesados.");

            // 6) Guardar la imagen resultante
            ImageIO.write(originalImage, "png", new File("imagen_filtrada.png"));
            System.out.println("Imagen filtrada guardada como imagen_filtrada.png");

            // Cerrar el ServerSocket si ya no deseas aceptar más conexiones
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clase interna que maneja la comunicación para un trozo de imagen:
     *  - Espera la conexión de un cliente
     *  - Envía el trozo (ImageChunkMessage)
     *  - Recibe el trozo procesado y lo reinserta en la imagen principal
     *  - Decrementa el CountDownLatch
     */
    static class ChunkServerHandler implements Runnable {
        private ServerSocket serverSocket;
        private CountDownLatch latch;
        private int startX, startY, width, height;
        private int[] pixels;

        public ChunkServerHandler(ServerSocket serverSocket, CountDownLatch latch,
                                  int startX, int startY, int width, int height, int[] pixels) {
            this.serverSocket = serverSocket;
            this.latch = latch;
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
            this.pixels = pixels;
        }

        @Override
        public void run() {
            Socket clientSocket = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try {
                // Espera a que un cliente se conecte
                clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getRemoteSocketAddress());

                // Crear streams
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());

                // Crear el mensaje con el trozo
                // operationCode=1 => Ejemplo de filtro (invertir colores, etc.)
                ImageChunkMessage msg = new ImageChunkMessage(startX, startY, width, height, pixels, 1);

                // Enviar el trozo al cliente
                oos.writeObject(msg);
                oos.flush();

                // Recibir el trozo procesado
                ImageChunkMessage response = (ImageChunkMessage) ois.readObject();
                System.out.println("Tamaño del trozo recibido: " + response.pixels.length);

                // Reinsertar píxeles en la imagen original
                synchronized (originalImage) {
                    originalImage.setRGB(
                        response.startX, response.startY,
                        response.width, response.height,
                        response.pixels, 0, response.width
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cerrar streams y socket
                try { if (oos != null) oos.close(); } catch (IOException ex) {}
                try { if (ois != null) ois.close(); } catch (IOException ex) {}
                try { if (clientSocket != null) clientSocket.close(); } catch (IOException ex) {}

                // Indicar que este trozo ha sido procesado
                latch.countDown();
            }
        }
    }
}
