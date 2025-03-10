class MyThread implements Runnable {
    private ThreadLocal<Integer> tL = new ThreadLocal<Integer>();

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            tL.set(i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Handle exception
            }
            System.out.printf("%s %d\n", Thread.currentThread().getName(), tL.get());
        }
    }
}

public class lab1prog16 {
    public static void main(String[] args) {
        MyThread task = new MyThread();
        Thread[] threads = new Thread[2]; // Array of threads

        // Initialize and start each thread
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // Join each thread
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // Handle exception
            }
        }

        System.out.println("All threads have finished.");
    }
}