class NewThread implements Runnable {
    Thread t;

    NewThread(String threadName) {
        // Create the thread object but do not start it in the constructor
        t = new Thread(this, threadName);
    }

    // Method to start the thread
    public void startThread() {
        t.start();
    }

    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted.");
        }
        System.out.println(Thread.currentThread().getName() + " exiting and will be destroyed.");
    }
}

class lab1prog04 {
    public static void main(String args[]) {
        NewThread nt = new NewThread("Demo Thread");
        NewThread nt2 = new NewThread("Demo Thread 2");
        nt.startThread(); // Start the thread using the method
        nt2.startThread(); // Start the thread using the method

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        System.out.println("Main thread exiting");
    }
}