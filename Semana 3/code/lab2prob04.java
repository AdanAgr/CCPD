
class CounterThread extends Thread {
    private SharedCounter sharedCounter;

    public CounterThread(SharedCounter sharedCounter) {
        this.sharedCounter = sharedCounter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            sharedCounter.increment();
        }
    }
}
class SharedCounter {

    public void increment() {
        count++;
    }
    private int count = 0;
    /*
     * ADD:  a synchronized increment()
     *       method
     */

    public int getCount() {
        return count;
    }
}



public class lab2prob04 {
    public static void main(String[] args) throws InterruptedException {
        SharedCounter sharedCounter = new SharedCounter();
        Thread[] threads = new Thread[10];

        // Create and start threads
        for (int i = 0; i < threads.length; i++) {
            /*    ADD CODE:
             * 
             *    Launch threads with shared object
             */
            threads[i] = new CounterThread(sharedCounter);
            threads[i].start();
            threads[i].join(); //Si no usamos el join, los threads se pisan entre ellos y no cuenta bien.
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Final Counter Value: " + sharedCounter.getCount());
    }
}

