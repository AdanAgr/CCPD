import java.util.concurrent.*;

public class FutureIsDone {

    // MAIN
    public static void main(String[] args) throws Exception {

        // Crea un ExecutorService con un pool de hilos fijo de 1 hilo
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        // Enviamos una tarea al executor que retorna un resultado
        Future<Integer> futuro = executor.submit(() ->
            {
                Thread.sleep(2000);
                return 10 * 2; // Simula un cálculo
            }
        );

        // Revisamos cada medio segundo si la tarea terminó
        while (!futuro.isDone()) {
            System.out.println("La tarea aún no ha terminado...");
            Thread.sleep(500);
        }

        // Ya terminó
        Integer resultado = futuro.get();
        System.out.println("Resultado final: " + resultado);

        executor.shutdown();
    }
}
