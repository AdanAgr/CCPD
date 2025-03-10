import java.util.ArrayList;

class TaskExecutor {
    public void enterAndWait(int threadId) {
        try {
            System.out.println("Start Thread " + threadId);
            Thread.sleep((int) (Math.random() * 100));
            System.out.println("Finish Thread " + threadId);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

class Task implements Runnable {
    private final TaskExecutor taskExecutor;
    private final int threadId;


    Task(int threadId, TaskExecutor taskExecutor) {
        this.threadId = threadId;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run() {
        taskExecutor.enterAndWait(threadId);
    }
}

public class lab2prob11 {
    public static void main(String[] args) {
        TaskExecutor taskExecutor = new TaskExecutor();
        int numberOfTasks = 5;
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfTasks; i++) {
            Thread thread = new Thread(new Task(i, taskExecutor));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Finished.");
    }
}