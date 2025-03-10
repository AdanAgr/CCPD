import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class WorkerThread implements Runnable {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition turnCondition = lock.newCondition();
    private static int currentMaxId;
    private final int threadId;

    public WorkerThread(int id, int maxId) {
        this.threadId = id;
        currentMaxId = maxId;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            while (threadId != currentMaxId) {
                turnCondition.await();
            }
            // Critical section
            System.out.println("Thread with ID " + threadId 
                       + " entering critical section.");
            Thread.sleep(1000); // Simulate work
            System.out.println("Thread with ID " + threadId + 
                        " leaving critical section.");
            currentMaxId--; // Move to the next thread
            turnCondition.signalAll(); // Notify all waiting threads 
                                       // to check their condition
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
public class lab3prob03 {
    public static void main(String[] args) {
        final int numberOfThreads = 5;
        List<Thread> threads = new ArrayList<>();

        // Create and start threads in reverse order
        for (int i = numberOfThreads; i > 0; i--) {
            Thread thread = new 
                 Thread(new WorkerThread(i, numberOfThreads));
            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All threads have executed.");
    }
}