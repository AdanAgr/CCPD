public class lab3prob02 {
    public static void main(String[] args) {
        int maxCount = 10;
        int startingTurn = 2;
        int numberOfThreads = 10;

        A sharedObject = new A(maxCount, startingTurn, numberOfThreads);
        B[] threadArray = new B[numberOfThreads];

        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfThreads; i++) {
            int nextThreadIndex = (i + 1) % numberOfThreads;
            threadArray[i] = new B(i, sharedObject, numberOfThreads, nextThreadIndex, threadArray);
        }

        for (B thread : threadArray) {
            thread.start();
        }

        synchronized (threadArray[startingTurn]) {
            threadArray[startingTurn].notify();
        }

        for (B thread : threadArray) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("A thread was interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        long estimatedTime = System.nanoTime() - startTime;
        System.out.println((float) estimatedTime / 1_000_000 + " ms");
    }
}
class A {
    private volatile int currentCount;
    private int turn, numberOfThreads;
    private boolean done = false;

    public A(int currentCount, int turn, int numberOfThreads) {
        this.currentCount = currentCount;
        this.turn = turn;
        this.numberOfThreads = numberOfThreads;
    }

    public synchronized void enterAndWait(int threadId) {
        try {
            System.out.println("Start Thread " + threadId + " currentCount=" + currentCount);
            Thread.sleep((int) (Math.random() * 100));
            System.out.println("Finish Thread " + threadId + " currentCount" + currentCount);
            currentCount--;
            if (currentCount <= 0) {
                done = true;
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isFinished() {return currentCount <= 0; }
    public int getTurn() {return turn; }
    public int getCurrentCount() {return currentCount; }
    
    public void setTurn(int turn) {this.turn = turn;  }
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void setDone(boolean done) { this.done = done;    }
    public boolean getDone() {return this.done; }
}

class B extends Thread {
    A sharedA;
    B[] threads;
    int threadId;
    private int nextThreadId;
    private int numberOfThreads;

    boolean done = false;

    B(int threadId, A sharedA, int numberOfThreads, int nextThreadId, B[] threads) {
        this.threadId = threadId;
        this.sharedA = sharedA;
        this.numberOfThreads = numberOfThreads;
        this.nextThreadId = nextThreadId;
        this.threads = threads;
    }

    public void run() {
        try {
            synchronized (this) {
                wait();
            }
            boolean flag = false;
            while (!flag) {
                if (sharedA.getDone()) {
                    synchronized (threads[nextThreadId]) {
                        threads[nextThreadId].notify();
                    }
                    flag = true;
                } else {
                    synchronized (threads[nextThreadId]) {
                        sharedA.enterAndWait(threadId);
                        if (sharedA.getCurrentCount() < 0) {
                            done = true;
                            sharedA.setDone(done);
                        }
                        threads[nextThreadId].notify();
                    }
                    synchronized (this) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}