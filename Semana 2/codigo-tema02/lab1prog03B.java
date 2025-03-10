public class lab1prog03B implements Runnable {

    private final String taskName;

    public lab1prog03B(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println("Executing task: " + taskName + " in thread " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        // Create instances of Runnable tasks
        Runnable task1 = new lab1prog03B("Task1");
        Runnable task2 = new lab1prog03B("Task2");
        Runnable task3 = new lab1prog03B("Task3");

        // Execute tasks sequentially in the main thread
        task1.run();
        task2.run();
        task3.run();
    }
}
