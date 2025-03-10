import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer {
    // Lock to control access to the buffer
    final Lock lock = new ReentrantLock();

    // Condition variables to manage the state of the buffer
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    // The buffer and its metadata
    final Object[] items = new Object[100]; // Buffer size
    int putptr, takeptr, count; // Indices for putting and taking items, and the current number of items

    // Method to add an item to the buffer
    public void put(Object x) throws InterruptedException {
        lock.lock(); // Acquire the lock before modifying the buffer
        try {
            // Wait while the buffer is full
            while (count == items.length) {
                notFull.await();
            }
            // Add item to the buffer
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0; // Circular increment of put pointer
            ++count; // Increase the buffer item count
            notEmpty.signal(); // Signal any waiting consumers that there is data
        } finally {
            lock.unlock(); // Ensure the lock is always released
        }
    }
    // Method to remove and return an item from the buffer
    public Object take() throws InterruptedException {
        lock.lock(); // Acquire the lock before modifying the buffer
        try {
            // Wait while the buffer is empty
            while (count == 0) {
                notEmpty.await();
            }
            // Remove item from the buffer
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0; // Circular increment of take pointer
            --count; // Decrease the buffer item count
            notFull.signal(); // Signal any waiting producers that there is space
            return x;
        } finally {
            lock.unlock(); // Ensure the lock is always released
        }
    }
}

class Consumer implements Runnable {
    private final BoundedBuffer buffer;

    public Consumer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object item = buffer.take();
                System.out.println(Thread.currentThread().getName() + " consumed " + item);
                Thread.sleep(1000); // Simulate time to consume an item
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer was interrupted.");
        }
    }
}

class Producer implements Runnable {
    private final BoundedBuffer buffer;

    public Producer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object item = produceItem();
                buffer.put(item);
                System.out.println(Thread.currentThread().getName() + " produced " + item);
                Thread.sleep(1000); // Simulate time to produce an item
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer was interrupted.");
        }
    }

    // Simulate producing an item
    private Object produceItem() {
        return "item" + Math.random();
    }
}


public class lab3prob06 {
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();

        // Create and start multiple producer threads.
        int numberOfProducers = 3;
        for (int i = 0; i < numberOfProducers; i++) {
            Thread producerThread = new Thread(new Producer(buffer), "Producer-" + i);
            producerThread.start();
        }

        // Create and start multiple consumer threads.
        int numberOfConsumers = 3;
        for (int i = 0; i < numberOfConsumers; i++) {
            Thread consumerThread = new Thread(new Consumer(buffer), "Consumer-" + i);
            consumerThread.start();
        }
    }
}


/*   Alternative
public class lab3prob05 {
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();

        // Create and start multiple producer threads.
        int numberOfProducers = 3;
        for (int i = 0; i < numberOfProducers; i++) {
            new Thread(() -> {
                try {
                    while (true) { // Infinite loop to continuously produce items.
                        Object item = produceItem();
                        buffer.put(item);
                        System.out.println(Thread.currentThread().getName() + " produced " + item);
                        Thread.sleep(1000); // Simulate time to produce an item.
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Producer-" + i).start();
        }

        // Create and start multiple consumer threads.
        int numberOfConsumers = 3;
        for (int i = 0; i < numberOfConsumers; i++) {
            new Thread(() -> {
                try {
                    while (true) { // Infinite loop to continuously consume items.
                        Object item = buffer.take();
                        System.out.println(Thread.currentThread().getName() + " consumed " + item);
                        Thread.sleep(1000); // Simulate time to consume an item.
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Consumer-" + i).start();
        }
    }

    //   Method to simulate producing an item. In a real scenario, 
    ///  this could be replaced with actual item production logic.
    private static Object produceItem() {
        return "item" + Math.random();
    }
}
*/