import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    int item;
    Node next;
    Lock lock = new ReentrantLock();

    public Node(int item) {
        this.item = item;
        this.next = null;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}


class LinkedList {
    private Node head;

    public LinkedList() {
        this.head = null;
    }

    public void add(int item) {
        if (head == null) {
            head = new Node(item);
            return;
        }

        Node current = head;
        Node previous = null;
        while (current != null && current.item < item) {
            previous = current;
            current = current.next;
        }

        Node newNode = new Node(item);
        if (previous == null) {
            newNode.next = head;
            head = newNode;
        } else {
            newNode.next = current;
            previous.next = newNode;
        }
    }
    
    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }
}


public class lab4prob02 {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.add(3);
        list.add(1);
        list.add(2);
        list.add(4);

        // Print the list to verify the output
        list.printList(); // Expected output: 1 2 3 4
    }
}