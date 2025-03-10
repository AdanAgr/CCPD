public class lab1prog02 extends Thread {
    public lab1prog02(String name) {
        super(name); //Constructor del Thread que recibe el nombre del hilo
    }
    @Override
    public void run() {
        System.out.println("Executing thread: " + Thread.currentThread().getName());
        System.out.println("Thread priority: " + Thread.currentThread().getPriority());
        // You can add more code here to demonstrate the thread's functionality
    }
    public static void main(String[] args) {
        lab1prog02 myThread = new lab1prog02("myThread");
        lab1prog02 myThread2 = new lab1prog02("myThread2");
        myThread.setPriority(MIN_PRIORITY);
        myThread2.setPriority(MAX_PRIORITY);
        myThread.start();
        myThread2.start();
    }
}