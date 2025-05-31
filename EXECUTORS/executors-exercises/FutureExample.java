import java.util.concurrent.*;

public class FutureExample {
    
    // MAIN
    public static void main(String[] args) throws Exception {
        // Crea un ExecutorService con un pool de hilos fijo de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Envía tarea asíncrona al executor
        // OBTIENE un FUTURE que representa el resultado de la tarea
        Future<String> future = executor.submit(() -> {
            // Simula un trabajo pesado
            Thread.sleep(1000);
            return "¡Hola desde la tarea! ejecutada por " + Thread.currentThread().getName();
        });

        // OTRAS ACCIONES EN PARALELO

        // Barrera que BLOQUEA hasta que la tarea se complete
        String resultado = future.get(); 

        // Imprime el resultado de la tarea
        System.out.println("Resultado: " + resultado);

        // CIERRA el executor
        executor.shutdown();
    }
}
