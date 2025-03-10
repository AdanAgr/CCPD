import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

class SynchronizedDoublyLinkedList {
    private Node head = null;
    private Node tail = null; // New tail reference for the doubly linked list
    private final ReentrantLock lock = new ReentrantLock();

    private static class Node {
        int value;
        Node next;
        Node prev; // New previous pointer for the doubly linked list

        Node(int value) {
            this.value = value;
            this.next = null;
            this.prev = null; // Initialize previous pointer as null
        }
    }

    // Add element to the end of the list
    public void addLast(int value) {
        lock.lock();
        try {
            Node newNode = new Node(value);
            if (tail == null) {
                head = tail = newNode; // First element to be added
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
        } finally {
            lock.unlock();
        }
    }

    // Remove the first element from the list
    public void removeFirst() {
        lock.lock();
        try {
            if (head != null) {
                head = head.next;
                if (head != null) {
                    head.prev = null;
                } else {
                    tail = null; // List became empty
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // Print the list
    public void printList() {
        lock.lock();
        try {
            Node current = head;
            while (current != null) {
                System.out.print(current.value + " ");
                current = current.next;
            }
            System.out.println(); // New line after printing the list
        } finally {
            lock.unlock();
        }
    }
}

public class lab3prob09 {
    public static void main(String[] args) {
        SynchronizedDoublyLinkedList list = new SynchronizedDoublyLinkedList();

        // Create and start threads for adding elements to the list
        int numThreads = 2; // Number of threads
        int numElements = 5; // Elements per thread
        startAddLastThreads(list, numThreads, numElements);

        // Wait a bit for all add operations to finish (not the best practice, but simple)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Removing elements and printing the list in main thread to avoid concurrency issues
        list.removeFirst();
        list.printList();
    }

    private static void startAddLastThreads(SynchronizedDoublyLinkedList list, int numThreads, int numElements) {
        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < numElements; j++) {
                    list.addLast(j);
                }
            }).start();
        }
    }
}