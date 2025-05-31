import java.util.concurrent.*;

// MAIN
public class CallableExample {
    public static void main(String[] args) throws Exception {
        
        // EXECUTORSERVICE con 1 solo hilo
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // TAREA TIPO CALLABLE (devuelve un resultado)
        Callable<Integer> task = () -> {
            Thread.sleep(1000); // simula trabajo pesado (1 sec)
            return 123; // devuelve result
        };

        // ENVIAR la tarea al executor --> retorna un FUTURE!!!
        Future<Integer> future = executor.submit(task);

        // OBTIENE RESULT (bloquea hasta que termine)
        Integer result = future.get();

        // IMPRIME result
        System.out.println("Result of Callable task: " + result);

        // CIERRA el executor
        executor.shutdown();
    }
}
