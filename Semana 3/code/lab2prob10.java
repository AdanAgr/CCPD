import java.util.ArrayList;
import java.util.List;

class TaskExecutor {
    public void enterAndWait(int threadNumber){
        try {
            System.out.println("Start Thread " + threadNumber);
            Thread.sleep((int) (Math.random() * 100));
            System.out.println("Finish Thread " + threadNumber);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

class TaskRunnable implements Runnable {
    private final TaskExecutor taskExecutor;
    private final int threadNumber;
    
    TaskRunnable(int threadNumber, TaskExecutor taskExecutor) {
        this.threadNumber = threadNumber;
        this.taskExecutor = taskExecutor;
    }
    
    @Override
    public void run() {
        taskExecutor.enterAndWait(threadNumber);
    }
}

public class lab2prob10 {
    public static void main(String[] args) {
        TaskExecutor taskExecutor = new TaskExecutor();
        int numberOfThreads = 5;
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < numberOfThreads; i++) {
            TaskRunnable taskRunnable = new TaskRunnable(i, taskExecutor);
            Thread thread = new Thread(taskRunnable);
            threads.add(thread);
            thread.start();
        }
        
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("Finished.");
    }
}