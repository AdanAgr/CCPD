import java.util.ArrayList;
import java.util.List;

class LifecycleTask implements Runnable {
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            // Simulating some work by sleeping
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("Finished run() of %s; time=%d State %s\n",
                          Thread.currentThread().getName(), (endTime - startTime),
                          Thread.currentThread().getState());
        System.out.println(Thread.currentThread().getName() + " Done!");
    }
}
public class lab1prog17 {
    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Thread thread = new Thread(new LifecycleTask(), "Thread-" + (i - 1));
            threads.add(thread);
            System.out.println(thread.getName() + " created, not assigned");
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            System.out.println(thread.getName() + "; State " + thread.getState());
        }

        // Optionally wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted");
            }
        }

        System.out.println("The Program is exiting");
    }
}