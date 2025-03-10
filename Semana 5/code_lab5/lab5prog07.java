import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;

public class lab5prog07 {

    // Parámetros de la imagen
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Región del plano complejo a visualizar
    private static final double MIN_X = -2.0;
    private static final double MAX_X = 1.0;
    private static final double MIN_Y = -1.2;
    private static final double MAX_Y = 1.2;

    // Máximo número de iteraciones para el algoritmo de escape
    private static final int MAX_ITER = 1000;

    // Número de hilos (ajusta según tu CPU)
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        // Creamos la imagen en memoria
        final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Latch para sincronizar a todos los hilos
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        // Dividimos la imagen en NUM_THREADS franjas horizontales
        int chunkHeight = HEIGHT / NUM_THREADS;

        for (int t = 0; t < NUM_THREADS; t++) {
            // Coordenadas verticales de la franja que procesará el hilo t
            final int startY = t * chunkHeight;
            final int endY = (t == NUM_THREADS - 1) ? HEIGHT : startY + chunkHeight;

            // Creamos el hilo
            new Thread(() -> {
                // Procesamos la franja [startY, endY)
                for (int py = startY; py < endY; py++) {
                    // Mapeamos py a la parte imaginaria
                    double ci = MIN_Y + (py * (MAX_Y - MIN_Y) / (HEIGHT - 1));
                    for (int px = 0; px < WIDTH; px++) {
                        // Mapeamos px a la parte real
                        double cr = MIN_X + (px * (MAX_X - MIN_X) / (WIDTH - 1));

                        // Calculamos cuántas iteraciones tardamos en que |z| > 2
                        int iterations = mandelbrotIterations(cr, ci, MAX_ITER);

                        // Asignamos un color según el número de iteraciones
                        int color = colorScheme(iterations, MAX_ITER);
                        image.setRGB(px, py, color);
                    }
                }
                // Indicamos que este hilo terminó
                latch.countDown();
            }).start();
        }

        // Esperamos a que todos los hilos terminen
        try {
            latch.await();
            // Guardamos la imagen resultante
            ImageIO.write(image, "png", new File("mandelbrot_concurrent.png"));
            System.out.println("¡Listo! Se generó mandelbrot_concurrent.png");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcula cuántas iteraciones tardamos en que |z| > 2
     * (o si llega a MAX_ITER, consideramos que no diverge en ese rango).
     */
    private static int mandelbrotIterations(double cr, double ci, int maxIter) {
        double zr = 0.0, zi = 0.0;
        int iter = 0;
        while ((zr*zr + zi*zi) <= 4.0 && iter < maxIter) {
            double temp = zr*zr - zi*zi + cr;
            zi = 2.0 * zr * zi + ci;
            zr = temp;
            iter++;
        }
        return iter;
    }

    /**
     * Asigna un color (en RGB) según el número de iteraciones.
     * - Si iter == maxIter, se colorea negro (dentro o cerca del conjunto).
     * - Si diverge antes, se genera un degradado con HSB.
     */
    private static int colorScheme(int iter, int maxIter) {
        if (iter >= maxIter) {
            // Dentro del conjunto => negro
            return 0x000000;
        } else {
            // Fuera => un degradado de color
            float hue = (float) iter / (float) maxIter;  // valor entre 0 y 1
            return java.awt.Color.HSBtoRGB(hue, 0.6f, 1.0f);
        }
    }
}
