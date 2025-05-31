import java.util.concurrent.*;
import java.util.*;

// MAIN
public class FutureCallableExamplePool {
    public static void main(String[] args) throws Exception {

        // EXECUTOR SERVICE con THREAD POOL FIJO de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // ARRAY para guardar los FUTURE de cada tarea
        @SuppressWarnings("unchecked") // ⚠️ evita las advertencias de tipo (porque iba a dar un warning por una movida de los tipos entre array y future)
        Future<String>[] futuros = new Future[5];

        // Lanzamos 5 tareas diferentes
        for (int i = 0; i < 5; i++) {
            int tareaId = i + 1; // Para identificar la tarea en la salida

            // Cada tarea Callable personalizada
            Callable<String> tarea = () -> {
                Thread.sleep(1000); // simula trabajo pesado
                return "Tarea " + tareaId + " ejecutada por " + Thread.currentThread().getName();
            };

            // Enviamos la tarea y guardamos su Future
            futuros[i] = executor.submit(tarea);
        }

        // Recogemos los resultados de cada Future
        for (int i = 0; i < 5; i++) {
            String resultado = futuros[i].get(); // bloquea hasta que termine
            System.out.println("Resultado: " + resultado);
        }

        // Cierra el executor
        executor.shutdown();
    }
}
