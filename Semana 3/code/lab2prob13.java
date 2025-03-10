import java.util.LinkedList; 

class SharedBuffer {
    private LinkedList<Integer> buffer = new LinkedList<>();
    private int capacity = 10;


    /*
     *  ADD CODE:   Write the "add" method that receives 'value'
     *      make the method synchronized
     *      it should wait if the buffer is full (at capacity); 
     *      otherwise it should add value to the buffer then 
     *      notify the rest of the threads.
     * 
    */
    public synchronized void add(int value) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait(); // Busy-waiting when buffer is full
        }
        buffer.add(value);
        notifyAll();
    }


    /*
     *  ADD CODE:   Write the "remove" method.  
     *      make the method synchronized
     *      it would wait if the buffer is empty, but otherwise it 
     *      should remove the first element of the buffer
     *      after that, it would notify other threads that it is 
     *      finished.
     */
    public synchronized int remove() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Busy-waiting when buffer is empty
        }
        int value = buffer.removeFirst();
        notifyAll();
        return value;


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
                buffer.add(i);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
        int value = 0;
        for (int i = 0; i < 50; i++) {
          try {
            value = buffer.remove();
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          System.out.println("Consumed: " + value);
        }
    }
}


public class lab2prob13 {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        producer.start();
        consumer.start();
    }
}