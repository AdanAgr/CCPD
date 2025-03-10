
class MyInterruptThread implements Runnable {
 
    @Override
    public void run() {
        try {
            // Thread goes to sleep for a long duration
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            // Handling the interrupt exception
            System.out.println("[" + Thread.currentThread().getName() + "] Interrupted by exception!");
        }
        // Loop continues until the thread is interrupted again
        while (!Thread.interrupted()) {
            // Loop body is empty
        }
        System.out.println("[" + Thread.currentThread().getName() + "] Interrupted for the second time.");
    }
}


public class lab1prog11 {
    public static void main(String[] args) throws InterruptedException {
        Thread myThread = new Thread(new MyInterruptThread(), "myThread");
        myThread.start();
         
        // Main thread sleeps for a while
        System.out.println("[" + Thread.currentThread().getName() + "] Sleeping in main thread for 5s...");
        Thread.sleep(5000);
         
        // Main thread interrupts 'myThread'
        System.out.println("[" + Thread.currentThread().getName() + "] Interrupting myThread");
        myThread.interrupt();
         
        // Main thread sleeps again
        System.out.println("[" + Thread.currentThread().getName() + "] Sleeping in main thread for 5s...");
        Thread.sleep(5000);
         
        // Main thread interrupts 'myThread' a second time
        System.out.println("[" + Thread.currentThread().getName() + "] Interrupting myThread");
        myThread.interrupt();
    }
}

