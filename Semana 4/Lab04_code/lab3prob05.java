import java.util.LinkedList;
import java.util.Queue;

class SharedBuffer {
    private Queue<Integer> buffer = new LinkedList<>();
    private final int capacity = 10;

    public synchronized void produce(int item) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait(); // Wait when buffer is full
        }
        buffer.add(item);
        System.out.println("Produced: " + item);
        notifyAll(); // Notify all waiting consumers and producers
    }

    public synchronized int consume() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Wait when buffer is empty
        }
        int item = buffer.remove();
        System.out.println("Consumed: " + item);
        notifyAll(); // Notify all waiting consumers and producers
        return item;
    }
}

class Producer extends Thread {
    private SharedBuffer buffer;

    public Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                buffer.produce(i);  
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();  
                return; // Stop the thread if it's interrupted
            }
        }
    }
}

class Consumer extends Thread {
    private SharedBuffer buffer;

    public Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                int value = buffer.consume();  
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); 
                return; // Stop the thread if it's interrupted
            }
        }
    }
}
public class lab3prob05 {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();
        int numberOfProducers = 4;  
        int numberOfConsumers = 4; 

        for (int i = 0; i < numberOfProducers; i++) {
            new Producer(buffer).start();
        }
        for (int i = 0; i < numberOfConsumers; i++) {
            new Consumer(buffer).start();
        }
    }
}