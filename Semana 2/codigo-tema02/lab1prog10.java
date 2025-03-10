import java.util.ArrayList;
import java.util.List;

class ThreadCalculator implements Runnable {
    @Override
    public void run() {
        long current = 1L;
        long max = 20000L;
        long numPrimes = 0L;
        System.out.printf("Thread '%s': START\n", Thread.currentThread().getName());
        while (current <= max) {
            if (isPrime(current)) {
                numPrimes++;
            }
            current++;
        }

        System.out.printf("Thread '%s': END. Number of Primes: %d\n", Thread.currentThread().getName(), numPrimes);
    }

    private boolean isPrime(long number) {
        if (number <= 2) {
            return true;
        }
        for (long i = 2; i < number; i++) {
            if ((number % i) == 0) {
                return false;
            }
        }
        return true;
    }
}

public class lab1prog10 {
    public static void main(String[] args) {
        System.out.printf("Minimum Priority: %s\n", Thread.MIN_PRIORITY);
        System.out.printf("Normal Priority: %s\n", Thread.NORM_PRIORITY);
        System.out.printf("Maximum Priority: %s\n", Thread.MAX_PRIORITY);

        List<Thread> threads = new ArrayList<>();
        List<Thread.State> status = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new ThreadCalculator());
            if ((i % 2) == 0) {
                thread.setPriority(Thread.MAX_PRIORITY);
            } else {
                thread.setPriority(Thread.MIN_PRIORITY);
            }
            thread.setName("My Thread " + i);
            threads.add(thread);
            status.add(thread.getState());
        }

        for (int i = 0; i < threads.size(); i++) {
            System.out.println("Main : Status of Thread " + i + " : " + threads.get(i).getState());
        }
        for (int i = 0; i < threads.size(); i++) {
            Thread thread = threads.get(i);
            thread.start();
        }

        boolean finish = false;
        while (!finish) {
            for (int i = 0; i < threads.size(); i++) {
                Thread thread = threads.get(i);
                if (thread.getState() != status.get(i)) {
                    writeThreadInfo(thread, status.get(i));
                    status.set(i, thread.getState());
                }
            }
            finish = threads.stream().allMatch(t -> t.getState() == Thread.State.TERMINATED);
        }
    }

    private static void writeThreadInfo(Thread thread, Thread.State state) {
        System.out.printf("Main : Id %d - %s\n", thread.getId(), thread.getName());
        System.out.printf("Main : Priority: %d\n", thread.getPriority());
        System.out.printf("Main : Old State: %s\n", state);
        System.out.printf("Main : New State: %s\n", thread.getState());
        System.out.printf("Main : ************************************\n");
    }
} 

