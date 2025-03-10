class TaskExecutor {

    public void EnterAndWait(int threadNumber){

        try{
            System.out.println("Starting Thread " + threadNumber);
            Thread.sleep((int)(Math.random() * 100));
            System.out.println("Finishing Thread " + threadNumber);
        
        } catch(InterruptedException e){
            System.out.println(e.getMessage());
        }   
    }
}


class TaskRunnable implements Runnable{

    TaskExecutor taskExecutor;
    int threadNumber;
    
    TaskRunnable(int threadNumber,  TaskExecutor taskExecutor){
        this.threadNumber = threadNumber;
        this.taskExecutor = taskExecutor;    
    }
    
    public void run() {
        taskExecutor.EnterAndWait(threadNumber);
    }  
}


public class lab2prob09 {
    public static void main(String[] args) {
        TaskExecutor taskExecutor = new TaskExecutor();
        int threadNumber = 5;
        TaskRunnable taskRunnables[] = new TaskRunnable[threadNumber];
        Thread threads[] = new Thread[threadNumber];
        for(int i = 0; i < threadNumber; i++){
            taskRunnables[i] = new TaskRunnable(i, taskExecutor);
            threads[i] = new Thread(taskRunnables[i]);
            threads[i].start();
        }
        
        try{
            for(int i = 0; i < threadNumber; i++){
                threads[i].join();
            }
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Finished.");}
}