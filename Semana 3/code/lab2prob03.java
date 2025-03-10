class TransThread extends Thread {
    private FinTrans ft;
    private Object lock; // Lock object shared between threads

    TransThread(FinTrans ft, String name, Object lock) {
        super(name);
        this.ft = ft;
        this.lock = lock; // Use a shared lock object
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            if (getName ().equals ("Deposit")){
                synchronized (lock) {
                // Start of deposit thread's critical code section
                ft.transName = "Deposit";
                try {
                    Thread.sleep ((int) (Math.random () * 1000));
                } catch (InterruptedException e) {}
                
                ft.amount = 2000.0;
                System.out.println (ft.transName + " " + ft.amount);
                // End of deposit thread's critical code section
                }
            } else {
                // Start of withdrawal thread's critical code section
                synchronized (lock) {
                
                ft.transName = "Withdrawal";
                try {
                    Thread.sleep ((int) (Math.random () * 1000));
                } catch (InterruptedException e)  {}
                ft.amount = 250.0;
                System.out.println (ft.transName + " " + ft.amount);
                // End of withdrawal thread's critical code section
            }
            }
        }
    }
}

class lab2prob03 {
    public static void main (String [] args)  {
	 FinTrans ft = new FinTrans();
       // Shared lock object
       Object lock = new Object();
	   TransThread tt1 = new TransThread(ft, "Deposit", lock);
       TransThread tt2 = new TransThread(ft, "Withdrawal", lock);
       tt1.start ();
       tt2.start ();
    }
 }