import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

class SafeTask implements Runnable {
    private static ThreadLocal<Date> startDate = ThreadLocal.withInitial(Date::new);

    @Override
    public void run() {
        System.out.printf("Starting Thread: %s : %s\n", Thread.currentThread().getId(), startDate.get());
        try {
            TimeUnit.SECONDS.sleep((int) Math.rint(Math.random() * 10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getId(), startDate.get());
    }
}

public class lab1prog15 {
    public static void main(String[] args) {
        SafeTask task = new SafeTask();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 2 * Runtime.getRuntime().availableProcessors(); i++) {
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Optional: Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}