import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;



class WorkerThread implements Runnable {
    private final int threadId;
    private static ReentrantLock lock; // Changed to non-static to be initialized via constructor
    private static Condition[] conditions;
    private static int currentMaxId;

    public WorkerThread(int id, int maxId, ReentrantLock lock) {
        this.threadId = id;
        currentMaxId = maxId;
        WorkerThread.lock = lock; // Assign the lock passed from main
        if (conditions == null) {
            conditions = new Condition[maxId + 1];
            for (int i = 1; i <= maxId; i++) {
                conditions[i] = lock.newCondition();
            }
        }
    }

    // Getter for conditions
    public static Condition getCondition(int index) {
        return conditions[index];
    }

    @Override
    public void run() {
        lock.lock();
        try {
            while (threadId != currentMaxId) {
                conditions[threadId].await();
            }
            // Critical section
            System.out.println("Thread with ID "  + threadId + " entering critical section.");
            Thread.sleep(1000); // Simulate work
            System.out.println("Thread with ID "  + threadId + " leaving critical section.");
            currentMaxId--; // Move to the next thread
            if (currentMaxId > 0) {
                conditions[currentMaxId].signal(); // Notify the next thread
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}


public class lab3prob04 {
    public static void main(String[] args) {
        final int numberOfThreads = 5;
        ReentrantLock lock = new ReentrantLock();
        List<Thread> threads = new ArrayList<>();

        // Initialize threads
        for (int i = 1; i <= numberOfThreads; i++) {
            Thread thread = new Thread(new WorkerThread(i, numberOfThreads, lock));
            threads.add(thread);
        }

        // Start threads in reverse order
        for (int i = numberOfThreads - 1; i >= 0; i--) {
            threads.get(i).start();
        }

        // Signal the first thread to start
        lock.lock();
        try {
            if (numberOfThreads > 0) {
                WorkerThread.getCondition(numberOfThreads).signal();
            }
        } finally {
            lock.unlock();
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