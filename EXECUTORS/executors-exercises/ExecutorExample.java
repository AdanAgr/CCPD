import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorExample {

    // MAIN
    public static void main(String[] args) {

        // EXECUTOR SERVICE con THREAD POOL FIJO de 2 hilos
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // ENVIAR tarea al executor
        executor.submit(() -> {
            System.out.println("Tarea ejecutada por " + Thread.currentThread().getName());
        });

        // CIERRA el executor
        executor.shutdown();
    }
}