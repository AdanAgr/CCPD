
class SharedResource {
    final Object lock = new Object();
    boolean flag = false;  
}


class CoffeeThread extends Thread {
    static int iInteger;
    private final SharedResource sharedResource;

    public CoffeeThread(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
      while (true) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Handle interruption
        }
        if (Thread.currentThread().getName().equals("Thread-0")) {
            firstCoffee();
        } else {
            secondCoffee();
        }
    }
    }

    void firstCoffee() {
        synchronized (sharedResource.lock) {
            while (sharedResource.flag) {
                try {
                    sharedResource.lock.wait();
                } catch (InterruptedException e) {
                    // Handle interruption
                }
            }
            // Added output to indicate action in FirstThread
            System.out.println(Thread.currentThread().getName() + " preparing coffee, iInteger=" + iInteger);
            
            sharedResource.flag = true;
            sharedResource.lock.notifyAll();
        }

    }
    void secondCoffee(){
        synchronized (sharedResource.lock) {
            while (!sharedResource.flag) {
                try {
                    sharedResource.lock.wait();
                } catch (InterruptedException e) {
                    // Handle interruption
                }
            }
            // Added output to indicate action in SecondThread
            iInteger++;
            System.out.println(iInteger);
            sharedResource.flag = false;
            sharedResource.lock.notifyAll();
        }
    }
}


public class lab2prob08 {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        CoffeeThread FirstCoffee = new CoffeeThread(sharedResource);
        CoffeeThread SecondCoffee = new CoffeeThread(sharedResource);
        FirstCoffee.start();
        SecondCoffee.start();
    }
}