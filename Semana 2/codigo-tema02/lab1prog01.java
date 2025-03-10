public class lab1prog01 {
    public static void main(String[] args) {
        printThreadDetails();
    }
    private static void printThreadDetails() {
        Thread currentThread = Thread.currentThread();
        long id          = currentThread.getId();
        String name      = currentThread.getName();
        int priority     = currentThread.getPriority();
        Thread.State state     = currentThread.getState();
        String threadGroupName = currentThread.getThreadGroup().getName();
        boolean isDaemon       = currentThread.isDaemon();
        int activeThreadCount  = currentThread.getThreadGroup().activeCount();

        System.out.printf("ID: %d | Name: %s | Priority: %d | State: %s | Thread Group Name: %s | Is Daemon: %s | Active Thread Count in Group: %d%n",
                id, name, priority, state, threadGroupName, isDaemon, activeThreadCount);
    }
}