class NewThread extends Thread {

    NewThread(String threadName) {
        // Set the thread name in the constructor
        super(threadName);
    }


    @Override
    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(getName() + ": " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " interrupted.");
        }
        System.out.println(getName() + " exiting and will be destroyed.");
    }
}

class lab1prog05 {
    public static void main(String args[]) {
        NewThread nt = new NewThread("Demo Thread");
        NewThread nt2 = new NewThread("Demo Thread 2");
        nt.setPriority(Thread.MAX_PRIORITY); // Set the priority of the thread
        nt2.setPriority(Thread.MIN_PRIORITY); // Set the priority of the thread
        nt.start(); // Directly start the thread
        nt2.start(); // Directly start the thread
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        try {
            nt.join(); // Wait for the thread to finish
            nt2.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("Main thread exiting");
    }
}