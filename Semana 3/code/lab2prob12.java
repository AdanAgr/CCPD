import java.util.LinkedList; 


class SharedBuffer {
    private LinkedList<Integer> buffer = new LinkedList<>();
    private int capacity = 10;



    /*
     *  ADD CODE:   Write the "add" method that receives 'value'
     *      use a synchronized block
     *      it should wait if the buffer is full (at capacity); 
     *      otherwise it should add value to the buffer then 
     *      notify the rest of the threads.
     * 
    */
    public void add(int value) {
        synchronized (this) {
            while (buffer.size() == capacity) {
                try {
                    wait(); // Busy-waiting when buffer is full
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            buffer.add(value);
            notifyAll();
        }
    }


    /*
     *  ADD CODE:   Write the "remove" method.  
     *      use a synchronized block
     *      it would wait if the buffer is empty, but otherwise it 
     *      should remove the first element of the buffer
     *      after that, it would notify other threads that it is 
     *      finished.
     */


    public int remove() {
        synchronized (this) {
            while (buffer.isEmpty()) {
                try {
                    wait(); // Busy-waiting when buffer is empty
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int value = buffer.removeFirst();
            notifyAll();
            return value;
        }
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
            buffer.add(i);
            System.out.println("Produced: " + i);
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
            int value = buffer.remove();
            System.out.println("Consumed: " + value);
        }
    }
}


public class lab2prob12 {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();
    }
}