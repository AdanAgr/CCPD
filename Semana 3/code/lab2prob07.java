import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/* 

public class lab2prob07 { //Usando CAS y Locks
    private AtomicBoolean locked = new AtomicBoolean(false);
    public void lock() {
        while (!this.locked.compareAndSet(false, true)) {
            // busy wait - until compareAndSet() succeeds
        }
    }
    public void unlock() {
        this.locked.set(false);
    }
    public static void main(String[] args) {
        final long temporizador = System.currentTimeMillis();
        final lab2prob07 lock = new lab2prob07();
        final AtomicInteger counter = new AtomicInteger(0);
        final int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        lock.lock();
                        try {
                            // Critical section 
                            //- only one thread can increment the counter at a time
                            counter.incrementAndGet();
                        } finally {
                            lock.unlock();
                        }
                    }
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join(); // Wait for all threads to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final counter value: " + counter.get());

        System.out.println(System.currentTimeMillis() - temporizador);
    }
}

*/

/* 
public class lab2prob07 { //Usando synchronized
    private AtomicBoolean locked = new AtomicBoolean(false);

    public static void main(String[] args) {
        final long temporizador = System.currentTimeMillis();

        final Object lock = new Object();
        final AtomicInteger counter = new AtomicInteger(0);
        final int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        synchronized (lock) {
                            counter.incrementAndGet();
                            
                        }
                    }
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join(); // Wait for all threads to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final counter value: " + counter.get());

        System.out.println(System.currentTimeMillis() - temporizador);
    }
}
*/

public class lab2prob07 { //Usando CAS sin Locks   Aprovechando el uso de CAS es el método mas rapido
                            // Al usar variables atomicas (CAS) no hace falta crear metodos de sincronizacion
    private AtomicBoolean locked = new AtomicBoolean(false);
    public void lock() {
        while (!this.locked.compareAndSet(false, true)) {
            // busy wait - until compareAndSet() succeeds
        }
    }
    public void unlock() {
        this.locked.set(false);
    }
    public static void main(String[] args) {
        final long temporizador = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger(0);
        final int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        counter.incrementAndGet();
                    }
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join(); // Wait for all threads to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final counter value: " + counter.get());

        System.out.println(System.currentTimeMillis() - temporizador);
    }
}