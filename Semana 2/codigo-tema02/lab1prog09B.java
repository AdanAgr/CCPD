import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Mi nombre es: " + this.getName());
        System.out.println("Finalizado el proceso " + this.getName());
    }
}
public class lab1prog09B {
    public static void main(String[] args) {
        final int NUMERO_THREADS = 5;
        List<Thread> threadList = new ArrayList<>(NUMERO_THREADS);

        // Create threads
        for (int i = 1; i <= NUMERO_THREADS; ++i) {
            threadList.add(new MyThread());
            System.out.println("Thread " + i + " creado");
        }

        // Start threads
        Iterator<Thread> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            iterator.next().start();
        }

        // Join threads
        iterator = threadList.iterator(); // Resetting the iterator to iterate again
        while(iterator.hasNext()) {
            Thread t = iterator.next();
            try {
                t.join();
                System.out.println("Terminado realmente " + t.getName());
            } catch(InterruptedException e) {
                System.out.println("Error");
            }
        }

        System.out.println("El programa ha terminado");
    }
}