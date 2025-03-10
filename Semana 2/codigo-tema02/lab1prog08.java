import java.util.Random;

class NewThread implements Runnable {
    String name;
    Thread t;
    Random rand = new Random();
    NewThread(String threadName) {
        name = threadName;
        t = new Thread(this, name);
        System.out.println("New thread created: " + t);
    }
    public void startThread() {
        t.start();
    }
    public void run() {
        // Simulate some CPU expensive task
        for(int i = 0; i < 100000000; i++) {
            rand.nextInt();
        }
        System.out.println("[" + Thread.currentThread().getName() + "] finished.");
    }
}


class lab1prog08 {
    public static void main(String[] args) {
        NewThread[] threads = new NewThread[5]; //Array Estático, no se puede cambiar el tamaño y es más rapido porque no se tiene que estar moviendo en memoria
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new NewThread("Thread " + i);
            threads[i].startThread();
        }
        for(int i = 0; i < threads.length; i++) {
            try {
                threads[i].t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread " + threads[i].t.getName() + " interrupted");
            }
        }
        System.out.println("[" + Thread.currentThread().getName() + "] All threads done!");
    }
}