import java.util.concurrent.*;

public class FutureCallableExample {

    // MAIN
    public static void main(String[] args) throws Exception {

        // EXECUTOR SERVICE con THREAD POOL FIJO de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Definimos tarea CALLABLE
        Callable<String> tarea = () -> {
            Thread.sleep(1000); // Simula trabajo pesado
            return "¡Hola desde Callable!" + " ejecutada por " + Thread.currentThread().getName();
        };

        // Enviar tarea al executor y obtener un FUTURE (resultado futuro)
        Future<String> futuro = executor.submit(tarea);

        // Hacemos otras cosas aquí mientras la tarea se ejecuta...

        // Cuando necesitemos el resultado:
        String resultado = futuro.get(); // Bloquea hasta que termine
        System.out.println("Resultado: " + resultado);

        executor.shutdown();
    }
}
