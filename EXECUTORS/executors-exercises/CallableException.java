import java.util.concurrent.*;

// MAIN
public class CallableException {
    public static void main(String[] args) {
        
        // EXECUTORSERVICE con 1 solo hilo
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // TAREA TIPO CALLABLE
        // Incluye el método: V call() throws Exception
        // Que permite devolver un valor y lanzar excepciones (a diferencia de Runnable)
        Callable<Integer> task = () -> {
            Thread.sleep(1000); // simula trabajo pesado (1 sec)
            
            // Ejemplo de lanzar una excepción explícitamente
            if (true) {
                throw new Exception("¡Error dentro del método call()!");
            }

            // Ejemplo de devolver un valor si no hay excepción
            return 123;
        };

        // ENVIAR la tarea al executor --> retorna un FUTURE!!!
        Future<Integer> future = executor.submit(task);

        try {
            // OBTIENE RESULT (bloquea hasta que termine o lance excepción)
            Integer result = future.get();
            System.out.println("Result of Callable task: " + result);
        } catch (ExecutionException e) {
            // Captura la excepción que fue lanzada dentro de call()
            System.out.println("Excepción capturada desde call(): " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // CIERRA el executor
        executor.shutdown();
    }
}
