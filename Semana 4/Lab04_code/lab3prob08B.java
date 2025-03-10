import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class SynchronizedQueue {
    private Node head, tail;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    private static class Node {
        int value;
        Node next;
        
        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    public SynchronizedQueue() {
        this.head = this.tail = null;
    }

    public void enqueue(int value) {
        lock.lock();
        try {
            Node newNode = new Node(value);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            notEmpty.signal(); // Signal any waiting dequeues
        } finally {
            lock.unlock();
        }
    }
    
    public int dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (head == null) {
                notEmpty.await();
            }
            int dequeuedValue = head.value;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            return dequeuedValue;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return head == null;
        } finally {
            lock.unlock();
        }
    }

    public int peek() {
        lock.lock();
        try {
            while (head == null) {
                notEmpty.await();
            }
            return head.value;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1; // Return a default value on interruption
        } finally {
            lock.unlock();
        }
    }
}


public class lab3prob08B {
    public static void main(String[] args) {
        SynchronizedQueue queue = new SynchronizedQueue();
        int numThreads = 2; // Number of threads for enqueueing and dequeueing
        int numElements = 10; // Each thread will enqueue/dequeue 10 elements

        startEnqueueThreads(queue, numThreads, numElements);
        startDequeueThreads(queue, numThreads, numElements);
    }

    private static void startEnqueueThreads(SynchronizedQueue queue, int numThreads, int numElements) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int threadNum = i; // For lambda expression
            Thread thread = new Thread(() -> {
                for (int j = 0; j < numElements; j++) {
                    queue.enqueue(threadNum * numElements + j);
                    System.out.println("Thread " + threadNum + " enqueued: " + (threadNum * numElements + j));
                    try {
                        Thread.sleep(100); // Simulate some delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
    }

    private static void startDequeueThreads(SynchronizedQueue queue, int numThreads, int numElements) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < numElements; j++) {
                    try {
                        int value = queue.dequeue();
                        System.out.println(Thread.currentThread().getName() + " dequeued: " + value);
                        Thread.sleep(100); // Simulate some delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
    }
}