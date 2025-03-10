import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class NodeInt {
    int value;
    NodeInt next;

    public NodeInt(int value) {
        this.value = value;
        this.next = null;
    }
}

class SynchronizedStack {
    private NodeInt top;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmptyCondition = lock.newCondition();

    public SynchronizedStack() {
        this.top = null;
    }

    // Method to add an item to the top of the stack
    public void push(int value) {
        lock.lock();
        try {
            NodeInt newNode = new NodeInt(value);
            newNode.next = top;
            top = newNode;
            notEmptyCondition.signalAll(); // Signal any waiting pop or peek operations
        } finally {
            lock.unlock();
        }
    }

        // Method to remove and return the top item from the stack
        
        public int pop()  {
            lock.lock();
            try {
                while (top == null) {
                    notEmptyCondition.await();
                }
                int poppedValue = top.value;
                top = top.next;
                return poppedValue;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1; // Return a default value on interruption
            } finally {
                lock.unlock();
            }
        }

        // Method to return the top item without removing it
        public int peek()  {
            lock.lock();
            try {
                while (top == null) {
                    notEmptyCondition.await();
                }
                return top.value;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1; // Return a default value on interruption
            } finally {
                lock.unlock();
            }
        }

        // Method to check if the stack is empty
        public boolean isEmpty(){
            lock.lock();
            try {
                return top == null;
            } finally {
                lock.unlock();
            }
        }
}

public class lab3prob07B {

   public static void main(String[] args) {
      SynchronizedStack stack = new SynchronizedStack();
     // Example: 2 threads for pushing and 2 for popping
      int numThreads = 2;
      int numElements = 10;// Each thread will push/pop 10 elements

      startPushThreads(stack, numThreads, numElements);
      startPopThreads(stack, numThreads, numElements);
   }

    // Method to create and start threads for popping from the stack
    private static void startPopThreads(SynchronizedStack stack, int numThreads, int numElements) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < numElements; j++) {
                    try {
                        int poppedValue = stack.pop(); // Correctly handle InterruptedException
                        System.out.println("Thread " + Thread.currentThread().getId() + " popped: " + poppedValue);
                        Thread.sleep(100); // Simulate some delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return; // Exit the loop and thread on interruption
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
    }

        // Method to create and start threads for pushing onto the stack
    private static void startPushThreads(SynchronizedStack stack, 
                                         int numThreads, int numElements) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int threadNum = i; // For lambda expression
            Thread thread = new Thread(() -> {
                for (int j = 0; j < numElements; j++) {
                    stack.push(threadNum * numElements + j);
                    System.out.println("Thread " + threadNum + " pushing: " 
                          + (threadNum * numElements + j));
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
}