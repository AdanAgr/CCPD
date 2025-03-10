class NewThread implements Runnable {
    String name;
    Thread t;
    NewThread(String threadName) {
        name = threadName;
        t = new Thread(this, name);
        System.out.println("New thread created: " + t);
    }
    public void startThread() {
        t.start();
    }
    public boolean isThreadAlive() {
        return t.isAlive();
    }

    @Override
    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(name + " Child Thread: " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " interrupted.");
        }
        System.out.println(name + " exiting.");
    }
}

class lab1prog07 {
    public static void main(String[] args) {
        NewThread nt1 = new NewThread("One");
        NewThread nt2 = new NewThread("Two");
        NewThread nt3 = new NewThread("Three");

        nt1.startThread();
        nt2.startThread();
        nt3.startThread();

        // Allow some time for threads to potentially finish execution
        try {
            Thread.sleep(200); // Short sleep to demonstrate timing of isAlive
        } catch (InterruptedException e) {
            System.out.println("Main thread sleep interrupted");
        }

        System.out.println("Thread One is alive: " + nt1.isThreadAlive());
        System.out.println("Thread Two is alive: " + nt2.isThreadAlive());
        System.out.println("Thread Three is alive: " + nt3.isThreadAlive());

        try {
            nt1.t.join();
            nt2.t.join();
            nt3.t.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("Thread One is alive: " + nt1.isThreadAlive());
        System.out.println("Thread Two is alive: " + nt2.isThreadAlive());
        System.out.println("Thread Three is alive: " + nt3.isThreadAlive());
        System.out.println("Main thread exiting");
    }
}