import java.util.concurrent.TimeUnit;


class PrimeGenerator extends Thread {
    @Override
    public void run() {
        long number = 1L;
        while (!isInterrupted()) {
            if (isPrime(number)) {
                System.out.printf("Number %d is Prime\n", number);
            }
            number++;
        }
        System.out.println("The Prime Generator has been Interrupted");
    }

    private boolean isPrime(long number) {
        if (number <= 2) {
            return true;
        }
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}

public class lab1prog12 {
    public static void main(String[] args) {
        Thread task = new PrimeGenerator();
        task.setName("PrimeGeneratorThread");
        task.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while sleeping.");
        }

        task.interrupt();

        System.out.printf("Main: Status of the Thread: %s\n", task.getState());
        System.out.printf("Main: isInterrupted: %s\n", task.isInterrupted());
        System.out.printf("Main: isAlive: %s\n", task.isAlive());
    }
}

