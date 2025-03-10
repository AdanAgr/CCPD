
import java.util.ArrayList;
import java.util.List;
// version 1
class SynchronizedMethodList {
    private List<Integer> list = new ArrayList<>();

    /*
        ADD CODE:   Add the synchronized methods
    */ 
    public synchronized void addElement(int element) {
        list.add(element);
        System.out.println("Method (Synchonized Funcion) Added element: " + element + "Thread: " + Thread.currentThread().getName());
    }
    public synchronized int getElement(int index) {
        System.out.println("Method (Synchonized Funcion) Got element: " + list.get(index) + "Thread: " + Thread.currentThread().getName());	
        return list.get(index);
    }

}
// version 2
class SynchronizedBlockList {
    private List<Integer> list = new ArrayList<>();
    private final Object lock = new Object();

    /*
        ADD CODE:    Add the methods with synchronized blocks
     */
    public void addElement(int element) {
        synchronized (lock) {
            list.add(element);
            System.out.println("Method (Synchonized Lock) Added element: " + element + "Thread: " + Thread.currentThread().getName());
        }
    }
    public int getElement(int index) {
        synchronized (lock) {
            System.out.println("Method (Synchonized Lock) Got element: " + list.get(index) + "Thread: " + Thread.currentThread().getName());
            return list.get(index);
        }
    }

}

class ListOperationThread extends Thread {
    private Object list;
    private boolean useMethod;

    public ListOperationThread(Object list, boolean useMethod) {
        this.list = list;
        this.useMethod = useMethod;
    }

    @Override
    public void run() {
        if (useMethod && list instanceof SynchronizedMethodList) {
            SynchronizedMethodList methodList = (SynchronizedMethodList) list;
            methodList.addElement(1);
            methodList.getElement(0);
        } else if (!useMethod && list instanceof SynchronizedBlockList) {
            SynchronizedBlockList blockList = (SynchronizedBlockList) list;
            blockList.addElement(1);
            blockList.getElement(0);
        }
    }
}

public class lab2prob06 {
    public static void main(String[] args) {
        SynchronizedMethodList methodList = new SynchronizedMethodList();
        SynchronizedBlockList blockList = new SynchronizedBlockList();

        // Create threads for method synchronization
        for (int i = 0; i < 5; i++) {
            new ListOperationThread(methodList, true).start();
        }

        // Create threads for block synchronization
        for (int i = 0; i < 5; i++) {
            new ListOperationThread(blockList, false).start();
        }
    }
}
