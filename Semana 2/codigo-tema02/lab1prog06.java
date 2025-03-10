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
    @Override
    public void run() {  //Siempre voy a tener un run method en una clase que implemente Runnable
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

class lab1prog06 {
    public static void main(String[] args) {
        NewThread nt1 = new NewThread("One");
        NewThread nt2 = new NewThread("Two");
        NewThread nt3 = new NewThread("Three");

        nt1.startThread();
        nt2.startThread();
        nt3.startThread();

        try {
            nt1.t.join();
            nt2.t.join();
            nt3.t.join();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("Main thread exiting");
    }
}
