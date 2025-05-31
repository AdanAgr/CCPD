import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorPoolDemo {

    // MAIN
    public static void main(String[] args) {
        
        // EXECUTORSERVICE con THREAD POOL FIJO de 2 hilos
        ExecutorService executor = Executors.newFixedThreadPool(2); // executor con DOS hilos

        // ENVIAR varias (5) TAREAS al executor
        // si se lanzan varias tareas, la asignación de tareas a los DOS hilos se hace de forma concurrente 
        for (int i = 1; i <= 5; i++) {
            int tareaId = i; // Necesario para usar la variable dentro de la lambda
            executor.submit(() -> {
                System.out.println("Tarea " + tareaId + " ejecutada por " + Thread.currentThread().getName());
            });
        }

        // CIERRA el executor
        executor.shutdown();
    }
}
